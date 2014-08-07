 package com.yuan.yuanisnosay.ui.adpater;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yuan.yuanisnosay.CommentActivity;
import com.yuan.yuanisnosay.MainActivity;
import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.DateUtil;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;

public class ConfessAdapter extends BaseAdapter {
	public static final int TYPE_NORMAL = 0; // 主界面表白类型
	public static final int TYPE_MINE = 1; // 个人界面表白类型

	private int mType; // adapter类型
	Context mContext;
	LayoutInflater mInflater;
	LinkedList<ConfessItem> mConfessList;
	private int itemId;
	private static Map<String, Boolean> flowerMap = new HashMap<String, Boolean>();
	
	ImageLoader mImageLoader;
	DisplayImageOptions mOptions;
	ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	View.OnClickListener mButonListener = new ButtonListener();

	static class ViewHolder {
		TextView content;
		TextView publishDate; 
		TextView position;
		TextView author;
		Button btnFlower;
		Button btnComment;
		ImageView ivIcon;
		ImageView ivPicture;
		View layoutContent;
		View layoutPicture;
		View layoutInfo;
	}

	public ConfessAdapter(Context context, int type, LinkedList<ConfessItem> confessList) {
		mType = type;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mConfessList = confessList;

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				// .displayer(new RoundedBitmapDisplayer(20))
				.build();
	}

	@Override
	public int getCount() {
		return mConfessList.size();
	}

	@Override
	public Object getItem(int position) {
		return mConfessList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ConfessItem curConfess = mConfessList.get(position);
		ViewHolder viewHolder;
//		itemId = position;
		if (convertView == null) {
			// if(mType == TYPE_NORMAL){
			// convertView=mInflater.inflate(R.layout.item_confess, null);
			// }else{
			// convertView=mInflater.inflate(R.layout.item_my_confess, null);
			// }

			//
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_confess, null);
			viewHolder.ivPicture=(ImageView) convertView.findViewById(R.id.imageView_picture);
//			if (curConfess.getPicture() == null || curConfess.getPicture().equals("")) {
//				convertView = mInflater.inflate(R.layout.item_confess, null);
//			} else {
//				convertView = mInflater.inflate(R.layout.item_confess_with_picture, null);
//				viewHolder.ivPicture=(ImageView) convertView.findViewById(R.id.imageView_picture);
//			}

			
			viewHolder.content = (TextView) convertView.findViewById(R.id.textView_confessItem_content);
			viewHolder.publishDate = (TextView) convertView.findViewById(R.id.textView_confessItem_publishTime);
			viewHolder.position = (TextView) convertView.findViewById(R.id.textView_confessItem_position);
			viewHolder.btnFlower = (Button) convertView.findViewById(R.id.button_confessItem_flowers);
			viewHolder.btnFlower.setTag(position);
			viewHolder.btnComment = (Button) convertView.findViewById(R.id.button_confessItem_comment);
			viewHolder.btnComment.setTag(position);
			viewHolder.btnFlower.setOnClickListener(mButonListener);
			viewHolder.btnComment.setOnClickListener(mButonListener);

			viewHolder.layoutContent = convertView.findViewById(R.id.relativeLayout_content);
			// viewHolder.layoutPicture=convertView.findViewById(R.id.relativeLayout_picture);
			viewHolder.layoutInfo = convertView.findViewById(R.id.relativeLayout_info);

			viewHolder.author = (TextView) convertView.findViewById(R.id.textView_confessItem_author);
			viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.imageView_confessItem_icon);
			if (mType == TYPE_MINE) {
				viewHolder.author.setVisibility(View.GONE);
				viewHolder.ivIcon.setVisibility(View.GONE);
			}

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.content.setText(curConfess.getContent());
		viewHolder.publishDate.setText(DateUtil.formatDateTime(curConfess.getPublishDate()));
		viewHolder.position.setText(curConfess.getPosition());

		viewHolder.btnFlower.setText(mContext.getString(R.string.confessItem_flowers) + " "
				+ curConfess.getFlowersCount());
		viewHolder.btnComment.setText(mContext.getString(R.string.confessItem_comment) + " "
				+ curConfess.getCommentCount());

		// viewHolder.layoutPicture.setVisibility(View.GONE);
		// viewHolder.layoutInfo.setVisibility(View.GONE);

		if (mType == TYPE_NORMAL) {
			viewHolder.author.setText(curConfess.getAuthor());
			mImageLoader.displayImage(curConfess.getIcon(), viewHolder.ivIcon, mOptions, animateFirstListener);
		}
		
		if(curConfess.getPicture() != null && !curConfess.getPicture().equals("")){
			viewHolder.ivPicture.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(curConfess.getPicture(), viewHolder.ivPicture, mOptions, animateFirstListener);
		}else{
			viewHolder.ivPicture.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ButtonListener implements View.OnClickListener {
		//获取当前表白，取List的第一个元素。
		@Override
		public void onClick(View button) {
			final ConfessItem confess = mConfessList.get((Integer)button.getTag());
			switch (button.getId()) {
				case R.id.button_confessItem_comment:
					confess.setCommentCount(confess.getCommentCount()+1);
					ConfessAdapter.this.notifyDataSetChanged();
					Intent intent = new Intent(mContext, CommentActivity.class);
					intent.putExtra(CommentActivity.POST_ID, confess.getId());
					mContext.startActivity(intent);
					break;
				case R.id.button_confessItem_flowers:
					
					if (flowerMap.get(CommentActivity.POST_ID) == true) {
						Toast.makeText(mContext, "你已经送过花儿啦~~", 1000).show();
						break;
					} else {
						flowerMap.put(CommentActivity.POST_ID, true);
						confess.setFlowersCount(confess.getFlowersCount()+1);
						ConfessAdapter.this.notifyDataSetChanged();
						ServerAccess.flower(confess.getId(), new ServerResponseHandler() {
							@Override
							public void onSuccess(JSONObject result) {
								// TODO Auto-generated method stub
								try {
									if (0 == result.getInt("status")) {
										confess.setFlowersCount(result.getInt("count"));
										//ConfessAdapter.this.notifyDataSetChanged();
										//Toast.makeText(mContext, "Flower:"+confess.getFlowersCount(), 1000).show();
										//getView(itemId, mInflater.inflate(R.layout.item_confess, null), null);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
	
							@Override
							public void onFailure(Throwable error) {
								// TODO Auto-generated method stub
								Toast.makeText(mContext, "送花失败。。。", 1000).show();
								Log.e("Flower Failure", "Flower Failure");
							}
							
						});
					}
			}
		}
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
