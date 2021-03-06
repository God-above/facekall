package com.falcon.facekall.av;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.falcon.facekall.R;
import com.tencent.avsdk.adapter.ChatMsgListAdapter;


public class DisplayOrgPicActivity  extends MyBaseActivity {
	private final static String TAG = DisplayOrgPicActivity.class.getSimpleName();
	private ImageView mIVOrgPic;
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_org_pic);
		initView();	
	}
	
	public void initView() {	
		
		String filePath = getIntent().getStringExtra("filePath");
		Log.d(TAG,"init ivew:" + filePath);
		Bitmap bitmap = ChatMsgListAdapter.GetRightOritationNew(filePath);
		Log.d(TAG,"bitmap");
		mIVOrgPic = (ImageView) findViewById(R.id.iv_org_pic);
		mIVOrgPic.setImageBitmap(bitmap);
	
		mIVOrgPic.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {			
				finish();
			} 			
		});
		
	}

}
