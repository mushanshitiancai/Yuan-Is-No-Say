package com.yuan.yuanisnosay.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.lee.pullrefresh.ui.PullToRefreshListView;
import com.yuan.yuanisnosay.Const;
import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.Util;
import com.yuan.yuanisnosay.ui.adpater.ConfessAdapter;
import com.yuan.yuanisnosay.ui.adpater.ConfessItem;

/**
 * 表白fragment
 * 
 * @author 志彬
 * 
 */
public class ConfessFragment extends Fragment {
	private static final String TAG = "yuan_ConfessFragment";
	public static final int TYPE_NEARBY = 0;
	public static final int TYPE_HOT = 1;
	
	public static final String ARGUMENT_VISIABLE="ARGUMENT_VISIABLE";

	private int mType;
	private Bundle mArguments;
	private PullToRefreshListView mRefreshListView;
	private ListView mListView;
	private ConfessAdapter mAdapter;
	private LinkedList<ConfessItem> mConfessList;
	
	public ConfessFragment(int type) {
		mType=type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//mArguments = getArguments();
		//boolean isVisiable = mArguments.getBoolean(ARGUMENT_VISIABLE);
		
		View rootView = inflater.inflate(R.layout.fragment_confess, container,
				false);
		mRefreshListView = (PullToRefreshListView) rootView
				.findViewById(R.id.pull_refresh_list);
		mListView = mRefreshListView.getRefreshableView();
		mRefreshListView.setPullLoadEnabled(false);
		mRefreshListView.setScrollLoadEnabled(true);

//		mConfessList = new LinkedList<ConfessItem>();
		mConfessList = getConfesses();
//		mConfessList.addAll(ConfessItem.getConfess(0, 3));
		mAdapter = new ConfessAdapter(getActivity(), mConfessList);
		mListView.setAdapter(mAdapter);

		mRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e(TAG, "onPullDownToRefresh");
						new GetConfessTask()
								.execute(GetConfessTask.ACTION_PULL_TO_REFRESH,0,1);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e(TAG, "onPullUpToRefresh");
						new GetConfessTask()
								.execute(GetConfessTask.ACTION_PULL_TO_MORE,mConfessList.size(),1);
					}
				});
		
		// mRefreshListView.doPullRefreshing(true, 500);

		//if(isVisiable==false)
		//	rootView.setVisibility(View.GONE);
		return rootView;
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
		//保存表白列表
		saveConfesses();
	}

	/**
	 *  刷新列表
	 */
	public void refesh() {
		mRefreshListView.doPullRefreshing(true, 50);
	}

	/**
	 *  设置listview的更新时间文本为当前时间
	 */
	private void setLastUpdateTime() {
		String text = Util.formatDateTime(System.currentTimeMillis());
		mRefreshListView.setLastUpdatedLabel(text);
	}
	
	/**
	 * 保存表白列表
	 */
	private void saveConfesses(){
		Log.e(TAG, "saveConfesses():"+mConfessList);
		File confessFile = new File(Const.YUAN_FOLDER_NAME,Const.CONFESS_FILENAME+mType);
		try {
			FileOutputStream fos=new FileOutputStream(confessFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mConfessList);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取表白列表
	 * @return 
	 */
	private LinkedList<ConfessItem> getConfesses(){
		File confessFile = new File(Const.YUAN_FOLDER_NAME,Const.CONFESS_FILENAME+mType);
		try {
			FileInputStream fis = new FileInputStream(confessFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mConfessList=(LinkedList<ConfessItem>) ois.readObject();
			ois.close();
			
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
		if(mConfessList==null) mConfessList = new LinkedList<ConfessItem>(); 
		Log.e(TAG, "getConfesses():"+mConfessList);
		return mConfessList;
	}

	/**
	 * 获取表白异步任务 入口：下拉刷新 ：上拉更多
	 * 
	 * @author 志彬
	 * 
	 */
	class GetConfessTask extends AsyncTask<Integer, Void, List<ConfessItem>> {
//		public static final int ACTION_FIRST_GET = 0;
		public static final int ACTION_PULL_TO_REFRESH = 1;
		public static final int ACTION_PULL_TO_MORE = 2;

		private int mAction;
		private int mGetCount;
		private boolean hasMoreData=true;

		/**
		 * params[0] 任务类型 params[1] 从第几条记录开始 params[2] 读取几条记录
		 */
		@Override
		protected List<ConfessItem> doInBackground(Integer... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mAction = params[0];
			int start = params[1];
			mGetCount = params[2];
			
			List<ConfessItem> result=null;
			hasMoreData=true;
			
			if (mAction == ACTION_PULL_TO_REFRESH) {	//去重+获取所有更新（可能不止获取一次）
				boolean isGetAllRefersh = false;
				int curStart=start;
				while(isGetAllRefersh==false){
					result=ConfessItem.getConfess(curStart, mGetCount);
					if(mConfessList.size()==0)
						isGetAllRefersh = true;
					for(int i=0;i<result.size();i++){
						if(isItemExisted(result.get(i))==false){
							mConfessList.add(curStart+i, result.get(i));
//							mConfessList.addFirst(result.get(i));
						}else{
							isGetAllRefersh = true;
						}
					}
					curStart+=mGetCount;
				}
				
			}else if(mAction == ACTION_PULL_TO_MORE){
				result=ConfessItem.getConfess(start, mGetCount);
				if(result.size()<mGetCount){
					Log.e(TAG, "已经到底啦");
					hasMoreData=false;
				}
				mConfessList.addAll(result);
				Log.e(TAG, "ACTION_PULL_TO_MORE:start="+start+" result="+result);
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(List<ConfessItem> result) {
			Log.e(TAG, "result:"+result);
//			boolean hasMoreData=true;
//			if (mAction == ACTION_PULL_TO_REFRESH) {
//				for(int i=0;i<result.size();i++){
//					if(!isItemExisted(result.get(i))){
//						mConfessList.addFirst(result.get(i));
//					}
//				}
//			}else if(mAction == ACTION_PULL_TO_MORE){
//				if(result.size()<mGetCount){
//					Log.e(TAG, "已经到底啦");
//					hasMoreData=false;
//				}
//				mConfessList.addAll(result);
//			}

			mAdapter.notifyDataSetChanged();
			if (mAction == ACTION_PULL_TO_MORE) {
				mRefreshListView.onPullUpRefreshComplete();
			} else {
				mRefreshListView.onPullDownRefreshComplete();
			}
			mRefreshListView.setHasMoreData(hasMoreData);
			setLastUpdateTime();
		}
	}
	
	private boolean isItemExisted(ConfessItem item){
		for(int i=0;i<mConfessList.size();i++){
			if(item.getId() == mConfessList.get(i).getId())
				return true;
		}
		return false;
	}

}
