package com.yuan.yuanisnosay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccessable;
import com.yuan.yuanisnosay.ui.Util;

public class ListTestActivity extends ActionBarActivity {
	private static final int M_REGISTER_END = 0;
	private static final int M_UPLOADPIC_END = 1;
	YuanApplication mApp;

	List<Button> btnList;
	int btnIdArr[] = { R.id.button_login, R.id.button_logout,
			R.id.button_getInfo, R.id.button_register,R.id.button_upPic };

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_test);

		mApp = (YuanApplication) getApplication();

		btnList = new ArrayList<Button>();
		View.OnClickListener listener = new ButtonListener();

		for (int id : btnIdArr) {
			Button btn = (Button) findViewById(id);
			btnList.add(btn);
			btn.setOnClickListener(listener);
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case M_REGISTER_END:
					Util.showResultDialog(ListTestActivity.this,
							msg.obj.toString(), "");
					break;
				case M_UPLOADPIC_END:
					Util.showResultDialog(ListTestActivity.this,
							msg.obj.toString(), "");
					break;
				}
			}
		};

	}

	class ButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View button) {
			switch (button.getId()) {
			case R.id.button_login:
				mApp.getLogin().login(ListTestActivity.this);
				break;
			case R.id.button_logout:
				mApp.getLogin().logout(ListTestActivity.this);
				break;
			case R.id.button_getInfo:
				Util.showResultDialog(ListTestActivity.this, "openid:"
						+ mApp.getLogin().getOpenId() + "\nqqtoken:"
						+ mApp.getLogin().getQQToken(), "");
				break;
			case R.id.button_register:
				registerTest();
				break;
			case R.id.button_upPic:
				uploadPicTest();
				break;
			}
		}
	}

	private void registerTest() {
		new Thread() {
			@Override
			public void run() {
				try {
					ServerAccess a = new ServerAccess();
					String b = a.registerNewUser("1", "");
					Log.e("mzb", "" + b);
					Message msg = mHandler.obtainMessage();
					msg.what = M_REGISTER_END;
					msg.obj = b;
					mHandler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void uploadPicTest(){
		final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
		final File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
			
		new Thread(){
			@Override
			public void run() {
				ServerAccess a = new ServerAccess();
				try {
					String b=a.uploadPic(testImageOnSdCard.getCanonicalPath());
					
					Message msg = mHandler.obtainMessage();
					msg.what = M_UPLOADPIC_END;
					msg.obj = b;
					mHandler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_test, menu);
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
}
