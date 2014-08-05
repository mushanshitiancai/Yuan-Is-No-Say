package com.yuan.yuanisnosay.confessandprofile;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageAdapterHelper {
	
	private ImageShowManager imageManager;
	private int picWidth;
	private int picHeight; 
	
	public ImageAdapterHelper(Activity uiActivity,int picWidth,int picHeight) {
		imageManager = ImageShowManager.from(uiActivity);
		this.picWidth = picWidth;
		this.picHeight = picHeight;
	}

	public void showImg(String imgPath, ImageView iv) {
		if (cancelPotentialLoad(imgPath, iv)) {
			AsyncLoadImageTask task = new AsyncLoadImageTask(iv);
			iv.setImageDrawable(new LoadingDrawable(task));
			task.execute(imgPath);
		}
	}

	/**
	 * 判断当前的imageview是否在加载相同的资源
	 * 
	 * @param url
	 * @param imageview
	 * @return
	 */
	private boolean cancelPotentialLoad(String url, ImageView imageview) {

		AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
		if (loadImageTask != null) {
			String bitmapUrl = loadImageTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				loadImageTask.cancel(true);
			} else {
				// 相同的url已经在加载中.
				return false;
			}
		}
		return true;
	}

	/**
	 * 负责加载图片的异步线程
	 * 
	 * @author Administrator
	 * 
	 */
	class AsyncLoadImageTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		private String url = null;

		public AsyncLoadImageTask(ImageView imageview) {
			super();
			imageViewReference = new WeakReference<ImageView>(imageview);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			/**
			 * 具体的获取bitmap的部分，流程： 从内存缓冲区获取，如果没有向硬盘缓冲区获取，如果没有从sd卡/网络获取
			 */

			Bitmap bitmap = null;
			this.url = params[0];

			// 从内存缓存区域读取
			bitmap = imageManager.getBitmapFromMemory(url);
			if (bitmap != null) {
				Log.d("dqq", "return by 内存");
				return bitmap;
			}
			// 从硬盘缓存区域中读取
			bitmap = imageManager.getBitmapFormDisk(url);
			if (bitmap != null) {
				imageManager.putBitmapToMemery(url, bitmap);
				Log.d("dqq", "return by 硬盘");
				return bitmap;
			}

			// 没有缓存则从原始位置读取
//			bitmap = BitmapUtilities.getBitmapThumbnail(url,
//					ImageShowManager.bitmap_width,
//					ImageShowManager.bitmap_height);
			bitmap = BitmapUtilities.getBitmapThumbnail(url,
					picWidth,picHeight);
			imageManager.putBitmapToMemery(url, bitmap);
			imageManager.putBitmapToDisk(url, bitmap);
			Log.d("dqq", "return by 原始读取");
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap resultBitmap) {
			if (isCancelled()) {
				// 被取消了
				resultBitmap = null;
			}
			if (imageViewReference != null) {
				ImageView imageview = imageViewReference.get();
				AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
				if (this == loadImageTask) {
					imageview.setImageDrawable(null);
					imageview.setImageBitmap(resultBitmap);
				}

			}

			super.onPostExecute(resultBitmap);
		}
	}

	/**
	 * 根据imageview，获得正在为此imageview异步加载数据的函数
	 * 
	 * @param imageview
	 * @return
	 */
	private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview) {
		if (imageview != null) {
			Drawable drawable = imageview.getDrawable();
			if (drawable instanceof LoadingDrawable) {
				LoadingDrawable loadedDrawable = (LoadingDrawable) drawable;
				return loadedDrawable.getLoadImageTask();
			}
		}
		return null;
	}

	/**
	 * 记录imageview对应的加载任务，并且设置默认的drawable
	 * 
	 * @author Administrator
	 * 
	 */
	public static class LoadingDrawable extends ColorDrawable {
		// 引用与drawable相关联的的加载线程
		private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

		public LoadingDrawable(AsyncLoadImageTask loadImageTask) {
			super(Color.GRAY);
			loadImageTaskReference = new WeakReference<AsyncLoadImageTask>(
					loadImageTask);
		}

		public AsyncLoadImageTask getLoadImageTask() {
			return loadImageTaskReference.get();
		}
	}
}
