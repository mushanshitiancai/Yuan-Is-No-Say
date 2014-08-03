package com.yuan.yuanisnosay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yuan.yuanisnosay.ui.ConfessFragment;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "yuan_MainActivity";
	private static final int TAB_NEARBY = R.id.radio_nearby;
	private static final int TAB_HOT = R.id.radio_hot;

	private RadioGroup radioGroupMainTab;
	private RadioButton radioNearby,radioHot;
	private int curTab = TAB_NEARBY;

	private ConfessFragment mFragmentNearby, mFragmentHot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 设置附近热门TAB
		radioGroupMainTab = (RadioGroup) findViewById(R.id.radioGroup_mainTab);
		radioGroupMainTab
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int id) {
						//切换TAB时切换fragment
						curTab=id;
						showFragment(curTab);
					}
				});
		
		View.OnClickListener radioListener=new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(curTab == view.getId())
					refreshFragment(curTab);
			}
		};
		radioNearby = (RadioButton)findViewById(R.id.radio_nearby);
		radioHot = (RadioButton)findViewById(R.id.radio_hot);
		radioNearby.setOnClickListener(radioListener);
		radioHot.setOnClickListener(radioListener);

		// 获取intent参数,判断Action
		Intent intent = getIntent();

		// 创建fragment
		mFragmentNearby = new ConfessFragment(ConfessFragment.TYPE_NEARBY);
//		Bundle nearbyBundle=new Bundle();
//		nearbyBundle.putBoolean(ConfessFragment.ARGUMENT_VISIABLE, true);
//		mFragmentNearby.setArguments(nearbyBundle);
//		getSupportFragmentManager().beginTransaction()
//				.add(R.id.frameLayout_nearby, mFragmentNearby).commit();
		
		mFragmentHot = new ConfessFragment(ConfessFragment.TYPE_HOT);
//		Bundle hotBundle=new Bundle();
//		hotBundle.putBoolean(ConfessFragment.ARGUMENT_VISIABLE, false);
//		mFragmentHot.setArguments(hotBundle);
//		getSupportFragmentManager().beginTransaction()
//				.add(R.id.frameLayout_hot, mFragmentHot).commit();
		
//		getSupportFragmentManager().beginTransaction()
//				.add(R.id.frameLayout_nearby, mFragmentNearby)
//				.add(R.id.frameLayout_hot, mFragmentHot).commit();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		showFragment(curTab);
	}
	
	private void showFragment(int tab){
		if(tab==TAB_NEARBY){
//			mFragmentNearby.getView().setVisibility(View.VISIBLE);
			//mFragmentNearby.getView().setEnabled(true);
//			mFragmentHot.getView().setVisibility(View.GONE);
			//mFragmentHot.getView().setEnabled(false);
			
			getSupportFragmentManager().beginTransaction()
			.remove(mFragmentHot)
			.add(R.id.frameLayout_nearby, mFragmentNearby).commit();
		}else{
//			mFragmentNearby.getView().setVisibility(View.GONE);
			//mFragmentNearby.getView().setEnabled(false);
//			mFragmentHot.getView().setVisibility(View.VISIBLE);
			//mFragmentHot.getView().setEnabled(true);
			
			getSupportFragmentManager().beginTransaction()
			.remove(mFragmentNearby)
			.add(R.id.frameLayout_hot, mFragmentHot).commit();
		}
	}
	
	private void refreshFragment(int tab){
//		if(tab==TAB_NEARBY){
//			mFragmentNearby.refesh();
//		}else{
//			mFragmentHot.refesh();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, ListTestActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
