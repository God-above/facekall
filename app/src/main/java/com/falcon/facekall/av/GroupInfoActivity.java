package com.falcon.facekall.av;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.falcon.facekall.R;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupDetailInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMValueCallBack;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.adapter.GroupMemberAdapter;
import com.tencent.avsdk.c2c.UserInfoManager;
import com.tencent.avsdk.view.MyGridView;


public class GroupInfoActivity  extends MyBaseActivity {
	private final static String TAG = GroupInfoActivity.class.getSimpleName();
	private  ArrayList<String> mListMember;
	private MyGridView mGVMembers;
	private Button mBtnQuitGroup;
	public GroupMemberAdapter mAdapter;
	private TextView memberNum;
	private TextView groupOwner;	
	private String mStrGroupID;
	private String mStrGroupName;
	private TextView groupName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);

		initView();	
	}
	
	public void initView() {	
	
		mGVMembers= (MyGridView)findViewById(R.id.gv_members);
		mListMember = new ArrayList<String>();
		mAdapter = new GroupMemberAdapter(getBaseContext(),mListMember);
		mGVMembers.setAdapter(mAdapter);			
		groupName = (TextView) findViewById(R.id.tv_group_name);	
		memberNum = (TextView) findViewById(R.id.tv_member_num);
		groupOwner = (TextView) findViewById(R.id.tv_group_owner);
		mStrGroupID = getIntent().getStringExtra("groupID");
		mStrGroupName = getIntent().getStringExtra("groupName");
	    
	    mBtnQuitGroup = (Button)findViewById(R.id.btn_quit_group);
	    mBtnQuitGroup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
                TIMGroupManager.getInstance().quitGroup(mStrGroupID, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                    	Toast.makeText(getBaseContext(), "quit group error:" + code + ":" + desc, Toast.LENGTH_SHORT).show();
                    	Log.e(TAG,  "quit group error:" + code + ":" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "quit group succ");
						Map<String,String> groupID2Name = UserInfoManager.getInstance().getGroupID2Name();
						groupID2Name.remove(mStrGroupID);                        
                        finish();
            			runOnUiThread(new Runnable() {
        					public void run(){
//        						Intent intent = new Intent(GroupInfoActivity.this,MainActivity.class);
//        						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        						startActivity(intent);
        				}
        			});	
                    }
                });
			}
	    	
	    });
	    
	    mGVMembers.setOnItemClickListener(new OnItemClickListener() {  
	    	public void onItemClick(AdapterView<?> arg0,
	    	                                  View arg1,
	    	                                  int arg2,
	    	                                  long arg3
	    	                                  ) {   
	    	    String userName =(String) arg0.getItemAtPosition(arg2);
	    	    if(userName == Constant.INVITE_FRIEND_TO_GROUP){
	    	    	Intent intent = new Intent(GroupInfoActivity.this,InviteToGroupActivity.class);
	    	    	intent.putStringArrayListExtra("members", mListMember);
	    	    	intent.putExtra("groupID", mStrGroupID);
	    	    	intent.putExtra("groupName", mStrGroupName);
	    	    	startActivity(intent);
	    	    }
	    	    
	    	    if(userName == Constant.DELETE_GROUP_MEMBER){
	    	    	Intent intent = new Intent(GroupInfoActivity.this,DeleteGroupMemberActivity.class);
	    	    	intent.putStringArrayListExtra("members", mListMember);
	    	    	intent.putExtra("groupID", mStrGroupID);
	    	    	intent.putExtra("groupName", mStrGroupName);
	    	    	startActivity(intent);
	    	    }	    	    
	    	      
	    	}
	    });
	    
	    RelativeLayout rlGroupName = (RelativeLayout) findViewById(R.id.rl_group_name);
	    rlGroupName.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
    	    	Intent intent = new Intent(GroupInfoActivity.this,EditGroupNameActivity.class);    	    	
    	    	intent.putExtra("groupID", mStrGroupID);
    	    	intent.putExtra("groupName", mStrGroupName);
    	    	startActivity(intent);			
			}
	    	
	    });
	}	

	@Override
	public void onResume() {		
		super.onResume();			
		Log.d(TAG,"activity resume ,refresh list");
		loadGroupMember(mStrGroupID);		
	}	
	
	private void loadGroupMember(String groupID){
		List<String> lGroup = new ArrayList<String>();
		lGroup.add(groupID);
        
		TIMGroupManager.getInstance().getGroupDetailInfo(lGroup,new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "get gruop detailinfo failed: " + code + " desc");
            }
      
			@Override
			public void onSuccess(List<TIMGroupDetailInfo> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size() != 1){
					Log.e(TAG,"result size error:" + arg0.size() );
					return;
				}
				TIMGroupDetailInfo info = arg0.get(0);
				groupName.setText(info.getGroupName());
				groupOwner.setText(info.getGroupOwner());
            	memberNum.setText(String.valueOf(info.getMemberNum()) + "/" + Constant.MAX_GROUP_MEMBER_NUM);
            	mGVMembers.setVisibility(View.VISIBLE);
            	mAdapter.notifyDataSetChanged(); 	   
            	mStrGroupName = info.getGroupName();
			}

        });
		
		TIMGroupManager.getInstance().getGroupMembers(groupID,new TIMValueCallBack<List<TIMGroupMemberInfo>>(){

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				  Log.e(TAG, "get gruop member failed: " + arg0 + " arg1");
			}

			@Override
			public void onSuccess(List<TIMGroupMemberInfo> arg0) {
				// TODO Auto-generated method stub
		    	mListMember.clear();   
		    	for(int i=0;i<arg0.size();i++){
		    		mListMember.add(arg0.get(i).getUser());
		    		//Log.d(TAG,"group member:" + arg0.get(i).getUser());
		    	}
            	mListMember.add(Constant.INVITE_FRIEND_TO_GROUP);	
            //	mListMember.add(Constant.DELETE_GROUP_MEMBER);
	        	mGVMembers.setVisibility(View.VISIBLE);
	        	mAdapter.notifyDataSetChanged(); 
			}
			
		});
	}
	
	public void onBack(View view){	
		finish();	
	}
	

}
