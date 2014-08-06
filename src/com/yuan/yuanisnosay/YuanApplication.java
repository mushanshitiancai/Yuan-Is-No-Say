package com.yuan.yuanisnosay;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.yuan.yuanisnosay.confessandprofile.TencentLocationHelper;
import com.yuan.yuanisnosay.confessandprofile.TencentLocationModule;
import com.yuan.yuanisnosay.login.Login;
import com.yuan.yuanisnosay.network.Network;
import com.yuan.yuanisnosay.storage.StorageModel;

/**
 * app入口
 * @author 志彬
 *
 */
public class YuanApplication extends Application{
	
	private static final int M_LOCATION = 0;
	
	private Login mLogin;
	private StorageModel mStorageModel;
	private Network mNetwork;
	
	private TencentLocationManager mLocationManager;
	private TencentLocationHelper mLocationHelper;
	private double mLongitude;
	private double mLatitude;
	private String mRegionName;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mNetwork = Network.getInstance(this);
		mLogin = Login.getInstance(getApplicationContext());
		mStorageModel = StorageModel.getInstance();
		
		//建立app sd卡目录
		StorageModel.createAppFolder();
		
		mStorageModel.read();
		
		//初始化定位变量
		initLocate();
		
		Log.e("mzb", "mLogin.islogin"+mLogin.isLogin());
		
	}
	
	public Login getLogin(){
		return mLogin;
	}
	
	public StorageModel getStorage(){
		return mStorageModel;
	}
	
	public Network getNetwork(){
		return mNetwork;
	}
	
	private void initLocate() {
		TencentLocationRequest tencentLocationReq = TencentLocationRequest
				.create().setRequestLevel(
						TencentLocationRequest.REQUEST_LEVEL_POI);
		mLocationHelper = new TencentLocationHelper(mHandler);
		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationManager.requestLocationUpdates(tencentLocationReq, mLocationHelper);
	}
	
	public double getLongitude() {
		return mLongitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public String getRegionName() {
		return mRegionName;
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case M_LOCATION:
				TencentLocationModule mLocationModule = (TencentLocationModule)msg.obj;
				mLongitude = mLocationModule.getLongitude();
				mLatitude = mLocationModule.getLatitude();
				mRegionName = mLocationModule.getRegionName();
				mLocationManager.removeUpdates(mLocationHelper);
				break;
			}
		}
	};
}
