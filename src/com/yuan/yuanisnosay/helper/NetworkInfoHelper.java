package com.yuan.yuanisnosay.helper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;

public class NetworkInfoHelper {
	public void checkNetworkInfo(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// wifi
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return;

		context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));// 进入无线网络配置界面
		// startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		// //进入手机中的wifi网络设置界面
	}
}
