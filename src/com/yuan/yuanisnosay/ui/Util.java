package com.yuan.yuanisnosay.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class Util {

	private static Dialog mProgressDialog;
	private static Toast mToast;

	public static final void showToast(Context context, String text) {
		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
		mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		mToast.show();
	}

	public static final void showResultDialog(Context context, String msg,
			String title) {
		if (msg == null)
			return;
		// String rmsg = msg.replace(",", "\n");
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
				.setNegativeButton("知道了", null).create().show();
	}

	public static final void showProgressDialog(Context context, String title,
			String message) {
		dismissDialog();
		if (TextUtils.isEmpty(title)) {
			title = "请稍候";
		}
		if (TextUtils.isEmpty(message)) {
			message = "正在加载...";
		}
		mProgressDialog = ProgressDialog.show(context, title, message);
	}

	public static final void dismissDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
}
