package com.yuan.yuanisnosay.ui;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuan.yuanisnosay.R;

/**
 * 表白fragment
 * @author 志彬
 *
 */
public class ConfessFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView=inflater.inflate(R.layout.fragment_confess, container, false);
		
		
		
		return rootView;
	}
}
