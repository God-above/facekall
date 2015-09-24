package com.falcon.facekall.mailList;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.HTTP.FLHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.av.MyBaseActivity;
import com.tencent.avsdk.c2c.HttpProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestActivity extends MyBaseActivity {
	private final static String TAG = FriendRequestActivity.class.getSimpleName();
    FLHttpLogin mHttp=new FLHttpLogin();
    private String targetUid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		initView();	
	}
	
	public void initView() {	
	}
		

	//按钮点击事件
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final Intent intent=new Intent();
		switch (v.getId()) {
			case R.id.tvFindFromMail:
				break;
			case R.id.tvFindFromWeChat:
				break;
			default:
				break;
		}
	}

}
