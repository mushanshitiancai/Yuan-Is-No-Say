package com.yuan.yuanisnosay.ui;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.yuan.yuanisnosay.DateUtil;
import com.yuan.yuanisnosay.MainActivity;
import com.yuan.yuanisnosay.R;
import com.yuan.yuanisnosay.YuanApplication;
import com.yuan.yuanisnosay.server.ServerAccess;
import com.yuan.yuanisnosay.server.ServerAccess.ServerResponseHandler;
import com.yuan.yuanisnosay.storage.StorageModel;
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
	public static final int TYPE_MINE = 2;

	// public static final int MESSAGE_REFRESH_LIST = 0;
	public static final int MESSAGE_PULL_UP_REFRESH_COMPLETE = 1;
	public static final int MESSAGE_PULL_DOWN_REFRESH_COMPLETE = 2;
	public static final int MESSAGE_PULL_UP_REFRESH_FAIL = 3;
	public static final int MESSAGE_PULL_DOWN_REFRESH_FAIL = 4;

	public static final String ARGUMENT_VISIABLE = "ARGUMENT_VISIABLE";

	private YuanApplication mApp;
	private int mType;
	private String mStorageKey;
	private Bundle mArguments;
	private PullToRefreshListView mRefreshListView;
	private ListView mListView;
	private ConfessAdapter mAdapter;
	private LinkedList<ConfessItem> mConfessList;
	private Handler mHandler;
	private PullDownResponseHandler mPullDownResponseHandler;
	private PullUpResponseHandler mPullUpResponseHandler;
	
	public interface ConfessActivityInterface{
		public int getDistance();
	}

	public ConfessFragment(int type) {
		mType = type;
		if (mType == TYPE_NEARBY) {
			mStorageKey = StorageModel.KEY_NEARBY_LIST;
		} else if (mType == TYPE_HOT) {
			mStorageKey = StorageModel.KEY_HOT_LIST;
		} else if (mType == TYPE_MINE) {
			mStorageKey = StorageModel.KEY_MINE_LIST;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mApp = (YuanApplication) getActivity().getApplication();
		// mArguments = getArguments();
		// boolean isVisiable = mArguments.getBoolean(ARGUMENT_VISIABLE);

		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				// case MESSAGE_REFRESH_LIST:
				// {
				//
				// mAdapter.notifyDataSetChanged();
				// if (mAction == ACTION_PULL_TO_MORE) {
				// mRefreshListView.onPullUpRefreshComplete();
				// } else {
				// mRefreshListView.onPullDownRefreshComplete();
				// }
				// mRefreshListView.setHasMoreData(hasMoreData);
				// setLastUpdateTime();
				// break;
				// }
				case MESSAGE_PULL_DOWN_REFRESH_COMPLETE:
					mAdapter.notifyDataSetChanged();
					mRefreshListView.onPullDownRefreshComplete();
					setLastUpdateTime();
					break;
				case MESSAGE_PULL_UP_REFRESH_COMPLETE:
					mAdapter.notifyDataSetChanged();
					mRefreshListView.onPullUpRefreshComplete();
					setLastUpdateTime();
					if(msg.arg1==0){
						mRefreshListView.setHasMoreData(false);
					}
					break;
				case MESSAGE_PULL_DOWN_REFRESH_FAIL:
					// mAdapter.notifyDataSetChanged();
					mRefreshListView.onPullDownRefreshComplete();
					// setLastUpdateTime();
					break;
				case MESSAGE_PULL_UP_REFRESH_FAIL:
					mRefreshListView.onPullUpRefreshComplete();
					break;
				}
			};
		};

		View rootView = inflater.inflate(R.layout.fragment_confess, container, false);
		mRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);
		mListView = mRefreshListView.getRefreshableView();
		mRefreshListView.setPullLoadEnabled(false);
		mRefreshListView.setScrollLoadEnabled(true);
		mListView.setVerticalScrollBarEnabled(false);

		mConfessList = getConfesses();
		if (mType == TYPE_MINE) {
			mAdapter = new ConfessAdapter(getActivity(), ConfessAdapter.TYPE_MINE, mConfessList);
		} else {
			mAdapter = new ConfessAdapter(getActivity(), ConfessAdapter.TYPE_NORMAL, mConfessList);
		}

		mListView.setAdapter(mAdapter);

		mPullDownResponseHandler=new PullDownResponseHandler();
		mPullUpResponseHandler=new PullUpResponseHandler();
		mRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.e(TAG, "onPullDownToRefresh");
				if(mType==TYPE_NEARBY){
					int distance = 0;
					Activity activity=getActivity();
					if(activity instanceof ConfessActivityInterface){
						ConfessActivityInterface mainActivity=(ConfessActivityInterface) activity;
						distance = mainActivity.getDistance();
					}
					ServerAccess.getNewConfessListNearby(mApp.getRegionName(), mApp.getLongitude(), mApp.getLatitude(), Const.GET_COUNT, distance, mPullDownResponseHandler);
				}else{
					ServerAccess.getNewConfessListHot(Const.GET_COUNT, mPullDownResponseHandler);
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.e(TAG, "onPullUpToRefresh");
				int baseId = mConfessList.getLast().getId();
				if(mType==TYPE_NEARBY){
					int distance = 0;
					Activity activity=getActivity();
					if(activity instanceof ConfessActivityInterface){
						ConfessActivityInterface mainActivity=(ConfessActivityInterface) activity;
						distance = mainActivity.getDistance();
					}
					ServerAccess.getMoreConfessListNearby(mApp.getRegionName(), mApp.getLongitude(), mApp.getLatitude(), baseId,Const.GET_COUNT, distance, mPullUpResponseHandler);
				}else{
					ServerAccess.getMoreConfessListHot(baseId, Const.GET_COUNT, mPullUpResponseHandler);
				}
			}
		});

		return rootView;
	}
	
	/**
	 * 下拉刷新回调
	 * @author 志彬
	 *
	 */
	class PullDownResponseHandler implements ServerResponseHandler{
		
		@Override
		public void onSuccess(JSONObject result) {
			try {
				if (ServerAccess.getStatus(result) != 0)
					return;
				List<ConfessItem> resultConfessList = ConfessItem.getConfessList(result);
				Log.e(TAG, resultConfessList.toString());

				boolean isExistedConfessTooOld = false;
				if (mConfessList.size() == 0) {
					mConfessList.addAll(resultConfessList);
				} else {
					isExistedConfessTooOld = !isItemsContains(resultConfessList);
					if (isExistedConfessTooOld) {
						mConfessList.clear();
						mConfessList.addAll(resultConfessList);
					} else {
						LinkedList<ConfessItem> tempList = new LinkedList<ConfessItem>();
						for (int i = 0; i < resultConfessList.size(); i++) {
							if (isItemExisted(resultConfessList.get(i)) == false) {
								tempList.addFirst(resultConfessList.get(i));
							}
						}
						for (ConfessItem item : tempList) {
							mConfessList.addFirst(item);
						}
					}
				}
				mHandler.sendEmptyMessage(MESSAGE_PULL_DOWN_REFRESH_COMPLETE);
			} catch (JSONException e) {
				Log.e(TAG, "Json 解析出错");
				mHandler.sendEmptyMessage(MESSAGE_PULL_DOWN_REFRESH_FAIL);
				// e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error) {
			Log.e(TAG, "onPullDownToRefresh:" + error.toString());
			Util.showToast(getActivity(), "下拉刷新失败");
			mHandler.sendEmptyMessage(MESSAGE_PULL_DOWN_REFRESH_FAIL);
		}
		
	}
	
	/**
	 * 上拉更多回调
	 * @author 志彬
	 *
	 */
	class PullUpResponseHandler implements ServerResponseHandler{
		@Override
		public void onSuccess(JSONObject result) {
			int hasMoreData = 1;
			try {
				if (ServerAccess.getStatus(result) != 0)
					return;
				List<ConfessItem> resultConfessList = ConfessItem.getConfessList(result);
				Log.e(TAG, resultConfessList.toString());

				if (resultConfessList.size() < Const.GET_COUNT) {
					Log.e(TAG, "已经到底啦");
					hasMoreData = 0;
				}
				mConfessList.addAll(resultConfessList);
				
				Message msg=mHandler.obtainMessage(MESSAGE_PULL_UP_REFRESH_COMPLETE, hasMoreData, 0);
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				Log.e(TAG, "Json 解析出错");
				mHandler.sendEmptyMessage(MESSAGE_PULL_UP_REFRESH_FAIL);
				// e.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error) {
			Log.e(TAG, "onPullDownToRefresh:" + error.toString());
			Util.showToast(getActivity(), "上拉刷新失败");
			mHandler.sendEmptyMessage(MESSAGE_PULL_UP_REFRESH_FAIL);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");

		// 保存表白列表
		mApp.getStorage().addData(mStorageKey, mConfessList);
		// saveConfesses();
	}

	/**
	 * 刷新列表
	 */
	public void refesh() {
		mRefreshListView.doPullRefreshing(true, 50);
	}
	
	/**
	 * 改变距离时刷新列表
	 * @param distance
	 */
	public void onRefershByDistance(int distance){
		if(mType==TYPE_HOT) return ;
		mConfessList.clear();
		mRefreshListView.setHasMoreData(true);
		refesh();
	}

	/**
	 * 设置listview的更新时间文本为当前时间
	 */
	private void setLastUpdateTime() {
		String text = DateUtil.formatDateTime(System.currentTimeMillis());
		mRefreshListView.setLastUpdatedLabel(text);
	}

	private LinkedList<ConfessItem> getConfesses() {
		LinkedList<ConfessItem> confessList = null;
		// confessList = (LinkedList<ConfessItem>)
		// mApp.getStorage().getData(mStorageKey); TODO
		if (confessList == null)
			confessList = new LinkedList<ConfessItem>();
		Log.e(TAG, "getConfesses():" + confessList);
		return confessList;
	}

	/**
	 * 获取表白异步任务 入口：下拉刷新 ：上拉更多
	 * 
	 * @author 志彬
	 * 
	 */
	class GetConfessTask extends AsyncTask<Integer, Void, List<ConfessItem>> {
		// public static final int ACTION_FIRST_GET = 0;
		public static final int ACTION_PULL_TO_REFRESH = 1;
		public static final int ACTION_PULL_TO_MORE = 2;

		private int mAction;
		private int mGetCount;
		private boolean hasMoreData = true;

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

			List<ConfessItem> result = null;
			hasMoreData = true;

			if (mAction == ACTION_PULL_TO_REFRESH) { // 去重+获取所有更新（可能不止获取一次）
				boolean isGetAllRefersh = false;
				int curStart = start;
				while (isGetAllRefersh == false) {
					result = ConfessItem.getConfess(curStart, mGetCount);
					if (mConfessList.size() == 0)
						isGetAllRefersh = true;
					for (int i = 0; i < result.size(); i++) {
						if (isItemExisted(result.get(i)) == false) {
							mConfessList.add(curStart + i, result.get(i));
							// mConfessList.addFirst(result.get(i));
						} else {
							isGetAllRefersh = true;
						}
					}
					curStart += mGetCount;
				}
			} else if (mAction == ACTION_PULL_TO_MORE) {
				result = ConfessItem.getConfess(start, mGetCount);
				if (result.size() < mGetCount) {
					Log.e(TAG, "已经到底啦");
					hasMoreData = false;
				}
				mConfessList.addAll(result);
				Log.e(TAG, "ACTION_PULL_TO_MORE:start=" + start + " result=" + result);
			}

			return result;
		}

		@Override
		protected void onPostExecute(List<ConfessItem> result) {
			Log.e(TAG, "result:" + result);
			// boolean hasMoreData=true;
			// if (mAction == ACTION_PULL_TO_REFRESH) {
			// for(int i=0;i<result.size();i++){
			// if(!isItemExisted(result.get(i))){
			// mConfessList.addFirst(result.get(i));
			// }
			// }
			// }else if(mAction == ACTION_PULL_TO_MORE){
			// if(result.size()<mGetCount){
			// Log.e(TAG, "已经到底啦");
			// hasMoreData=false;
			// }
			// mConfessList.addAll(result);
			// }

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

	private boolean isItemExisted(ConfessItem item) {
		for (int i = 0; i < mConfessList.size(); i++) {
			if (item.getId() == mConfessList.get(i).getId())
				return true;
		}
		return false;
	}

	private boolean isItemsContains(List<ConfessItem> list) {
		for (int i = 0; i < mConfessList.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getId() == mConfessList.get(i).getId())
					return true;
			}
		}
		return false;
	}

}
