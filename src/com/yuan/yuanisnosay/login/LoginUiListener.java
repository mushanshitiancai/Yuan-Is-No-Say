package com.yuan.yuanisnosay.login;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.yuan.yuanisnosay.ui.Util;

/**
 * qq登陆的回调
 * @author 志彬
 *
 */
public class LoginUiListener implements IUiListener{
	private Context mContext;
	private boolean mIsCaneled;
	private static final int ON_COMPLETE = 0;
	private static final int ON_ERROR = 1;
	private static final int ON_CANCEL = 2;
	
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ON_COMPLETE:
                JSONObject response = (JSONObject)msg.obj;
                Util.showResultDialog(mContext, response.toString(), "onComplete");
                Util.dismissDialog();
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
	
	public LoginUiListener(Context mContext) {
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