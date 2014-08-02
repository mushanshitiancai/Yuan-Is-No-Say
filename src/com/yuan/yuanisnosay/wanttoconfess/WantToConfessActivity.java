package com.yuan.yuanisnosay.wanttoconfess;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.yuan.yuanisnosay.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WantToConfessActivity extends Activity implements
		TencentLocationListener {
	
	private static final int LOCATING_MSG_CODE = 0x111;
	/**
	 * 表示正在定位时，显示在“正在定位”后面的.个数
	 */
	private int mLocatingDotCount;//
	
	private DateFormat mSdf;
	private TencentLocationModule mLocationModule;
	protected TencentLocationManager mLocationManager;
	private boolean mStarted;
	private boolean mLocated;
	
	private TextView txtBack;
	private EditText editConfessContentInput;
	private TextView txtLocationShow;
	private Button btnSend;
	
	private GridView gridThumbnailShower;
	private GridAdapter thumbnailAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wantto_confess);

		initLocate();
		initView();
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == LOCATING_MSG_CODE){
				if(!mLocated) {
//					int count = msg.getData().getInt("count");
					StringBuffer sb = new StringBuffer();
					sb.append("正在定位");
					for(int i = 0; i < mLocatingDotCount+1;i++) {
						sb.append(".");
					}
					mLocatingDotCount++;
					txtLocationShow.setText(sb.toString());	
				}
			}
		}
	};
	
	private void initLocate(){
		TencentLocationRequest tencentLocationReq = TencentLocationRequest
				.create().setRequestLevel(
						TencentLocationRequest.REQUEST_LEVEL_POI);
		mLocationManager = TencentLocationManager.getInstance(this);
		mLocationManager.requestLocationUpdates(tencentLocationReq, this);
		
		mStarted = true;
		mLocationModule = new TencentLocationModule();
		
		new Timer().schedule(new TimerTask(){

			@Override
			public void run() {
//				int tmp = 0;
				while(!mLocated) {
//					Message msg = new Message();
//					Bundle data = new Bundle();
					mLocatingDotCount %= 3;
//					data.putInt("count", mLocatingDotCount++);
//					msg.setData(data);
//					msg.what = LOCATING_MSG_CODE;
					mHandler.sendEmptyMessage(LOCATING_MSG_CODE);
				}
			}
			
		},0,2000);
		
	}
	
	private void initView() {
		editConfessContentInput = (EditText) findViewById(R.id.text_edit);
		txtLocationShow = (TextView) findViewById(R.id.view_location);
		
		gridThumbnailShower = (GridView) findViewById(R.id.img_show_thumbnail);
		gridThumbnailShower.setSelector(new ColorDrawable(Color.TRANSPARENT));
		thumbnailAdapter = new GridAdapter(this);
		thumbnailAdapter.update();
		gridThumbnailShower.setAdapter(thumbnailAdapter);
		gridThumbnailShower.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					Intent intent = new Intent(WantToConfessActivity.this,
							ImageBucketActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(WantToConfessActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				final String text = editConfessContentInput.getText().toString();
				final String addr = mLocationModule.getAddr();
				final String jingdu = mLocationModule.getLatitude()+"";
				final String weidu = mLocationModule.getLongitude()+"";
//				ServerAccess sa = new ServerAccess();
				if(Bimp.drr.size() != 0){
					new Thread(){
						public void run(){
//							try {
//								String ss = ServerAccess.newPost(
//										text, addr, jingdu, weidu, Bimp.drr.get(0));
//								Log.e("back", ss);
//								
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
						
					}.start();
					
				}else {
					new Thread(){
						public void run(){
//							try {
//								String ss = ServerAccess.newPost(
//										text, addr, jingdu, weidu);
//								Log.e("back", ss);
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
					}.start();
				}
//				finish();
			}
		});
		
		txtBack = (TextView)findViewById(R.id.back);
		txtBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				showChangeSaveAlert();
			}
			
		});
	}

	@Override
	public void onLocationChanged(TencentLocation location, int error,
			String reason) {
		mSdf = SimpleDateFormat.getTimeInstance();
		if (error == TencentLocation.ERROR_OK) {
			setTencentLocationModule(location);
			txtLocationShow.setText(mLocationModule.getRegionName());
			mLocated = true; 
		} else if (error == TencentLocation.ERROR_NETWORK) {
//			Toast toast = Toast.makeText(this, "网络不可用,请检查网络连接", Toast.LENGTH_SHORT);
//			toast.show();
		}
	}

	public void setTencentLocationModule(TencentLocation location) {
		mLocationModule.setLatitude(location.getLatitude());
		mLocationModule.setLongitude(location.getLongitude());
		mLocationModule.setAccuracy(location.getAccuracy());
		mLocationModule.setNation(location.getNation());
		mLocationModule.setProvince(location.getProvince());
		mLocationModule.setCity(location.getCity());
		mLocationModule.setDistrict(location.getDistrict());
		mLocationModule.setRegionName(location.getPoiList().get(0)
				.getName());
	}

	@Override
	public void onStatusUpdate(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
	}
	
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 1) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		@SuppressLint("HandlerLeak")
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					thumbnailAdapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	@Override
	protected void onRestart() {
		thumbnailAdapter.update();
		super.onRestart();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showChangeSaveAlert();
		}
		return false;
	}
	
	private void showChangeSaveAlert(){
		if((Bimp.bmp.size() == 0 || Bimp.drr.size() == 0) && 
				(editConfessContentInput.getText() == null || 
				editConfessContentInput.getText().toString().trim().length() == 0)) {
			finish();
			return;
		}
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("我要表白").setMessage("确定放弃此次编辑？");
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Bimp.bmp.clear();
						Bimp.drr.clear();
						Bimp.max = 0;
						finish();
					}

				});
		builder.setNegativeButton("取消",null).create().show();
	}
}
