package com.yuan.yuanisnosay.wanttoconfess;

public class TencentLocationModule {
	
	private String nation;
	private String province;
	private String city;
	private String district;
	private String regionName;
	private double latitude;
	private double longitude;
	private float accuracy;
	
	public TencentLocationModule(){}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("name=").append(getRegionName()).append(",");
		sb.append("nation=").append(getNation()).append(",");
		sb.append("province=").append(getProvince()).append(",");
		sb.append("city=").append(getCity()).append(",");
		sb.append("district=").append(getDistrict()).append(",");
		sb.append("longitude=").append(getLatitude()).append(",");
		sb.append("longitude=").append(getLongitude()).append(",");
		sb.append("accuracy=").append(getAccuracy());
		return sb.toString();
	}
	
	public String getAddr(){
		StringBuffer sbAddr = new StringBuffer();
		sbAddr.append(getNation()+",");
		sbAddr.append(getProvince()+",");
		sbAddr.append(getCity()+",");
		sbAddr.append(getDistrict());
		return sbAddr.toString();
	}
	
	public float getAccuracy(){
		return accuracy;
	}
	
	public void setNation(String nation){
		this.nation = nation;
	}

	public String getNation() {
		return nation;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	
}
