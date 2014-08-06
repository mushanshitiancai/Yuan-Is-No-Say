package com.yuan.yuanisnosay.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yuan.yuanisnosay.ListTestActivity;
import com.yuan.yuanisnosay.Status;
import com.yuan.yuanisnosay.confessandprofile.PersonalProfileActivity;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;
import com.yuan.yuanisnosay.ui.Util;

/**
 * qq登陆的回调
 * @author 志彬
 *
 */
public class LoginUiListener implements IUiListener{
	private Tencent mTencent;
	private Activity mContext;
	private boolean mIsCaneled;
	private static final int ON_COMPLETE = 0;
	private static final int ON_ERROR = 1;
	private static final int ON_CANCEL = 2;
	
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ON_COMPLETE:
//                JSONObject response = (JSONObject)msg.obj;
//                Util.showResultDialog(mContext, response.toString(), "onComplete");
//                Util.dismissDialog();
                final YuanApplication app=(YuanApplication) mContext.getApplication();
                app.getLogin().recordInfo();
                
                
                ServerAccess.registerNewUser(app.getLogin().getQQToken(),app.getLogin().getOpenId(), new ServerResponseHandler() {
					
					@Override
					public void onSuccess(JSONObject result) {
						if(result == null) {
							return;
						}
						try {
							int status = result.getInt("status");
							switch(status){
							case Status.Login.M_REGISTER_SUCCESS:
								Util.showToast(mContext, "注册成功！");
								break;
							case Status.Login.M_FIRST_LOGIN:
								Util.showToast(mContext, "首次登陆，注册到系统");
								Intent intent = new Intent(mContext,PersonalProfileActivity.class);
								mContext.startActivity(intent);
								break;
							case Status.Login.M_VERITY_FAIL:
								Util.showToast(mContext, "验证无效！");
								break;
							}
							app.getLogin().setmRegisterStatus(status);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					@Override
					public void onFailure(Throwable error) {
						Util.showToast(mContext, "网络连接有问题，请检查");
					}
				});
                break;
            case ON_ERROR:
                UiError e = (UiError)msg.obj;
                Util.showResultDialog(mContext, "errorMsg:" + e.errorMessage
                        + "errorDetail:" + e.errorDetail, "onError");
                Util.dismissDialog();
                break;
            case ON_CANCEL:
                Util.showToast((Activity)mContext, "登录取消");
                break;
            }
        }	    
	};
	
	public LoginUiListener(Activity mContext) {
		super();
		this.mContext = mContext;
	}
	
	public void cancel() {
		mIsCaneled = true;
	}


	@Override
	public void onComplete(Object response) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
	    msg.what = ON_COMPLETE;
	    msg.obj = response;
	    mHandler.sendMessage(msg);
	}

	@Override
	public void onError(UiError e) {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_ERROR;
        msg.obj = e;
        mHandler.sendMessage(msg);
	}

	@Override
	public void onCancel() {
		if (mIsCaneled) return;
	    Message msg = mHandler.obtainMessage();
        msg.what = ON_CANCEL;
        mHandler.sendMessage(msg);
	}
}