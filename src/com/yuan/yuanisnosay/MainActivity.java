package com.yuan.yuanisnosay;

import java.io.IOException;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccessable;
import com.yuan.yuanisnosay.ui.Util;

public class MainActivity extends ActionBarActivity {
	YuanApplication mApp;

	Button btnLogin;
	Button btnLogout;
	Button btnGetInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnLogin=(Button)findViewById(R.id.button_login);
		btnLogout=(Button)findViewById(R.id.button_logout);
		btnGetInfo=(Button)findViewById(R.id.button_getInfo);
		
		View.OnClickListener listener=new ButtonListener();
		btnLogin.setOnClickListener(listener);
		btnLogout.setOnClickListener(listener);
		btnGetInfo.setOnClickListener(listener);

		mApp = (YuanApplication) getApplication();
		
		

		new Thread() {
			@Override
			public void run() {
				try {
					ServerAccessable a = new ServerAccess();
					String b = a.registerNewUser("1", "");
					Log.e("mzb", "" + b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	class ButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View button) {
			switch (button.getId()) {
			case R.id.button_login:
				mApp.getLogin().login(MainActivity.this);
				break;
			case R.id.button_logout:
				mApp.getLogin().logout(MainActivity.this);
				break;
			case R.id.button_getInfo:
				Util.showResultDialog(MainActivity.this, "openid:"+mApp.getLogin().getOpenId()+"\nqqtoken:"+mApp.getLogin().getQQToken(), "");
				break;
			}
		}

	}
}
