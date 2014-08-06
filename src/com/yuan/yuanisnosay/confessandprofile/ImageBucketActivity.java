package com.yuan.yuanisnosay.confessandprofile;

import java.io.Serializable;
import java.util.List;

import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.ui.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class ImageBucketActivity extends Activity{
	
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	private List<ImageBucket> dataList;
	private ImageView imgBack;
	private GridView gridView;
	private ImageBucketAdapter adapter;// 自定义的适配器
	private AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_bucket);
        helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
//		new Thread(){
//			@Override
//			public void run(){
		Util.showProgressDialog(this, "相册", "正在加载图片...");
				dataList = helper.getImagesBucketList(false);
				Util.dismissDialog();
//			}
//		}.start();
		
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
//		
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		imgBack = (ImageView) findViewById(R.id.imageView_back);
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(ImageBucketActivity.this, dataList);
		gridView.setAdapter(adapter);

		imgBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
				 */
				// adapter.notifyDataSetChanged();
				Intent intent = new Intent(ImageBucketActivity.this,
						ImageGridActivity.class);
				intent.putExtra(ImageBucketActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				startActivity(intent);
				finish();
			}

		});
	}
}
