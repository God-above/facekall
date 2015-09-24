package com.falcon.facekall.av;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.falcon.HTTP.FLFriendHttp;
import com.falcon.HTTP.FLHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.tencent.avsdk.c2c.HttpProcessor;

import org.json.JSONException;
import org.json.JSONObject;

public class AddFriendActivity  extends MyBaseActivity {
	private final static String TAG = AddFriendActivity.class.getSimpleName();
	private  RelativeLayout mRLSearchUserResult;
	private EditText mEVSearchUserName;
	private  TextView mTVFriendName;
	private  StringBuilder mStrRetMsg;
	FLFriendHttp mHttp=new FLFriendHttp();
    private String targetUid = "D6E27747A02B4D98ADD31B3EEDCD649A";

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		initView();	
	}
	
	public void initView() {	
		mEVSearchUserName = (EditText) findViewById(R.id.et_search_user);
		mRLSearchUserResult = (RelativeLayout) findViewById(R.id.rl_search_user_result);
		mTVFriendName = (TextView)findViewById(R.id.tv_friend_name);	
		mStrRetMsg = new StringBuilder();		
	}	
		
	public void onBack(View view){	
		finish();
	}
	public void onSearchFriend(View view){
//		final String friendName = mEVSearchUserName.getText().toString();
//		if(friendName.isEmpty()){
//			Toast.makeText(this, "请输入要查找的用户", Toast.LENGTH_SHORT).show();
//			return ;
//		}
//		new Thread( new Runnable(){
//			@Override
//			public void run() {
//				final int iRet = HttpProcessor.doRequestGetInfo(friendName, mStrRetMsg);
//				runOnUiThread(new Runnable() {
//				public void run(){
//				if(iRet == 0){
//					Log.d(TAG,"get usrinfo succ:" + friendName);
//					mTVFriendName.setText(friendName);
//					mRLSearchUserResult.setVisibility(View.VISIBLE);
//					Button btn = (Button)AddFriendActivity.this.findViewById(R.id.btn_add_friend);
//					btn.setClickable(true);
//					btn.setText("添加");
//				}else{
//					Toast.makeText(getBaseContext(), "查询异常:" + iRet + ":" + mStrRetMsg, Toast.LENGTH_SHORT).show();
//				}
//				}
//			});
//			}
//
//		}).start();
//        //自己服务器的搜索
//        mHttp.searchUserList(friendName, "1", "10", new FLHttpListener() {
//            @Override
//            public void onHttpDone(JSONObject result) throws JSONException {
//                Log.e("Login-result", result.toString());
//                try {
//                    if(result.getString("r").equals(FLHttp.RESULTSUCCESS)){
//
//                    }else{
//                        return;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

	}

	public void onAddFriend(View view){
		final String friendName = mTVFriendName.getText().toString();
		new Thread( new Runnable(){
			@Override
			public void run() {
				final int iRet = HttpProcessor.doRequestAddFriend(FacekallApplication.getInstance().getUserInfo().getPhone(), friendName, mStrRetMsg);
				runOnUiThread(new Runnable() {
				public void run(){
				if(iRet == 0){
					Log.d(TAG,"add friend succ:" + friendName);
					mTVFriendName.setText(friendName);
					Button btn = (Button)AddFriendActivity.this.findViewById(R.id.btn_add_friend);
					btn.setClickable(false);
					btn.setText("已添加");
					mRLSearchUserResult.setVisibility(View.VISIBLE);										
				}else {					
					Toast.makeText(getBaseContext(), "im添加好友失败:" + iRet + ":" + mStrRetMsg, Toast.LENGTH_SHORT).show();
				}
				}
			});		
			}
			
		}).start();
        //自己服务器的添加好友
        String uid = FacekallApplication.getInstance().getUserInfo().getUID();
        Log.e(TAG,"result-UID"+uid);
        mHttp.addFriend(uid, targetUid, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) throws JSONException {
                try {
                    if(result.getString("r").equals(FLHttp.RESULTSUCCESS)){
                        Toast.makeText(getBaseContext(), "service添加好友成功:" +result.toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getBaseContext(), "service添加好友失败:" + result.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

			@Override
			public void onHttpErr(String err){

			}
        });

	}

}
