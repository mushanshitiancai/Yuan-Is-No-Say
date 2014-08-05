package com.yuan.yuanisnosay.confessandprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.yuan.yuanisnosay.R;
//import com.yuan.yuanisnosay.confessandprofile.BitmapCache.ImageCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageGridAdapter extends BaseAdapter {
	
	final String TAG = getClass().getSimpleName();
	private final int BITMAP_WIDTH = 300;
	private final int BITMAP_HEIGHT = 300;
	
	Activity act;
	List<ImageItem> dataList;
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	
	private ImageAdapterHelper imageAdapterHelper;
//	BitmapCache cache;
//	ImageCallback callback = new ImageCallback() {
//		@Override
//		public void imageLoad(ImageView imageView, Bitmap bitmap,
//				Object... params) {
//			if (imageView != null && bitmap != null) {
//				String url = (String) params[0];
//				if (url != null && url.equals((String) imageView.getTag())) {
//					((ImageView) imageView).setImageBitmap(bitmap);
//				} else {
//					Log.e(TAG, "callback, bmp not match");
//				}
//			} else {
//				Log.e(TAG, "callback, bmp null");
//			}
//		}
//	};

	public static interface TextCallback {
		public void onListen(int count);
	}


	public ImageGridAdapter(Activity act, List<ImageItem> list) {
		this.act = act;
		dataList = list;
//		cache = new BitmapCache();
		imageAdapterHelper = new ImageAdapterHelper(act,BITMAP_WIDTH,BITMAP_HEIGHT);
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
//		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
//				callback);
		imageAdapterHelper.showImg(item.imagePath, holder.iv);
		if (item.isSelected) {
			holder.selected.setImageResource(R.drawable.icon_data_select);  
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
			holder.selected.setImageResource(-1);
			holder.text.setBackgroundColor(0x00000000);
		}
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bimp.clear();
				String path = dataList.get(position).imagePath;
				
				item.isSelected = !item.isSelected;
				if (item.isSelected) {
					holder.selected
					.setImageResource(R.drawable.icon_data_select);
					holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
					map.put(path, path);

				} else if (!item.isSelected) {
					holder.selected.setImageResource(-1);
					holder.text.setBackgroundColor(0x00000000);
					map.remove(path);
				}
				
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				
				if (Bimp.act_bool) {
//					Intent intent = new Intent(act,
//							BiaoBaiActivity.class);
//					act.startActivity(intent);
					
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 1) {
						Bimp.drr.add(list.get(i));
					}
				}
				act.finish();
			}

		});

		return convertView;
	}
	
}
