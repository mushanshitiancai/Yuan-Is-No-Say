package com.yuan.yuanisnosay.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yuan.yuanisnosay.R;

public class DistancePopup {
	private static final String TAG = "yuan_DistancePopup";
	private PopupWindow mDistancePopup;
	private LayoutInflater mInflater;

	private int[] mDistanceArr;
	private int mCurDistance;
	private DistancePopupListener mListener;

	public interface DistancePopupListener {
		void onDistanceChange(int distance);
	}

	public DistancePopup(Context context, View aimButton, int[] distanceArr, DistancePopupListener listener) {
		mInflater = LayoutInflater.from(context);
		mListener = listener;
		mCurDistance = distanceArr[0];
		mDistanceArr=distanceArr;

		aimButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (dismissDistancePopup())
					return;

				showDistancePopup(view);
			}
		});
	}

	public void showDistancePopup(View aimButton) {
		final int height = aimButton.getLayoutParams().height * (mDistanceArr.length - 1);
		final int width = aimButton.getLayoutParams().width;
		LinearLayout popupView = (LinearLayout) mInflater.inflate(R.layout.popup_distance, null);

		for (int i = 0; i < mDistanceArr.length; i++) {
			if (mDistanceArr[i] != mCurDistance) {
				Button btn = (Button) mInflater.inflate(R.layout.popup_distance_button, null);
				btn.setText(mDistanceArr[i] + " m");
				btn.setTag(mDistanceArr[i]);
				btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View button) {
						int dis = (Integer) button.getTag();
						if (dis != mCurDistance) {
							mCurDistance = dis;
							mListener.onDistanceChange(dis);
						}
						mDistancePopup.dismiss();
					}
				});

				ViewGroup.LayoutParams params = new LayoutParams(aimButton.getLayoutParams().width,
						aimButton.getLayoutParams().height);
				popupView.addView(btn, params);
			}
		}

		mDistancePopup = new PopupWindow(popupView, width, height);
		mDistancePopup.setFocusable(false);
		mDistancePopup.setAnimationStyle(R.style.PopupAnimation);
		
		int[] location = new int[2];
		aimButton.getLocationInWindow(location);
		mDistancePopup.showAtLocation(aimButton, Gravity.NO_GRAVITY, location[0], location[1] - height);
	}

	public boolean dismissDistancePopup() {
		if (mDistancePopup != null && mDistancePopup.isShowing()) {
			mDistancePopup.dismiss();
			return true;
		}
		return false;
	}
}
