package com.yuan.yuanisnosay.ui.adpater;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	List<String> mData;
	Context mContext;
	
	public ListAdapter(Context context,List<String> data) {
		mData=data;
		mContext=context;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=new TextView(mContext);
		}
		TextView tv=(TextView)convertView;
		tv.setText(mData.get(position));
		
		return convertView;
	}

}
