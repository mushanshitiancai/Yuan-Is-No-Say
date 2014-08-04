package com.yuan.yuanisnosay.confessandprofile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.ui.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PersonalProfileActivity extends Activity {
	
	private static final String TAG = "PersonalProfileActivity";
	private static final int INFO_SEND_SUCCESS = 0;
	private static final int INFO_SEND_FAIL_NOT_LOGIN = 1;
	private static final int INFO_SEND_FAIL_OTHER = 2;
	
	class ViewHolder {
		private TextView txtBack;
		private Button btnEnter;
		private EditText editNickName;
		private GridView gridThumbnailShower;
	}
	
	private ViewHolder mHolder;
	private PicThumAdapter mthumbnailAdapter;
	private YuanApplication mApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_profile);
		
		initView();
		mApp = (YuanApplication) getApplication();
	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case INFO_SEND_SUCCESS:
				Util.dismissDialog();
				Util.showToast(PersonalProfileActivity.this, "资料设置成功！");
				break;
			case INFO_SEND_FAIL_NOT_LOGIN:
				Util.dismissDialog();
				Util.showToast(PersonalProfileActivity.this, "你还没登录呢，先登录吧...");
				break;
			case INFO_SEND_FAIL_OTHER:
				Util.dismissDialog();
				Util.showToast(PersonalProfileActivity.this, "网络不给力啊，检查网络连接再来设置吧");
				break;
			}
		}
	};
	
	private void initView(){
		mHolder = new ViewHolder();
		mHolder.txtBack = (TextView)findViewById(R.id.back);
		mHolder.btnEnter = (Button)findViewById(R.id.btn_send);
		mHolder.gridThumbnailShower = (GridView)findViewById(R.id.img_show_thumbnail);
		mHolder.gridThumbnailShower.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mHolder.editNickName = (EditText)findViewById(R.id.edit_nickname);
		
		mthumbnailAdapter = new PicThumAdapter(this,0);
		mthumbnailAdapter.update();
		mHolder.gridThumbnailShower.setAdapter(mthumbnailAdapter);
		mHolder.gridThumbnailShower.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					Intent intent = new Intent(PersonalProfileActivity.this,
							ImageBucketActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(PersonalProfileActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		
		mHolder.btnEnter.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String text = mHolder.editNickName.getText()
						.toString();
				final String openId = mApp.getLogin().getOpenId();
				Log.e("personalprofileActivity", openId);
				
				if (Bimp.drr.size() != 0) {
					new Thread() {
						public void run() {
							try {
//								long startTime = System.currentTimeMillis();
								String response = ServerAccess.updateUserInfo(openId
										, "1",text,Bimp.drr.get(0) );
								if(response == null) {
									return;
								}
//								long endTime = System.currentTimeMillis();
								JSONObject status = new JSONObject(response);
								int responseCode = status.getInt("status");
								mHandler.sendEmptyMessage(responseCode);
								Log.e("personalprofileActivity", response);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								Log.e(TAG, "");
								e.printStackTrace();
							}

						}

					}.start();
					Util.showProgressDialog(PersonalProfileActivity.this, "请稍后",
							"正在上传资料...");
				} else {
					new Thread() {
						public void run() {
//							AssetManager aManager= getApplication().getAssets();
//							try {
//								InputStream is = aManager.open("ic_launcher.png");
//								AssetFileDescriptor afd = aManager.openFd("ic_launcher.png");
//								
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							try {
//								
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
					}.start();
				}
				// finish();
			}
		});

		mHolder.editNickName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				int strLen = s.toString().trim().length();
				if(strLen == 0) {
					mHolder.btnEnter.setEnabled(false);
					return;
				}
				mHolder.btnEnter.setEnabled(true);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		mHolder.txtBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeSaveAlert();
			}

		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
				&& (mHolder.editNickName.getText() == null || mHolder.editNickName
						.getText().toString().trim().length() == 0)) {
			finish();
			return;
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("设置个人资料").setMessage("确定放弃此次编辑？");
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
