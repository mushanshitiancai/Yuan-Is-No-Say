package com.yuan.yuanisnosay;

import android.os.Environment;

public class Const {
	public static final String YUAN_FOLDER_NAME = Environment.getExternalStorageDirectory() + "/YuanIsNoSay/";
	public static final String STORAGE_FILENAME = "storage";
	public static final int REQUEST_CODE_CONFESS_PROFILE = 0;
	public static final int GET_COUNT = 10;	//每次获取的表白条数
	
	public static final int PROFILE_TO_WANTTO_CONFESS = 0;
	public static final int PROFILE_TO_PERSONAL_USER = 1;
	public static final int PROFILE_TO_COMMENT = 2;
}
