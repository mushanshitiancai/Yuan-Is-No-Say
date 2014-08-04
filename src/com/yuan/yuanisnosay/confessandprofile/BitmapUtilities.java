package com.yuan.yuanisnosay.confessandprofile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtilities {

	public BitmapUtilities() {
		// TODO Auto-generated constructor stub
	}
	
	public static Bitmap getBitmapThumbnail(String path,int width,int height){
		Bitmap bitmap = null;
		//这里可以按比例缩小图片：
		/*BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 4;//宽和高都是原来的1/4
		bitmap = BitmapFactory.decodeFile(path, opts); */
		
		/*进一步的，
	            如何设置恰当的inSampleSize是解决该问题的关键之一。BitmapFactory.Options提供了另一个成员inJustDecodeBounds。
	           设置inJustDecodeBounds为true后，decodeFile并不分配空间，但可计算出原始图片的长度和宽度，即opts.width和opts.height。
	           有了这两个参数，再通过一定的算法，即可得到一个恰当的inSampleSize。*/
		BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options); 
	    
	    int i = 0;
	    while (true) {
			if ((options.outWidth >> i <= width)
					&& (options.outHeight >> i <= height)) {
//				in = new BufferedInputStream(
//						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
//				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
//	    options.inSampleSize = Math.max((int)(options.outHeight / (float) height), (int)(options.outWidth / (float) width));
//	    options.inJustDecodeBounds = false;
	    
	    bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	/*
	public static Bitmap getBitmapThumbnail(Bitmap bmp,int width,int height){
		Bitmap bitmap = null;
		if(bmp != null ){
			int bmpWidth = bmp.getWidth();
			int bmpHeight = bmp.getHeight();
			if(width != 0 && height !=0){
				Matrix matrix = new Matrix();
				float scaleWidth = ((float) width / bmpWidth);
				float scaleHeight = ((float) height / bmpHeight);
				matrix.postScale(scaleWidth, scaleHeight);
				bitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
			}else{
				bitmap = bmp;
			}
		}
		return bitmap;
	}*/

}
