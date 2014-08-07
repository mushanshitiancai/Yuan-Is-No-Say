package com.yuan.yuanisnosay;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;
import com.yuan.yuanisnosay.ui.ConfessFragment;
import com.yuan.yuanisnosay.ui.adpater.ConfessAdapter;
import com.yuan.yuanisnosay.ui.adpater.ConfessItem;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends ActionBarActivity {
	
	public static final String PARENT_CONFESS = "confessItem";
	public static final String POST_ID = "postID";
	private static TextView mConfessContent;
	private static ImageView mConfessPic;
	private static Button mConfessFlower;
	private static Button mConfessComment;
	
	private static ListView mCommentList;
	private static EditText mNewComment;
	private static Button mCommentSend;
	private static ImageView mCommentBack;
	private int postID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		mConfessContent = (TextView) findViewById(R.id.textView_confessItem_content);
		mConfessPic = (ImageView) findViewById(R.id.imageView_picture);
		mConfessFlower = (Button) findViewById(R.id.button_confessItem_flowers);
		mConfessComment = (Button) findViewById(R.id.button_confessItem_comment);
		mCommentList = (ListView) findViewById(R.id.listView_commentList);
		mNewComment = (EditText) findViewById(R.id.editText_commentContent);
		mCommentSend = (Button) findViewById(R.id.button_commentSend);
		mCommentBack = (ImageView) findViewById(R.id.img_back);
		
		Intent intent = getIntent();
		postID = intent.getIntExtra(POST_ID, 1);
		setParentConfess(postID);
		setCommentList(postID);
		//Log.i("Test", "Test");
		mCommentBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
	}
	
	//TODO
	public void setParentConfess(int postID) {
		ServerAccess.getConfessById(postID, new ServerResponseHandler() {

			@Override
			public void onSuccess(JSONObject result) {
				// TODO Auto-generated method stub
				try {
					if (result.getInt("status") == 0) {
						//TODO 解析JSON，设置表白内容
						Log.d("confessContent", result.getJSONArray("express_list").toString());
						JSONArray confessList = result.getJSONArray("express_list");
						JSONObject confess = (JSONObject) confessList.get(1);
						mConfessContent.setText(confess.getString("express_msg"));
						
						if (0 == confess.getInt("express_picture")) {
							mConfessPic.setVisibility(View.GONE);
						} else {
							mConfessPic.setVisibility(View.VISIBLE);
							//String url = ServerAccess.HOST + "download_user_head?user_openid=" + confess.getString("user_openid");
							
							ConfessItem confessItem = new ConfessItem(confess);
							LinkedList<ConfessItem> confessLL = new LinkedList<ConfessItem>();
							confessLL.add(confessItem);
							//设置adapter
							ConfessAdapter adapter = new ConfessAdapter(getApplicationContext(), ConfessAdapter.TYPE_NORMAL, confessLL);
							ConfessFragment fragment = new ConfessFragment(ConfessFragment.TYPE_MINE);
							fragment.setmAdapter(adapter);
							/*{"status":0,"express_list":
							[{"express_reply_cnt":4,
							"express_latitude":0,"user_openid":"1",
							"express_location":"","express_bad_cnt":0,
							"express_longitude":0,"user_nickname":"",
							"express_time":0,"unread_reply_cnt":4,"express_id":1,
							"express_picture":0,"express_msg":"fdgsdfh",
							"express_like_cnt":0}]}*/
						}
						
					} else {
						Toast.makeText(getApplicationContext(), "木有表白消息。。。", 1000).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "拉取表白失败。。。", 1000).show();
			}
			
		});
		
		return ;		
	}
	
	private void setCommentList(final int postID) {
		final ArrayList<String> strs = new ArrayList<String>();
		//strs.add("first");
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, strs);
		
		ServerAccess.getCommentList(postID, new ServerResponseHandler() {

			@Override
			public void onSuccess(JSONObject result) {
				// TODO Auto-generated method stub
				try {
					JSONArray replyList = result.getJSONArray("reply_list");
					if (0 == result.getInt("status")) {
						//TODO 绑定列表
						for (int i = 0; i < replyList.length(); ++i) {
							JSONObject reply = (JSONObject) replyList.get(i);
							strs.add(reply.getString("user_nickname") + ": " + reply.getString("reply_msg"));
							
						}
						
						mCommentList.setAdapter(adapter);
						/*{"status":0,
							"reply_list":[
								{"read_status":0,"reply_bad_cnt":0,
								"reply_location":"","user_openid":"1",
								"reply_id":1,"reply_like_cnt":0,"reply_timedate":0,
								"express_id":1,"reply_msg":"sgsfgsdf"},
								{"read_status":0,"reply_bad_cnt":0,
								"reply_location":"","user_openid":"1",
								"reply_id":2,"reply_like_cnt":0,
								"reply_timedate":0,"express_id":1,
								"reply_msg":"dfgdsgd"},
								{"read_status":0,"reply_bad_cnt":0,
								"reply_location":"","user_openid":"1",
								"reply_id":3,"reply_like_cnt":0,"reply_timedate":0,
								"express_id":1,"reply_msg":"hfgjdfhgds"}]}*/
						
					} else {
						Toast.makeText(getApplicationContext(), "拉取不到列表。。。", 1000).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "请求失败。。。", 1000).show();
			}
			
		});
		
		mCommentSend.setOnClickListener(new OnClickListener() { 
            @Override 
            public void onClick(View view) { 
                // TODO Auto-generated method stub 
            	final String newComment = mNewComment.getText().toString();
            	if (0 == newComment.length()) {
            		Toast.makeText(getApplicationContext(), "评论不能为空。。。", 1000).show();
            	} else {
            		//TODO 发送评论
            		String openid = ((YuanApplication)getApplication()).getLogin().getOpenId();
            		ServerAccess.postNewComment(openid, postID, newComment, new ServerResponseHandler() {

						@Override
						public void onSuccess(JSONObject result) {
							// TODO Auto-generated method stub
							try {
								int status = result.getInt("status");
								if (0 == status) {
									strs.add(((YuanApplication)getApplication()).getLogin().getNickname() +  ":" + newComment);
									adapter.notifyDataSetChanged();
									mNewComment.setText("");
								} else if (4 == status) {
									Toast.makeText(getApplicationContext(), "其它错误：" + result.getString("hint"), 1000).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(Throwable error) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "发表评论失败", 1000).show();
						}
            		});
            	}
            } 
        });
		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}
