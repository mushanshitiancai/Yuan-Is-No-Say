package com.yuan.yuanisnosay;

import java.io.File;

import android.app.Application;

import com.yuan.yuanisnosay.login.Login;
import com.yuan.yuanisnosay.storage.StorageModel;

/**
 * app入口
 * @author 志彬
 *
 */
public class YuanApplication extends Application{
	
	private Login mLogin;
	private StorageModel mStorageModel;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mLogin = Login.getInstance(getApplicationContext());
		mStorageModel = StorageModel.getInstance();
		
		//建立app sd卡目录
		StorageModel.createAppFolder();
		
		mStorageModel.read();
		
	}
	
	public Login getLogin(){
		return mLogin;
	}
	
	public StorageModel getStorage(){
		return mStorageModel;
	}
}
