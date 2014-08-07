package com.yuan.yuanisnosay;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yuan.yuanisnosay.confessandprofile.WantToConfessActivity;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.ui.ConfessFragment;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;

public class UserActivity extends FragmentActivity {

	private ImageView btnBack;
//	private Button btnMessage;
	private RelativeLayout layoutWantToConfess;
	private ImageView ivIcon;
	private TextView tvName;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	
	
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
//		String nickName = ((YuanApplication)getApplication()).getLogin().getNickname();
		String openid = ((YuanApplication)getApplication()).getLogin().getOpenId();
		
		//TODO 显示用户头像
		mImageLoader = ImageLoader.getInstance();
		String url = ServerAccess.HOST + "download_user_head?user_openid=" + openid;
		
		mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				// .displayer(new RoundedBitmapDisplayer(20))
				.build();
		mImageLoader.displayImage(url, ivIcon, mOptions, new AnimateFirstDisplayListener());
		
		ServerAccess.getUserInfo(openid, new ServerResponseHandler() {

			@Override
			public void onSuccess(JSONObject result) {
				// TODO Auto-generated method stub
				try {
					if (result ==  null) {
						Toast.makeText(getApplicationContext(), "服务器木有数据。。。", 1000).show();
						return;
					} 
					if (0 == result.getInt("status")) {
						String nickname = result.getString("nick_name");
						int sexvalue = result.getInt("sex");
						String sex = "";
						if (0 == sexvalue) {
							sex = "女";
						} else if (1 == sexvalue) {
							sex = "男";
						}
						if (nickname != null) {
							tvName.setText(nickname + " (" + sex + ")" );
						} else {
							tvName.setText("");
						}
					} else {
						Toast.makeText(getApplicationContext(), "获取个人信息失败。。。", 1000).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mMineConfessFragment = new ConfessFragment(ConfessFragment.TYPE_MINE);
		getSupportFragmentManager().beginTransaction().
			add(R.id.frameLayout_container, mMineConfessFragment).commit();
		
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
				Intent intent = new Intent(UserActivity.this,WantToConfessActivity.class);
				startActivity(intent);
				com.yuan.yuanisnosay.ui.Util.showToast(UserActivity.this,
						((YuanApplication)UserActivity.this.getApplication()).getLogin().getOpenId());
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
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
