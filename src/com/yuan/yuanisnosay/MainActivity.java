package com.yuan.yuanisnosay;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yuan.yuanisnosay.ui.ConfessFragment;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "yuan_MainActivity";
	private static final int TAB_NEARBY = R.id.radio_nearby;
	private static final int TAB_HOT = R.id.radio_hot;

	private YuanApplication mApp;
	private RadioGroup radioGroupMainTab;
	private RadioButton radioNearby, radioHot;
	private int curTab = TAB_NEARBY;
	private boolean radioButtonChange = false;
	private Button btnUser;
	private View btnDistance;
	private PopupWindow mPopupWindowDistance;

	private ConfessFragment mFragmentNearby, mFragmentHot;

	// viewpager
	ViewPager vpMain;
	
	//test
	boolean mIsWantToDelete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mApp = (YuanApplication) getApplication();
		
		// 设置附近热门TAB
		radioGroupMainTab = (RadioGroup) findViewById(R.id.radioGroup_mainTab);
		radioGroupMainTab
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int id) {
						radioButtonChange = true;
						// 切换TAB时切换fragment
						showFragment(id);
					}
				});

		View.OnClickListener radioListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Log.e(TAG, "RadioButton onClick");
				if (radioButtonChange) {
					radioButtonChange = false;
				} else {
					refreshFragment(curTab);
				}
				// if (curTab == view.getId())
				// refreshFragment(curTab);
			}
		};
		radioNearby = (RadioButton) findViewById(R.id.radio_nearby);
		radioHot = (RadioButton) findViewById(R.id.radio_hot);
		radioNearby.setOnClickListener(radioListener);
		radioHot.setOnClickListener(radioListener);

		// 获取intent参数,判断Action
		Intent intent = getIntent();

		// 创建fragment
		mFragmentNearby = new ConfessFragment(ConfessFragment.TYPE_NEARBY);
		mFragmentHot = new ConfessFragment(ConfessFragment.TYPE_HOT);

		// 配置Viewpager
		vpMain = (ViewPager) findViewById(R.id.viewPager_main);
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		List<String> titleList = new ArrayList<String>();
		fragmentList.add(mFragmentNearby);
		fragmentList.add(mFragmentHot);
		titleList.add("1");
		titleList.add("2");
		PagerAdapter pagerAdapter = new myPagerAdapter(
				getSupportFragmentManager(), fragmentList, titleList);
		vpMain.setAdapter(pagerAdapter);
		vpMain.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int item) {
				Log.e(TAG, "onPageSelected:" + item);
				setCurTab(item);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// Log.e(TAG, "onPageScrolled:"+arg0+" "+arg1+" "+arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// Log.e(TAG, "onPageScrollStateChanged:"+arg0);
			}
		});

		// vpMain.setCurrentItem(1);
		btnUser = (Button)findViewById(R.id.button_mainActivity_user);
		btnUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View button) {
				Intent intent=new Intent(MainActivity.this,UserActivity.class);
				startActivity(intent);
			}
		});
		
		initDistanceButton();
	}
	
	private void initDistanceButton(){
		btnDistance = findViewById(R.id.relativeLayout_distance);
		final int height=btnDistance.getLayoutParams().height*2;
        int width=btnDistance.getLayoutParams().width;
        View popupView = this.getLayoutInflater().inflate(R.layout.popup_distance, null);
        
        View btn=popupView.findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mPopupWindowDistance.dismiss();
			}
		});

        mPopupWindowDistance =new PopupWindow(popupView,width,height);
        mPopupWindowDistance.setFocusable(true);
        mPopupWindowDistance.setAnimationStyle(R.style.PopupAnimation);
        
        btnDistance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int[] location = new int[2];
				view.getLocationInWindow(location);
				
				mPopupWindowDistance.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]-height);
			}
		});
	}

	/**
	 * 主页Viewpager的适配器
	 * 
	 * @author 志彬
	 * 
	 */
	class myPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragmentList;
		private List<String> titleList;

		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
				List<String> titleList) {
			super(fm);
			this.fragmentList = fragmentList;
			this.titleList = titleList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (titleList.size() > position) ? titleList.get(position) : "";

		}

		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		showFragment(curTab);
	}

	private void setCurTab(int index) {
		if (index == 0 && curTab != R.id.radio_nearby) {
			curTab = R.id.radio_nearby;
			radioNearby.setChecked(true);
			radioHot.setChecked(false);
			radioButtonChange = false;
			btnDistance.setVisibility(View.VISIBLE);
		} else if(index == 1 && curTab != R.id.radio_hot) {
			curTab = R.id.radio_hot;
			radioNearby.setChecked(false);
			radioHot.setChecked(true);
			radioButtonChange = false;
			btnDistance.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示tab对应的页面
	 * 
	 * @param tab
	 *            tab按钮对于的id
	 */
	private void showFragment(int tab) {
		curTab = tab;
		if (tab == TAB_NEARBY) {
			vpMain.setCurrentItem(0, true);
		} else {
			vpMain.setCurrentItem(1, true);
		}
	}

	/**
	 * 刷新tab对应的页面
	 * 
	 * @param tab
	 *            tab按钮对于的id
	 */
	private void refreshFragment(int tab) {
		if (tab == TAB_NEARBY) {
			mFragmentNearby.refesh();
		} else {
			mFragmentHot.refesh();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		mApp.getStorage().save();
		if(mIsWantToDelete){
			mApp.getStorage().delete();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		if(id == R.id.action_delete){
			mIsWantToDelete = true;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
