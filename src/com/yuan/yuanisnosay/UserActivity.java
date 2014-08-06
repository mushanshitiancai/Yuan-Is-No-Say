package com.yuan.yuanisnosay;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuan.yuanisnosay.ui.ConfessFragment;

public class UserActivity extends FragmentActivity {

	private ImageView btnBack;
//	private Button btnMessage;
	private RelativeLayout layoutWantToConfess;
	private ImageView ivIcon;
	private TextView tvName;
	
	private ConfessFragment mMineConfessFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		//绑定按钮
		ButtonLisener buttonLisener=new ButtonLisener();
		btnBack = (ImageView)findViewById(R.id.imageView_user_back);
//		btnMessage = (Button)findViewById(R.id.button_user_message);
		layoutWantToConfess = (RelativeLayout)findViewById(R.id.layout_user_wantToConfess);
		btnBack.setOnClickListener(buttonLisener);
//		btnMessage.setOnClickListener(buttonLisener);
		layoutWantToConfess.setOnClickListener(buttonLisener);
		
		ivIcon = (ImageView)findViewById(R.id.imageView_user_icon);
		tvName = (TextView)findViewById(R.id.textView_user_name);
		
		mMineConfessFragment = new ConfessFragment(ConfessFragment.TYPE_MINE);
		getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_container, mMineConfessFragment).commit();
		
	}
	
	/**
	 * 按钮回调
	 * @author 志彬
	 *
	 */
	class ButtonLisener implements View.OnClickListener{
		@Override
		public void onClick(View button) {
			switch(button.getId()){
			case R.id.imageView_user_back:
				finish();
				break;
//			case R.id.button_user_message:
				
//				break;
			case R.id.layout_user_wantToConfess:
				
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
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
