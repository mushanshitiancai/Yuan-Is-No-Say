package com.yuan.yuanisnosay.confessandprofile;

import java.io.IOException;

import com.yuan.yuanisnosay.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * 应用于表白界面中的图片，以及个人资料修改界面中的头像
 * @author Juanna
 *
 */
public class PicThumAdapter extends BaseAdapter{
	
	private static final int FOR_PERSONAL_PROFILE = 0;
	private static final int FOR_SEND_CONFESS = 1;
	
	private int forWho;
	private Context mContext;
//	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	
	
	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public PicThumAdapter(Context context,int forWho) {
		this.mContext = context;
		this.forWho = forWho;
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
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
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
			if (forWho == FOR_PERSONAL_PROFILE) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.ic_launcher));
			} else if (forWho == FOR_SEND_CONFESS) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						mContext.getResources(),
						R.drawable.icon_addpic_unfocused));
			}
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

	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				PicThumAdapter.this.notifyDataSetChanged();
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
