package com.yuan.yuanisnosay;

import com.yuan.yuanisnosay.confessandprofile.PersonalProfileActivity;
import com.yuan.yuanisnosay.confessandprofile.WantToConfessActivity;
import com.yuan.yuanisnosay.network.Network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentUtil {
	public static void doIntent(Context fromAct,Class<?> toAct, int ProToActConst) {
		
		YuanApplication app = (YuanApplication) fromAct.getApplicationContext();
		Network network = app.getNetwork();
		if (!network.isOnline()) {
			com.yuan.yuanisnosay.ui.Util.showToast(fromAct,
					fromAct.getString(R.string.network_offline));
			return;
		}
		
		if (app.getLogin().getRegisterStatus() == Status.Login.M_FIRST_LOGIN) {// 个人资料设置成功后需要跳转到相应的界面
			Intent intent = new Intent(fromAct, PersonalProfileActivity.class);
			Bundle data = new Bundle();
			data.putInt("to", Const.PROFILE_TO_WANTTO_CONFESS);
			intent.putExtras(data);
			fromAct.startActivity(intent);
			return;
		}
		
		Intent intent = new Intent(fromAct,toAct);
		fromAct.startActivity(intent);
		
	}
}
