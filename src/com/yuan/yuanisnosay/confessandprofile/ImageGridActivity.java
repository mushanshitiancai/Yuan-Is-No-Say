package com.yuan.yuanisnosay.confessandprofile;

import java.util.List;

import com.yuan.yuanisnosay.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	List<ImageItem> dataList;
	ImageView imgBack;
	GridView gridView;
	ImageGridAdapter adapter;
//	AlbumHelper helper;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);

//		helper = AlbumHelper.getHelper();
//		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		imgBack = (ImageView) findViewById(R.id.imageView_back);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList);
		gridView.setAdapter(adapter);

		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Bimp.drr.size() == 0) {
					Intent intent = new Intent(ImageGridActivity.this, ImageBucketActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Bimp.drr.size() == 0) {
				Intent intent = new Intent(ImageGridActivity.this, ImageBucketActivity.class);
				startActivity(intent);
				finish();
			}
		}
		return false;
	}

}
