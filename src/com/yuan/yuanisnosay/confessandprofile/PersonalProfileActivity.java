package com.yuan.yuanisnosay.confessandprofile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.yuan.yuanisnosay.CommentActivity;
import com.yuan.yuanisnosay.Const;
import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.Status;
import com.yuan.yuanisnosay.UserActivity;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;
import com.yuan.yuanisnosay.ui.Util;
import com.yuan.yuanisnosay.ui.adpater.ConfessItem;

import de.hdodenhof.circleimageview.CircleImageView;

import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class PersonalProfileActivity extends Activity {

	private static final String TAG = "PersonalProfileActivity";
	
	class ViewHolder {
		private ImageView imgBack;
		private TextView txtSend;
		private EditText editNickName;
		private GridView gridThumbnailShower;
		private RadioGroup rgGender;
	}

	private ViewHolder mHolder;
	private PicThumAdapter mthumbnailAdapter;
	private YuanApplication mApp;
	
	private String gender = "1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_profile);

		initView();
		mApp = (YuanApplication) getApplication();
	}

	private void initView() {
		mHolder = new ViewHolder();
		mHolder.imgBack = (ImageView) findViewById(R.id.imageView_back);
		mHolder.txtSend = (TextView) findViewById(R.id.button_enter);
		mHolder.gridThumbnailShower = (GridView) findViewById(R.id.img_show_thumbnail);
		mHolder.gridThumbnailShower.setSelector(new ColorDrawable(
				Color.TRANSPARENT));
		mHolder.editNickName = (EditText) findViewById(R.id.edit_nickname);
		mHolder.rgGender = (RadioGroup)findViewById(R.id.radioGroup_choose_sex);
		
		mthumbnailAdapter = new PicThumAdapter(this, 0);
		mthumbnailAdapter.update();
		mHolder.gridThumbnailShower.setAdapter(mthumbnailAdapter);
		mHolder.gridThumbnailShower
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == Bimp.bmp.size()) {
							Intent intent = new Intent(
									PersonalProfileActivity.this,
									ImageBucketActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(
									PersonalProfileActivity.this,
									PhotoActivity.class);
							intent.putExtra("ID", arg2);
							startActivity(intent);
						}
					}
				});

		mHolder.txtSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String text = mHolder.editNickName.getText().toString();
				final String openId = mApp.getLogin().getOpenId();
				final RadioButton rb = (RadioButton)findViewById(mHolder.rgGender.getCheckedRadioButtonId());
//				final String gender = rb.getText().toString().trim().equals(R.string.gender_male)?"1":"0";
//				Log.e("personalprofileActivity", openId);
				Util.showToast(PersonalProfileActivity.this, gender);

				try {
					if (Bimp.drr.size() != 0) {
						ServerAccess.updateUserInfo(openId, text, gender,
								Bimp.drr.get(0),
								new SetProfileResponseHandler());
					} else {
						AssetManager aManager = getApplication().getAssets();
						InputStream is = aManager.open("user2_03.png");
						ServerAccess.updateUserInfo(openId, text, gender, is,
								new SetProfileResponseHandler());
					}
				} catch (FileNotFoundException e) {
					Util.showToast(PersonalProfileActivity.this, "找不到所选择的图片文件");
					Util.dismissDialog();
					e.printStackTrace();
				} catch (IOException e) {
					Util.showToast(PersonalProfileActivity.this, "上传图片失败了！");
					Util.dismissDialog();
					e.printStackTrace();
				}
				Util.showProgressDialog(PersonalProfileActivity.this, "请稍后",
						"正在上传资料...");
				// finish();
			}
		});

		mHolder.editNickName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				int strLen = s.toString().trim().length();
				if (strLen == 0) {
					mHolder.txtSend.setEnabled(false);
					return;
				}
				mHolder.txtSend.setEnabled(true);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

		});

		mHolder.imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showChangeSaveAlert();
			}

		});
		
		mHolder.rgGender.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				gender = rb.getText().toString().trim().equals(R.string.gender_male)?"1":"0";
				gender = checkedId == R.id.radioButton_male ? "1":"0";
			}
			
		});
	}

	private class SetProfileResponseHandler implements ServerResponseHandler {

		@Override
		public void onSuccess(JSONObject result) {
			if (result == null) {
				return;
			} else {
				try {
					int status = result.getInt("status");
					switch (status) {
					case Status.SetInfo.INFO_SEND_SUCCESS:
						Util.dismissDialog();
						Util.showToast(PersonalProfileActivity.this, "资料设置成功！");
						Bimp.bmp.clear();
						Bimp.drr.clear();
						Bimp.max = 0;
						finish();
						
						Intent intent = getIntent();
						PersonalProfileActivity.this.setResult(Const.REQUEST_CODE_CONFESS_PROFILE, intent);
						Bundle data =intent.getExtras();
						if( data != null){
							int to = data.getInt("to");
							Intent intentTo;
							switch(to){
							case Const.PROFILE_TO_WANTTO_CONFESS:
								intentTo = new Intent(PersonalProfileActivity.this,WantToConfessActivity.class);
								PersonalProfileActivity.this.startActivity(intentTo);
								
								break;
							case Const.PROFILE_TO_PERSONAL_USER:
								intentTo = new Intent(PersonalProfileActivity.this,UserActivity.class);
								PersonalProfileActivity.this.startActivity(intentTo);
								
								break;
							case Const.PROFILE_TO_COMMENT:
								ConfessItem postComment = (ConfessItem)data.getSerializable(CommentActivity.POST_CONFESS);
								intentTo = new Intent(PersonalProfileActivity.this,CommentActivity.class);
//								Bundle data = new Bundle();
								
//								data.putSerializable(CommentActivity.POST_CONFESS, postComment);
								intentTo.putExtra(CommentActivity.POST_CONFESS, postComment);
								PersonalProfileActivity.this.startActivity(intentTo);
								
								break;
							}
						}
						
						PersonalProfileActivity.this.finish();
						mApp.getLogin().setRegisterStatus(Status.Login.M_REGISTER_SUCCESS);
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onFailure(Throwable error) {
			Util.dismissDialog();
			Util.showToast(PersonalProfileActivity.this, PersonalProfileActivity.
					this.getString(R.string.server_fail));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_profile, menu);
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

	@Override
	protected void onRestart() {
		mthumbnailAdapter.update();
		super.onRestart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showChangeSaveAlert();
		}
		return false;
	}

	private void showChangeSaveAlert() {
		if ((Bimp.bmp.size() == 0 || Bimp.drr.size() == 0)
				&& (mHolder.editNickName.getText() == null || mHolder.editNickName
						.getText().toString().trim().length() == 0)) {

			final AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setTitle("设置个人资料").setMessage("不设置个人资料，就无法表白哦，真的确定不表白了？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}

					});
			builder.setNegativeButton("取消", null).create().show();
			finish();
			return;
		}

		final AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("设置个人资料").setMessage("确定放弃此次编辑？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				finish();
			}

		});
		builder.setNegativeButton("取消", null).create().show();
	}
}
