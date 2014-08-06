/**
 * 
 */
package com.yuan.yuanisnosay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;
import com.yuan.yuanisnosay.ui.Util;
import com.yuan.yuanisnosay.ui.adpater.ItemData_newcomment;
import com.yuan.yuanisnosay.ui.adpater.NewCommentAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * @author Administrator
 *
 */
public class NewCommentActivity extends Activity {
	ListView list_newcomment;
	YuanApplication mApp;
	private static final int PULL_SUCCESS = 0;
	private static final int PULL_FAIL = 1;

	
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newcomment);
		list_newcomment=(ListView) findViewById(R.id.ListView_newcomment);
		setnewcommentlistcontent();
	}
	  
	public void setnewcommentlistcontent(){
		
		ArrayList<ItemData_newcomment> mylist_newcomment = new ArrayList<ItemData_newcomment>();
		try {
			requestdata(mylist_newcomment);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void requestdata(final ArrayList<ItemData_newcomment> mlist) throws IOException{
		mApp = (YuanApplication) getApplication();
		ServerAccess.getNewCommentList("1", new ServerResponseHandler() {
				@Override
				public void onSuccess(JSONObject result) {
					// TODO Auto-generated method stub
					if(result == null) {
						Util.showResultDialog(NewCommentActivity.this, "获取新私语", "result是null");
						return;
					} 
					String msg = "";
					try {
						int status = result.getInt("status");
//							String msg = result.getString("message");
						switch(status){
						case PULL_SUCCESS:
							
//								Util.showToast(NewCommentActivity.this, "获取新私语成功！");
						    JSONArray commentarray = result.getJSONArray("comment_list");
						    Util.showResultDialog(NewCommentActivity.this, "获取新私语成功！"+status+"  "+msg+commentarray.toString(),"获取新私语" );
							int length = commentarray.length();
							ItemData_newcomment Item_comment = new ItemData_newcomment();
							for(int i=0;i<length;i++)
							{
								JSONObject oj = commentarray.getJSONObject(i);
								Item_comment.sethisNickname(oj.getString("user_nickname"));
								Item_comment.setNewComment(oj.getString("reply_msg"));
								Item_comment.setRawConfess(oj.getString("express_msg"));
								Item_comment.setRawConfessid(oj.getInt("express_id"));
								mlist.add(Item_comment);
								Log.e("NewComment", Item_comment.tostring());
							}
							
							NewCommentAdapter adapter=new NewCommentAdapter(NewCommentActivity.this,mlist);
							list_newcomment.setAdapter(adapter);
							break;
						case PULL_FAIL:
							Util.showResultDialog(NewCommentActivity.this, "获取新私语", "获取新私语失败Fail！"+status+"  "+msg);
//								Util.showToast(NewCommentActivity.this, "获取新私语失败");
							break;
						default:
//								Util.showToast(NewCommentActivity.this, "获取新私语失败");
							Util.showResultDialog(NewCommentActivity.this, "获取新私语", "获取新私语失败,不知原因"+status+"  "+msg);
							break;
						
						}
						
					} catch (JSONException e) {
						Util.showResultDialog(NewCommentActivity.this, "获取新私语", "json exception");
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void onFailure(Throwable error) {
					// TODO Auto-generated method stub
//						Util.showToast(NewCommentActivity.this, "获取新消息失败");
					Util.showResultDialog(NewCommentActivity.this, "获取新私语", "连接失败");
				}
			});
			
	}
	@Override
	public void onStart() {
		super.onStart();
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		// TODO Auto-generated method stub

	}

}
