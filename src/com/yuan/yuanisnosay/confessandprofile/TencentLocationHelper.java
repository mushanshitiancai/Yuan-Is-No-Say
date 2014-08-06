package com.yuan.yuanisnosay.confessandprofile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;

public class TencentLocationHelper implements TencentLocationListener{

	private static final int M_LOCATION = 0;
	
	private TencentLocationModule mLocationModule;
	protected TencentLocationManager mLocationManager;
	private boolean mLocated;
	
	private Handler mHandler;
	
	public TencentLocationHelper(Handler mHandler){
		this.mHandler = mHandler;
		mLocationModule = new TencentLocationModule();
	}
	
	@Override
	public void onLocationChanged(TencentLocation location, int error,
			String reason) {
		if (error == TencentLocation.ERROR_OK) {
			mLocated = true;
			setTencentLocationModule(location);
			Message msg = new Message();
			msg.obj = mLocationModule;
			msg.what = M_LOCATION;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		
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
	
	public boolean getLocated(){
		return mLocated;
	}
}
