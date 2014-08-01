package com.yuan.yuanisnosay.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.Tencent;

/**
 * 和登录有关的逻辑,单例
 * 
 * @author 志彬
 * 
 */
public class Login {
	private static final String APP_ID = "1101877629";
	private static final String SCOPE = "get_user_info";

	private static final String SP_NAME = "login"; // sharePreference name
	private static final String KEY_OPENID = "open_id";
	private static final String KEY_QQTOKEN = "qq_token";
	private static final String KEY_QQEXPIRESDATE = "qq_expires_date";

	private static Login mInstance = null;

	private Tencent mTencent;
	private UserInfo mUserInfo;

	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

	// private String mOpenId;
	// private String mQQToken;
	// private long mExpiresIn; // token剩余有效时间
	// private long mExpiresDate; // token失效时间

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
		Long mExpiresDate = mPreferences.getLong(KEY_QQEXPIRESDATE, -1);
		Long mExpiresIn = (mExpiresDate - System.currentTimeMillis()) / 1000;
		
		// 判断记录是否有效
		if (mOpenId != null && mQQToken != null && mExpiresDate != -1) {
			mTencent.setAccessToken(mQQToken, Long.toString(mExpiresDate));
			mTencent.setOpenId(mOpenId);
		}
		mTencent.login(activity, SCOPE, new LoginUiListener(activity));
		recordInfo();
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
		mEditor.remove(KEY_QQEXPIRESDATE);
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
	private void recordInfo() {
		mEditor = mPreferences.edit();

		long mExpiresDate = System.currentTimeMillis()
				+ mTencent.getExpiresIn() * 1000;
		mEditor.putString(KEY_OPENID, mTencent.getOpenId());
		mEditor.putString(KEY_QQTOKEN, mTencent.getAccessToken());
		mEditor.putLong(KEY_QQEXPIRESDATE, mExpiresDate);

		mEditor.commit();
	}

}
