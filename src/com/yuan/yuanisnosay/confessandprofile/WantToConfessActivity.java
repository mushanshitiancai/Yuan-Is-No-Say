package com.yuan.yuanisnosay.confessandprofile;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.ui.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WantToConfessActivity extends Activity implements
		TencentLocationListener {
	private static final String TAG = "WantToConfessActivity";
	private static final int LOCATING_MSG_CODE = -1;
	private static final String LOCATING_MSG_DOT_COUNT_CODE = "dotCount";
	private static final int CONFESS_SEND_SUCCESS = 0;
	private static final int CONFESS_SEND_FAIL_NOT_LOGIN = 1;
	private static final int CONFESS_SEND_FAIL_OTHER = 2;
	
	YuanApplication mApp;

//	private DateFormat mSdf;
	private TencentLocationModule mLocationModule;
	protected TencentLocationManager mLocationManager;
//	private boolean mStarted;
	private boolean mLocated;

	private TextView txtBack;
	private EditText editConfessContentInput;
	private TextView txtLocationShow;
	private Button btnSend;

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
				if (!mLocated) {
					int count = msg.getData().getInt(
							LOCATING_MSG_DOT_COUNT_CODE);
					StringBuffer sb = new StringBuffer();
					sb.append("正在定位");
					for (int i = 0; i < count + 1; i++) {
						sb.append(".");
					}
					txtLocationShow.setText(sb.toString());
				}
				break;
			case CONFESS_SEND_SUCCESS:
				Util.dismissDialog();
				Util.showToast(WantToConfessActivity.this, "表白成功！");
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				finish();
				break;
			case CONFESS_SEND_FAIL_NOT_LOGIN:
				Util.dismissDialog();
				Util.showToast(WantToConfessActivity.this, "你还没登录呢，先登录吧...");
				finish();
				break;
			case CONFESS_SEND_FAIL_OTHER:
				Util.dismissDialog();
				Util.showToast(WantToConfessActivity.this, "网络不给力啊，发送失败了！");
//				finish();
				break;
				
			}
		}
	};

	private void initLocate() {
		TencentLocationRequest tencentLocationReq = TencentLocationRequest
				.create().setRequestLevel(
						TencentLocationRequest.REQUEST_LEVEL_POI);
		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationManager.requestLocationUpdates(tencentLocationReq, this);

//		mStarted = true;
		mLocationModule = new TencentLocationModule();

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				int tmp = 0;
				while (!mLocated) {
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
		txtLocationShow = (TextView) findViewById(R.id.view_location);

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

		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!mLocated) {
					Util.showToast(WantToConfessActivity.this, "请确定网络连接后,再重新发送");
					return;
				}
				final String openId = mApp.getLogin().getOpenId();
				final String text = editConfessContentInput.getText()
						.toString();
				final String addr = mLocationModule.getAddr();
				final double latitude = mLocationModule.getLatitude();
				final double longtitude = mLocationModule.getLongitude();
				if (Bimp.drr.size() != 0) {
					
					try {
						ServerAccess.postNewConfess(openId, text, addr,longtitude,
								latitude,  Bimp.drr.get(0),
								new ServerAccess.ServerResponseHandler(){

									@Override
									public void onSuccess(
											JSONObject result) {
										// TODO Auto-generated method stub
										if(result == null) {
											return;
										}
										else {
											try {
												int status = result.getInt("status");
												switch(status) {
												case CONFESS_SEND_SUCCESS:
													Util.dismissDialog();
													Util.showToast(WantToConfessActivity.this, "表白成功！");
													break;
													
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}

									@Override
									public void onFailure(Throwable error) {
										// TODO Auto-generated method stub
										Util.dismissDialog();
										Util.showToast(WantToConfessActivity.this, "网络不给力啊，检查网络连接再来设置吧");
									}
							
						});
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Util.showProgressDialog(WantToConfessActivity.this, "请稍后",
							"正在发送...");
				} else {
					ServerAccess.postNewConfess(openId, text, addr,longtitude,
						latitude,new ServerAccess.ServerResponseHandler(){

							@Override
							public void onSuccess(
									JSONObject result) {
								// TODO Auto-generated method stub
								if(result == null) {
									return;
								}
								else {
									try {
										int status = result.getInt("status");
										switch(status) {
										case CONFESS_SEND_SUCCESS:
											Util.dismissDialog();
											Util.showToast(WantToConfessActivity.this, "表白成功！");
											break;
											
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}

							@Override
							public void onFailure(Throwable error) {
								// TODO Auto-generated method stub
								Util.dismissDialog();
								Util.showToast(WantToConfessActivity.this, "网络不给力啊，检查网络连接再来表白吧");
							}
					
				});
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
					btnSend.setEnabled(true);
				} else {
					btnSend.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		txtBack = (TextView) findViewById(R.id.back);
		txtBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeSaveAlert();
			}

		});
	}

	@Override
	public void onLocationChanged(TencentLocation location, int error,
			String reason) {
//		mSdf = SimpleDateFormat.getTimeInstance();
		if (error == TencentLocation.ERROR_OK) {
			setTencentLocationModule(location);
			txtLocationShow.setText(mLocationModule.getCity()+" "+mLocationModule.getRegionName());
			mLocated = true;
		} else if (error == TencentLocation.ERROR_NETWORK) {
			// Toast toast = Toast.makeText(this, "网络不可用,请检查网络连接",
			// Toast.LENGTH_SHORT);
			// toast.show();
		}
	}

	public void setTencentLocationModule(TencentLocation location) {
		mLocationModule.setLatitude(location.getLatitude());
		mLocationModule.setLongitude(location.getLongitude());
		mLocationModule.setAccuracy(location.getAccuracy());
		mLocationModule.setNation(location.getNation());
		mLocationModule.setProvince(location.getProvince());
		mLocationModule.setCity(location.getCity());
		mLocationModule.setDistrict(location.getDistrict());
		mLocationModule.setRegionName(location.getPoiList().get(0).getName());
	}

	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
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
