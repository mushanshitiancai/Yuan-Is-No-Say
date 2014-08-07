package com.yuan.yuanisnosay.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;

/**
 * 和登录有关的逻辑,单例
 * 
 * @author 志彬
 * 
 */
public class Login {
	private static final String TAG="yuan_Login";
	private static final String APP_ID = "1101877629";
	private static final String SCOPE = "get_user_info";

	private static final String SP_NAME = "login"; // sharePreference name
	private static final String KEY_OPENID = "open_id";
	private static final String KEY_QQTOKEN = "qq_token";
	private static final String KEY_QQEXPIRES_IN = "qq_expires_in";

	private static Login mInstance = null;

	private Tencent mTencent;
	private UserInfo mUserInfo;

	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

	private int mRegisterStatus;//用户注册时返回的status，详情参考api
	//	private String mOpenId;
//	private String mQQToken;
	private String mNickname;
	private String mIconPath;
	
	private long mExpiresIn; // token剩余有效时间
	private long mExpiresDate; // token失效时间

	
	// private boolean isLogin = false;

	public static Login getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new Login(context);
		}
		return mInstance;
	}

	private Login(Context context) {
		mTencent = Tencent.createInstance(APP_ID, context);
		mPreferences = context.getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * 登录
	 * 
	 * @param activity
	 *            调用登录功能的activity
	 */
	public void login(Activity activity) {
		// 获取sharePreferences，获取记录的openId和qqToken
		String mOpenId = mPreferences.getString(KEY_OPENID, null);
		String mQQToken = mPreferences.getString(KEY_QQTOKEN, null);
		Long mExpiresIn = mPreferences.getLong(KEY_QQEXPIRES_IN, -1);
		
		Log.e(TAG, "读取数据 mExpiresIn="+mExpiresIn);

		// 判断记录是否有效
		if (mOpenId != null && mQQToken != null && mExpiresIn != -1) {
			Log.e(TAG,"记录有效+mOpenId:"+mOpenId+"  mQQToken:"+mQQToken);
			mTencent.setAccessToken(mQQToken, Long.toString((mExpiresIn-System.currentTimeMillis())/1000));
			mTencent.setOpenId(mOpenId);
		}

		mTencent.login(activity, SCOPE, new LoginUiListener(activity));
		//recordInfo();
		
	}

	/**
	 * 登出
	 * 
	 * @param context
	 */
	public void logout(Context context) {
		mEditor = mPreferences.edit();
		mEditor.remove(KEY_OPENID);
		mEditor.remove(KEY_QQTOKEN);
		mEditor.remove(KEY_QQEXPIRES_IN);
		mEditor.commit();

		mTencent.logout(context);
	}

	public boolean isLogin() {
		return mTencent.isSessionValid();
	}
	
	public String getOpenId() {
		return mTencent.getOpenId();
	}

	public String getQQToken() {
		return mTencent.getAccessToken();
	}

	/**
	 * 记录id，token，expires到记录文件
	 */
	public void recordInfo() {
		mEditor = mPreferences.edit();
		
		Log.e(TAG, "TencentgetExpiresIn="+mTencent.getExpiresIn());
		//long expiresDate = System.currentTimeMillis()
		//		+ mTencent.getExpiresIn() ;
		mEditor.putString(KEY_OPENID, mTencent.getOpenId());
		mEditor.putString(KEY_QQTOKEN, mTencent.getAccessToken());
		mEditor.putLong(KEY_QQEXPIRES_IN, mTencent.getExpiresIn());

		//记录数据
		Log.e(TAG, "记录数据xxxxxxxxxx="+System.currentTimeMillis());
		Log.e(TAG, "记录数据 mExpiresIn="+mTencent.getExpiresIn());
		
		mEditor.commit();
	}
	
//	public boolean is
	
//	public void setOpenId(String openId) {
//		this.mOpenId = openId;
//	}
//	
//	public void setQQToken(String qqToken) {
//		this.mQQToken = qqToken;
//	}
	public String getNickname() {
		return mNickname;
	}
	public void setNickname(String nickname) {
		this.mNickname = nickname;
	}
	public String getIconPath() {
		return mIconPath;
	}
	public void setIconPath(String iconPath) {
		this.mIconPath = iconPath;
	}
	public int getRegisterStatus() {
		return mRegisterStatus;
	}
	public void setRegisterStatus(int mRegisterStatus) {
		this.mRegisterStatus = mRegisterStatus;
	}
}
