package com.yuan.yuanisnosay;

public class Status {
	
	public static class Login{
		public static final int M_REGISTER_SUCCESS = 0;
		public static final int M_FIRST_LOGIN = 1;
		public static final int M_VERITY_FAIL = 2;
	}
	public static class Confess{
		public static final int CONFESS_SEND_SUCCESS = 0;
		public static final int CONFESS_SEND_FAIL_NOT_LOGIN = 1;
		public static final int CONFESS_SEND_FAIL_OTHER = 2;
	}
	public static class SetInfo{
		public static final int INFO_SEND_SUCCESS = 0;
		public static final int INFO_SEND_FAIL_NOT_LOGIN = 1;
		public static final int INFO_SEND_FAIL_OTHER = 2;
	}
	
}
