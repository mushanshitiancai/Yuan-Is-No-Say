package com.yuan.yuanisnosay.confessandprofile;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.Status;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;
import com.yuan.yuanisnosay.ui.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WantToConfessActivity extends Activity {
	private static final String TAG = "WantToConfessActivity";
	private static final int LOCATING_MSG_CODE = -1;
	private static final String LOCATING_MSG_DOT_COUNT_CODE = "dotCount";
	private static final int M_LOCATION = 0;
	
	YuanApplication mApp;

	private TencentLocationModule mLocationModule;
	protected TencentLocationManager mLocationManager;
	private TencentLocationHelper mLocationHelper;

	private ImageView imgBack;
	private EditText editConfessContentInput;
	private TextView txtLocationShow;
	private TextView txtSend;

	private GridView gridThumbnailShower;
	private PicThumAdapter mthumbnailAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wantto_confess);

		mApp = (YuanApplication) getApplication();

		initLocate();
		initView();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case LOCATING_MSG_CODE:
				if (!mLocationHelper.getLocated()) {
					int count = msg.getData().getInt(
							LOCATING_MSG_DOT_COUNT_CODE);
					StringBuffer sb = new StringBuffer();
					sb.append("  正在定位");
					for (int i = 0; i < count + 1; i++) {
						sb.append(".");
					}
					txtLocationShow.setText(sb.toString());
				}
				break;
			case M_LOCATION:
				mLocationModule = (TencentLocationModule)msg.obj;
				mLocationManager.removeUpdates(mLocationHelper);
				txtLocationShow.setText("  "+mLocationModule.getCity()+" "+mLocationModule.getRegionName());
				break;
				
			}
		}
	};

	private void initLocate() {
		TencentLocationRequest tencentLocationReq = TencentLocationRequest
				.create().setRequestLevel(
						TencentLocationRequest.REQUEST_LEVEL_POI);
		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationHelper = new TencentLocationHelper(mHandler);
		mLocationManager.requestLocationUpdates(tencentLocationReq, mLocationHelper);

		mLocationModule = new TencentLocationModule();

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				int tmp = 0;
				while (!mLocationHelper.getLocated()) {
					Message msg = new Message();
					Bundle data = new Bundle();
					tmp++;
					tmp %= 3;
					data.putInt(LOCATING_MSG_DOT_COUNT_CODE, tmp);
					msg.setData(data);
					msg.what = LOCATING_MSG_CODE;
					mHandler.sendMessage(msg);
				}
			}

		}, 0, 2000);

	}
	
	private void initView() {
		editConfessContentInput = (EditText) findViewById(R.id.text_edit);
		txtLocationShow = (TextView) findViewById(R.id.textView_location);

		gridThumbnailShower = (GridView) findViewById(R.id.img_show_thumbnail);
		gridThumbnailShower.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mthumbnailAdapter = new PicThumAdapter(this,1);
		mthumbnailAdapter.update();
		gridThumbnailShower.setAdapter(mthumbnailAdapter);
		gridThumbnailShower.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					Intent intent = new Intent(WantToConfessActivity.this,
							ImageBucketActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(WantToConfessActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

		txtSend = (TextView) findViewById(R.id.textView_send);
		txtSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!mLocationHelper.getLocated()) {
					Util.showToast(WantToConfessActivity.this, "请确定网络连接后,再重新发送");
					return;
				}
				final String openId = mApp.getLogin().getOpenId();
				final String text = editConfessContentInput.getText()
						.toString();
				final String addr = mLocationModule.getRegionName();
				final double latitude = mLocationModule.getLatitude();
				final double longtitude = mLocationModule.getLongitude();
				
				try{
					if (Bimp.drr.size() != 0) {
						ServerAccess.postNewConfess(openId, text, addr,longtitude,
								latitude,  Bimp.drr.get(0),new ConfessResponseHandler());
					}else{
						ServerAccess.postNewConfess(openId, text, addr,longtitude,
								latitude,new ConfessResponseHandler());
					}
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				Util.showProgressDialog(WantToConfessActivity.this, "请稍后",
						"正在发送...");
			}
		});

		editConfessContentInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.toString().trim().length() != 0) {
					txtSend.setEnabled(true);
				} else {
					txtSend.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		imgBack = (ImageView) findViewById(R.id.imageView_back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeSaveAlert();
			}

		});
	}
	
	private class ConfessResponseHandler implements ServerResponseHandler{

		@Override
		public void onSuccess(JSONObject result) {
			if(result == null) {
				return;
			}
			else {
				try {
					int status = result.getInt("status");
					switch(status) {
					case Status.Confess.CONFESS_SEND_SUCCESS:
						Util.dismissDialog();
						Util.showToast(WantToConfessActivity.this, "表白成功！");
						confessClear();
						finish();
						break;
					default:
						Util.dismissDialog();
						Util.showToast(WantToConfessActivity.this, "表白失败！");
						break;	
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onFailure(Throwable error) {
//			Util.dismissDialog();
//			Util.showToast(WantToConfessActivity.this, "网络不给力啊，检查网络连接再来表白吧");
			Log.e(TAG, error.toString());
		}
		
	}
	
	private void confessClear(){
		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		mLocationManager.removeUpdates(mLocationHelper);
	}
	
	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	@Override
	protected void onRestart() {
		mthumbnailAdapter.update();
		super.onRestart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showChangeSaveAlert();
		}
		return false;
	}

	private void showChangeSaveAlert() {
		if ((Bimp.bmp.size() == 0 || Bimp.drr.size() == 0)
				&& (editConfessContentInput.getText() == null || editConfessContentInput
						.getText().toString().trim().length() == 0)) {
			finish();
			return;
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("我要表白").setMessage("确定放弃此次编辑？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				finish();
			}

		});
		builder.setNegativeButton("取消", null).create().show();
	}
}
