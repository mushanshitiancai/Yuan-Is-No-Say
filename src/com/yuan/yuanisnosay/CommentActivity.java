package com.yuan.yuanisnosay;

import com.yuan.yuanisnosay.ui.adpater.ConfessItem;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CommentActivity extends ActionBarActivity {
	
	public final String PARENT_CONFESS = "confessItem";
	private TextView mParentConfess;
	private ListView mCommentList;
	private EditText mCommentContent;
	private Button mCommentSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		mParentConfess = (TextView) findViewById(R.id.textView_parentConfess);
		mCommentList = (ListView) findViewById(R.id.listView_commentList);
		mCommentContent = (EditText) findViewById(R.id.editText_commentContent);
		mCommentSend = (Button) findViewById(R.id.button_commentSend);
		setCommentListContent();
	}
	
	private void setCommentListContent() {
		// TODO Auto-generated method stub
		final String[] strs = 
				new String[] {"first", "second", "third", "fourth", "fifth"};
		mCommentList.setAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line, strs));
		/*Intent intent = getIntent();
		ConfessItem confessItem = (ConfessItem) intent.getSerializableExtra(PARENT_CONFESS);
		mParentConfess.setText(confessItem.getContent());*/
		
	}

	//TODO
	public ConfessItem getParentConfess() {
		return null;		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
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
