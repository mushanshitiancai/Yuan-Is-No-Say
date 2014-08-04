package com.yuan.yuanisnosay;

import java.io.File;

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
		
		//建立app sd卡目录
		File folder = new File(Const.YUAN_FOLDER_NAME);
		if(folder.exists()==false){
			folder.mkdir();
		}
		
		mLogin=Login.getInstance(getApplicationContext());
	}
	
	public Login getLogin(){
		return mLogin;
	}
}
