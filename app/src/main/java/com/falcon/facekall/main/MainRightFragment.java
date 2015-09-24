package com.falcon.facekall.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.HTTP.FLHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.tencent.avsdk.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainRightFragment extends Fragment implements View.OnClickListener{
	private CircleImageView iconHead;
	private TextView myNickName,myMobile;
	FLHttpLogin mHttp=new FLHttpLogin();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_layout_right, container, false);
		initData();
		initView(view);
		return view;
	}

	private void initData() {
//		String uid = FacekallApplication.getInstance().userID();
//		mHttp.queryFriendList(uid, "1", "16", new FLHttpListener() {
//			@Override
//			public void onHttpDone(JSONObject result) throws JSONException {
//				try {
//					if(result.getString("r").equals(FLHttp.RESULTSUCCESS)){
//
//					}else{
//						return;
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	private void initView(View view) {
		view.findViewById(R.id.right_menu_icon).setOnClickListener(this);
		view.findViewById(R.id.right_menu_nickname).setOnClickListener(this);
		view.findViewById(R.id.right_menu_mobile).setOnClickListener(this);
		view.findViewById(R.id.right_menu_setting).setOnClickListener(this);
		view.findViewById(R.id.right_menu_expression).setOnClickListener(this);
		view.findViewById(R.id.right_menu_file).setOnClickListener(this);
		view.findViewById(R.id.right_menu_contacts).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.right_menu_icon:
				break;
			case R.id.right_menu_nickname:
				break;
			case R.id.right_menu_mobile:
				break;
			case R.id.right_menu_setting:
				break;
			case R.id.right_menu_expression:
				break;
			case R.id.right_menu_file:
				break;
			case R.id.right_menu_contacts:
				break;
		}
	}
}
