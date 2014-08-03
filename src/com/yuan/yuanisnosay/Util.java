package com.yuan.yuanisnosay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	
	public static String formatDateTime(long time) {
		if (0 == time)return "";
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return mDateFormat.format(new Date(time));
	}
	
	public static String formatDateTime(Date date) {
		if (date == null)return "";
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return mDateFormat.format(date);
	}
}
