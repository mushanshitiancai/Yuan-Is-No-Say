package com.yuan.yuanisnosay.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.util.Log;

import com.yuan.yuanisnosay.Const;
import com.yuan.yuanisnosay.ui.adpater.ConfessItem;

/**
 * 
 * @author 志彬
 * 
 */
public class StorageModel {
	private static final String TAG = "yuan_StorageModel";
	public static final String KEY_NEARBY_LIST = "KEY_NEARBY_LIST";
	public static final String KEY_HOT_LIST = "KEY_HOT_LIST";
	public static final String KEY_MINE_LIST = "KEY_MINE_LIST";

	private Map<String, Serializable> mStorageMap;

	private static StorageModel mInstance = null;

	public static final StorageModel getInstance() {
		if (mInstance == null) {
			mInstance = new StorageModel();
		}
		return mInstance;
	}

	private StorageModel() {
		// mStorageMap=new HashMap<String, Serializable>();
		read();
	}

	public void addData(String key, Serializable data) {
		mStorageMap.put(key, data);
	}

	public Serializable getData(String key) {
		return mStorageMap.get(key);
	}

	public void save() {
		File file = new File(Const.YUAN_FOLDER_NAME, Const.STORAGE_FILENAME);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mStorageMap);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read() {
		File file = new File(Const.YUAN_FOLDER_NAME, Const.STORAGE_FILENAME);
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mStorageMap = (HashMap<String, Serializable>) ois.readObject();
			ois.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (mStorageMap == null)
			mStorageMap = new HashMap<String, Serializable>();
		Log.e(TAG, "getConfesses():" + mStorageMap);
	}

	public void delete() {
		File file = new File(Const.YUAN_FOLDER_NAME, Const.STORAGE_FILENAME);
		file.delete();
	}

	/**
	 * 建立app目录
	 */
	public static final void createAppFolder() {
		File folder = new File(Const.YUAN_FOLDER_NAME);
		if (folder.exists() == false) {
			folder.mkdir();
		}
	}
}
