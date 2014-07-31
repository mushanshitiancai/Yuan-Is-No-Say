package com.yuan.yuanisnosay;

import android.app.Application;

import com.yuan.yuanisnosay.login.Login;

/**
 * app入口
 * @author 志彬
 *
 */
public class YuanApplication extends Application{
	
	private Login mLogin;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mLogin=Login.getInstance(getApplicationContext());
	}
	
	public Login getLogin(){
		return mLogin;
	}
}
