package com.yuan.yuanisnosay.ui.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yuan.yuanisnosay.MainActivity;
import com.yuan.yuanisnosay.R;
//import com.yuan.yuanisnosay.Util;
import com.yuan.yuanisnosay.ui.adpater.ConfessAdapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
 
public class NewCommentAdapter extends BaseAdapter {
	private Context mContext;
    private List<ItemData_newcomment> mList;
     
    public NewCommentAdapter(Context context,List<ItemData_newcomment> list){
        mContext = context;
        setmList(list);
    }
 
     
    public void setList(List<ItemData_newcomment> list){
        setmList(list);
    }
     
    @Override
    public int getCount() {
         
        return getmList().size();
    }
 
    @Override
    public Object getItem(int position) {
         
        return null;
    }
 
    @Override
    public long getItemId(int position) {
         
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	if(getmList() == null) {
    		return null;
    	}
        HolderView holder = null;
        if(convertView==null){
            holder = new HolderView();
            //修改成你自己的布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_newcomment, null);
            holder.text= (TextView)convertView.findViewById(R.id.newcomment);
            convertView.setTag(holder);
             
        }else{
            holder = (HolderView)convertView.getTag();
        }
        ItemData_newcomment curnewcomment = getmList().get(position);
        holder.text.setText(curnewcomment.tostring());
         
        /**
         * 加入数据怎么怎么样，
         */
        if(getmList().get(position) != null){
            /**
             * 跳转到某个Activity里
             */
            convertView.setOnClickListener(new OnClickListener() {
                 
                @Override
                public void onClick(View view) {
                    //跳转到某个Activity里
                	Intent intent=new Intent();  
                    intent.setClass(mContext, MainActivity.class);  
                    Bundle bundle = new Bundle();  
                    int id = mList.get(position).getRawConfessid();  
                    bundle.putInt("confessid", id);  
                    intent.putExtras(bundle); 
                    mContext.startActivity(intent);  
                	
                }
            });
        }
         
         
         
        return convertView;
    }
     
    public List<ItemData_newcomment> getmList() {
		return mList;
	}


	public void setmList(List<ItemData_newcomment> mList) {
		this.mList = mList;
	}

	/**
     * 这个是List item里面的组件，根据你自己的
     * 情况修改
     * @author Administrator
     */
    class HolderView{
        TextView text;
        
    }
 }
