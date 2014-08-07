package com.yuan.yuanisnosay.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ServerAccess {

	public static final String KEY_STATUS = "status";

	public static final int getStatus(JSONObject json) throws JSONException {
		return json.getInt(KEY_STATUS);
	}

	public interface ServerResponseHandler {
		public void onSuccess(JSONObject result);

		public void onFailure(Throwable error);
	}
/*
	public interface ServerLongResponseHandler {
		public void onSuccess(JSONObject result);

		public void onFailure(Throwable error);

		public void onStart();

		public void onProgress(int percent);

	}*/

	private static final long MAX_ID = ~(long) (1 << 63);
	private static final int TIME_OUT = 10 * 1000;
	public final static String HOST = "http://yswy.r4c00n.com/";/*
	private static final String CHARSET = "utf-8";
	
	private static final String PREFIX = "--", LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data";
	

	private static class PostAsyncTask extends
			AsyncTask<ServerResponseHandler, Integer, JSONObject> {
		private static String packMutipartData(String boundary, String key,
				String value) {
			StringBuffer sb = new StringBuffer();

			sb.append(PREFIX);
			sb.append(boundary);
			sb.append(LINE_END);
			sb.append("Content-Disposition: form-data; name=\"" + key + "\""
					+ LINE_END);
			sb.append(LINE_END);
			sb.append(value);
			sb.append(LINE_END);

			return sb.toString();
		}
		private static String uploadFile(String uri, String params, String fileParam, String filePath) throws Throwable
	    {
	        int responseCode = 0;
	        String result = null;
	        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	        File file = new File(filePath);
	        Log.e("hehe", "wen jian ok");
	        URL url = new URL(HOST+uri);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        Log.e("hehe", "lianjie ok");
	        
	        conn.setReadTimeout(TIME_OUT);
	        conn.setConnectTimeout(TIME_OUT);
	        conn.setDoInput(true); // 允许输入流
	        conn.setDoOutput(true); // 允许输出流
	        conn.setUseCaches(false); // 不允许使用缓存
	        conn.setRequestMethod("POST"); // 请求方式
	        conn.setRequestProperty("Charset", CHARSET); // 设置编码
	        conn.setRequestProperty("connection", "keep-alive");
	        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);

//	        if (null != file) 
	        {
	            /**
	             * 当文件不为空时执行上传
	             
	            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
	            StringBuffer sb = new StringBuffer();
	            /*
	            sb.append(PREFIX);
	            sb.append(BOUNDARY);
	            sb.append(LINE_END);
	            sb.append("Content-Disposition: form-data; name=\"access_token\""+LINE_END);
	            sb.append("Content-Type: text/plain; charset=utf-8"+LINE_END);
	            sb.append(LINE_END);
	            sb.append(params);
	            sb.append(LINE_END);
	            sb.append(PREFIX);
	            sb.append(BOUNDARY);
	            sb.append(PREFIX+LINE_END);
	            
	            String[] paramsArray = params.split("&");
	            for (int i = 0; i < paramsArray.length; i++) 
	            {
	                int idxOfEqu = paramsArray[i].indexOf("=");
		            sb.append(packMutipartData(BOUNDARY, paramsArray[i].substring(0, idxOfEqu), paramsArray[i].substring(idxOfEqu+1)));
		            System.out.println(paramsArray[i].substring(0, idxOfEqu)+paramsArray[i].substring(idxOfEqu+1));
	            }
	            
	            sb.append(PREFIX);
	            sb.append(BOUNDARY);
	            sb.append(LINE_END);
	            /**
	             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
	             * filename是文件的名字，包含后缀名
	             
	            sb.append("Content-Disposition: form-data; name=\""+fileParam+"\"; filename=\""
	                + file.getName() + "\"" + LINE_END);
	            sb.append("Content-Type: application/octet-stream; charset="
	                + CHARSET + LINE_END);
	            sb.append(LINE_END);

	            System.out.println(sb);
	            dos.write(sb.toString().getBytes());
	            
	            InputStream is = new FileInputStream(file);
	            byte[] bytes = new byte[1024];
	            int len = 0;
	            
	            while ((len = is.read(bytes)) != -1) 
	            {
	                dos.write(bytes, 0, len);
	            }
	            is.close();
	            
	            dos.write(LINE_END.getBytes());
	            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
	                .getBytes();
	            dos.write(end_data);
	            Log.e("hehe", "flush");
	            dos.flush();
	            /**
	             * 获取响应码 200=成功 当响应成功，获取响应的流
	             
	            responseCode = conn.getResponseCode();

            	Log.e("hehe", responseCode+"http");
	            //System.out.println(responseCode);
	            //System.out.println(TAG+":\n\tresponse code:" + res);
	            if (200 == responseCode) 
	            {
	                //System.out.println(TAG+"\n\trequest success");
	                InputStream input = conn.getInputStream();
	                StringBuffer sb1 = new StringBuffer();
	                int ss;
	                
	                while ((ss = input.read()) != -1) 
	                {
	                    sb1.append((char) ss);
	                }
	                result = sb1.toString();
	                //System.out.println(TAG+":\n\tresult : " + result);
	            }
	        }
//		else
//		{
//		    throw new IOException();
//		}
	        
	        return result;
	    }
		private static JSONObject sendPacket(String uri, String params,
				String uploadName, String filePath) throws Throwable {
			return new JSONObject(uploadFile(uri, params, uploadName, filePath));
			/*int responseCode = 0;
			String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成

			URL url = new URL(HOST + uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			StringBuffer sb = new StringBuffer();

			conn.setReadTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			String[] paramsArray = params.split("&");
			for (int i = 0; i < paramsArray.length; i++) {
				int idxOfEqu = paramsArray[i].indexOf("=");
				sb.append(packMutipartData(BOUNDARY,
						paramsArray[i].substring(0, idxOfEqu),
						paramsArray[i].substring(idxOfEqu + 1)));
			}

			File file = new File(filePath);
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINE_END);
			sb.append("Content-Disposition: form-data; name=\"" + uploadName
					+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
			sb.append("Content-Type: application/octet-stream; charset="
					+ CHARSET + LINE_END);
			sb.append(LINE_END);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.write(sb.toString().getBytes());

			InputStream is = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;

			while ((len = is.read(bytes)) != -1) {
				dos.write(bytes, 0, len);
			}
			is.close();

			dos.write((LINE_END + PREFIX + BOUNDARY + PREFIX + LINE_END)
					.getBytes());
			dos.flush();

			responseCode = conn.getResponseCode();
			if (200 == responseCode) {
				is = conn.getInputStream();
				sb = new StringBuffer();
				int ss;

				while ((ss = is.read()) != -1) {
					sb.append((char) ss);
				}
				is.close();
				return new JSONObject(new String(sb));
			} else {
				throw (new IOException("EROR:HTTP connect fail!"));
			}
		}

		String uri, params, fileParams, filePath;
		ServerResponseHandler handler;
		Throwable error;

		private PostAsyncTask(String uri, String params, String fileParams,
				String filePath) {
			super();
			this.uri = uri;
			this.params = params;
			this.fileParams = fileParams;
			this.filePath = filePath;
		}

		@Override
		protected JSONObject doInBackground(ServerResponseHandler... handler) {
			this.handler = handler[0];
			try {
				publishProgress(100);
				return sendPacket(uri, params, fileParams, filePath);
			} catch (Throwable error) {
				this.error = error;
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			if (null != handler) {
				if (null == error) {
					handler.onSuccess(result);
				} else {
					handler.onFailure(error);
				}
			}
		}

		@Override
		protected void onPreExecute() {
			if (null != handler) {
				// handler.onStart();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... vlaue) {
			if (null != handler) {
				// handler.onProgress(value);
			}
		}

	}

	private static void doPost(String uri, String params, String fileParams,
			String filePath, ServerResponseHandler handler) {
		PostAsyncTask send = new PostAsyncTask(uri, params, fileParams,
				filePath);
		send.execute(handler);
	}*/

	private static void doPost(String uri, RequestParams params,
			final ServerResponseHandler handler) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(TIME_OUT);
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

		//doPost("recv_user_info", "user_openid=" + openid + "&user_nickname="
		// + nickName + "&user_sex=" + sex, "user_head", picPath, handler);
		doPost("recv_user_info", params, handler);
	}

	public static void getNewcommentnum(String openID,
			ServerResponseHandler handler) throws IOException {

		RequestParams params = new RequestParams();
		params.put("user_openid", openID);
		doPost("get_unread_comment_cnt", params, handler);

	}

	public static void getNewcommentlist(String openID,
			ServerResponseHandler handler) throws IOException {

		RequestParams params = new RequestParams();
		params.put("user_openid", openID);
		doPost("get_unread_comment_list", params, handler);

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
			double latitude, long baseID, int len, int distance,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_location", addr);
		params.put("user_longitude", longitude);
		params.put("user_latitude", latitude);
		params.put("base_id", baseID-1);
		params.put("length", len);
		params.put("neibor", 1);
		params.put("distance", distance);

		doPost("read_express_message", params, handler);
	}

	public static void getNewConfessListNearby(String addr, double longitude,
			double latitude, int len, int distance,
			ServerResponseHandler handler) {
		getMoreConfessListNearby(addr, longitude, latitude, ~(long) (1 << 63),
				len, distance, handler);
	}

	public static void getMoreConfessListHot(long baseID, int len,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_location", "123");
		params.put("user_longitude", 0);
		params.put("user_latitude", 0);
		params.put("base_id", baseID-1);
		params.put("length", len);
		params.put("neibor", 0);
		params.put("distance", 0);

		doPost("read_express_message", params, handler);
	}

	public static void getConfessById(long postID, ServerResponseHandler handler) {
		getMoreConfessListHot(postID+1, 1, handler);
	}

	public static void getNewConfessListHot(int len,
			ServerResponseHandler handler) {
		getMoreConfessListHot(MAX_ID, len, handler);
	}

	public static void postNewConfess(String openid, String confessMsg,
			String addr, double longitude, double latitude, String picPath,
			ServerResponseHandler handler) throws FileNotFoundException {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);
		params.put("express_msg", confessMsg);
		params.put("express_location", addr);
		params.put("express_longitude", longitude);
		params.put("express_latitude", latitude);
		if ("" != picPath) {
		
			 /*doPost("post_express_message", "user_openid=" + openid +
			 "&express_msg=" + confessMsg + "&express_location=" + addr +
			 "&express_longitude=" + longitude + "&express_latitude=" +
			 latitude, "express_picture", picPath, handler); return;*/
			 
			params.put("express_picture", new File(picPath));
		}

		doPost("post_express_message", params, handler);
	}

	public static void postNewConfess(String openid, String confessMsg,
			String addr, double longitude, double latitude,
			ServerResponseHandler handler) {
		try {
			postNewConfess(openid, confessMsg, addr, longitude, latitude, "",
					handler);
		} catch (FileNotFoundException e) {
		}
	}

	public static void flower(long postID, ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("express_id", postID);

		doPost("add_like", params, handler);
	}

	public static void getCommentList(long postID, ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("express_id", postID);

		doPost("read_comment", params, handler);
	}

	public static void postNewComment(String openid, long postID, String text,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("openid", openid);
		params.put("express_id", postID);
		params.put("reply_msg", text);
		params.put("user_openid", openid);
		params.put("express_id", postID);
		params.put("reply_msg", text);

		doPost("post_comment", params, handler);
	}

	public static void delMyPost(String openid, long postID,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);
		params.put("express_id", postID);

		doPost("delete_express_message", params, handler);
	}

	public static void getNewCommentList(String openid,
			ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);

		doPost("get_unread_comment_list", params, handler);
	}

	public static void getMoreConfessHistoryList(String openid, long baseID,
			int len, ServerResponseHandler handler) {
		RequestParams params = new RequestParams();

		params.put("user_openid", openid);
		params.put("base_id", baseID-1);
		params.put("length", len);

		doPost("express_history", params, handler);
	}

	public static void getNewConfessHistoryList(String openid, int len,
			ServerResponseHandler handler) {
		getMoreConfessHistoryList(openid, MAX_ID, len, handler);
	}
}
