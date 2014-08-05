package com.yuan.yuanisnosay.network;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
	private static final String TAG = "yuan_NetWork";
	private static Network mInstance = null;
	private Application mApp;
	
	public static final Network getInstance(Application app){
		if(mInstance ==null){
			mInstance = new Network(app);
		}
		return mInstance;
	}
	
	private Network(){
	}
	
	private Network(Application app){
		mApp = app;
	}
	
	public boolean isOnline(){
		ConnectivityManager conMgr = (ConnectivityManager)mApp.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
	
}
