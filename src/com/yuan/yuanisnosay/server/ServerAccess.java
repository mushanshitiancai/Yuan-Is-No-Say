package com.yuan.yuanisnosay.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ServerAccess {

	public interface ServerResponseHandler {
		public void onSuccess(JSONObject result);
		public void onFailure(Throwable error);
	}

	public final static String HOST = "http://yswy.r4c00n.com/";

	private static void doPost(String uri, RequestParams params,
			final ServerResponseHandler handler) {
		AsyncHttpClient client = new AsyncHttpClient();

		client.post(HOST + uri, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode,
					org.apache.http.Header[] headers, byte[] responseBody) {
				if (null != handler)
					try {
						handler.onSuccess(new JSONObject(new String(
								responseBody)));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

			public void onFailure(int statusCode,
					org.apache.http.Header[] headers, byte[] responseBody,
					Throwable error) {
				if (null != handler)
					handler.onFailure(error);
			}
		});
	}

	public static void registerNewUser(String accessToken, String openid,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_token", accessToken);
		params.put("user_openid", openid);

		doPost("login", params, handler);
	}

	public static void getUserInfo(String openid, ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);

		doPost("download_user_info", params, handler);
	}

	public static void updateUserInfo(String openid, String nickName,
			String sex, String picPath, ServerResponseHandler handler)
			throws java.io.FileNotFoundException {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);
		params.put("user_nickname", nickName);
		params.put("user_sex", sex);
		params.put("user_head", new File(picPath));

		doPost("recv_user_info", params, handler);
	}

	public static void updateUserInfo(String openid, String nickName,
			String sex, InputStream streamDefaultPic,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);
		params.put("user_nickname", nickName);
		params.put("user_sex", sex);
		params.put("user_head", streamDefaultPic);

		doPost("recv_user_info", params, handler);
	}

	public static void getMoreConfessListNearby(String addr, double longitude,
			double latiude, long baseID, int len, int distance, ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_location", addr);
		params.put("user_longitude", longitude);
		params.put("user_latiude", latiude);
		params.put("base_id", baseID);
		params.put("length", len);
        params.put("neibor", 1);
        params.put("distance", distance);

		doPost("read_express_message", params, handler);
	}

    public static void getNewConfessListNearby(String addr, double longitude, double latiude, int len, int distance, ServerResponseHandler handler) {
        getMoreConfessListNearby(addr, longitude, latiude, ~(long)(1<<63), len, distance, handler);
    }

    public static void getMoreConfessListHot(long baseID, int len, ServerResponseHandler handler) {
        RequestParams params = new RequestParams();

        params.put("user_location", "123");
        params.put("user_longitude", 0);
        params.put("user_latiude", 0);
        params.put("base_id", baseID);
        params.put("length", len);
        params.put("neibor", 0);
        params.put("distance", 0);

        doPost("read_express_message", params, handler);
    }

    public static void getConfessById(long postID, ServerResponseHandler handler) {
        getMoreConfessListHeat(postID, 1, handler);
    }

    public static void getNewConfessListHeat(int len, ServerResponseHandler handler) {
        getMoreConfessListHot(~(long)(1<<63), len, handler);
    } 

	public static void postNewConfess(String openid, String confessMsg,
			String addr, double longitude, double latiude, String picPath,
			ServerResponseHandler handler) throws FileNotFoundException {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);
		params.put("express_msg", confessMsg);
		params.put("express_location", addr);
		params.put("express_longitude", longitude);
		params.put("express_latiude", latiude);
		if ("" != picPath)
			params.put("express_picture", new File(picPath));

		doPost("post_express_message", params, handler);
	}

	public static void postNewConfess(String openid, String confessMsg,
			String addr, double longitude, double latiude,
			ServerResponseHandler handler) {
		try {
			postNewConfess(openid, confessMsg, addr, longitude, latiude, "",
					handler);
		} catch (FileNotFoundException e) {
		}
	}

    public static void flower(long postID, ServerResponseHandler handler) {
        RequestParams params = new RequestParams();

        params.put("express_id", postID);

        doPost("add_like", params, handler);
    }

    public static void getCommentList(long postID ,ServerResponseHandler handler) {
        RequestParams params = new RequestParams();

        params.put("express_id", postID);

        doPost("read_comment", params, handler);
    }

    public static void postNewComment(String openid, long postID, String text, ServerResponseHandler handler) {
        RequestParams params = new RequestParams();

        params.put("openid", openid);
        params.put("express_id", postID);
        params.put("reply_msg", text);

        doPost("post_comment", params, handler);
    }

    public static void delMyPost(String openid, long postID, ServerResponseHandler handler) {
        RequestParams params = new RequestParams();

        params.put("user_openid", openid);
        params.put("express_id", postID);

        doPost("delete_express_message", params, handler);
    }

    public static void getNewCommentCount(String openid, ServerResponseHandler hander) {
        RequestParams params = new RequestParams();
        params.put("user_openid", openid);
    }
}
