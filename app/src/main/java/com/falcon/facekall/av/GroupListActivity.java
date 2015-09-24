package com.falcon.facekall.av;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface; 
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.falcon.facekall.R;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMValueCallBack;
import com.tencent.avsdk.Utils.GroupEntity;
import com.tencent.avsdk.adapter.GroupListAdapter;
import com.tencent.avsdk.c2c.UserInfoManager;


public class GroupListActivity  extends MyBaseActivity {
	private final static String TAG = GroupListActivity.class.getSimpleName();
//	private  List<TIMGroupBaseInfo> mListGroup;
	private  List<GroupEntity> mListGroup;
	private ListView mLVGroup;
	private Button mBtnCreateGroup;
	public GroupListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouplist);
		initView();	
	}
	
	public void initView() {	
		mBtnCreateGroup = (Button) findViewById(R.id.btn_goto_create_group);
		mLVGroup= (ListView)findViewById(R.id.lv_groups);	
		//mListGroup = new ArrayList<TIMGroupBaseInfo>();
		mListGroup = new ArrayList<GroupEntity>();
		mAdapter = new GroupListAdapter(getBaseContext(),mListGroup);
		mLVGroup.setAdapter(mAdapter);		
	
		mBtnCreateGroup.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GroupListActivity.this,CreateGroupActivity.class));	
				
			} 			
		});
		
		mLVGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {			
				//TIMGroupBaseInfo entity = (TIMGroupBaseInfo) mAdapter.getItem(position);
				GroupEntity entity = (GroupEntity) mAdapter.getItem(position);
			    Intent intent = new Intent(GroupListActivity.this, ChatNewActivity.class);
			    intent.putExtra("chatType", ChatNewActivity.CHATTYPE_GROUP);
    	    	intent.putExtra("groupID", entity.getID());
		    	intent.putExtra("groupName", entity.getName());	 
		    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivity(intent);				
			}
		});
		
	
		mLVGroup.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
		//		final TIMGroupBaseInfo entity = (TIMGroupBaseInfo) mAdapter.getItem(position);
				final GroupEntity entity = (GroupEntity) mAdapter.getItem(position);
			       Dialog alertDialog = new AlertDialog.Builder(GroupListActivity.this). 
			                setTitle("确定退出？"). 
			                setMessage("确定退出该群组吗？"). 			       
			                setNegativeButton("确定", new DialogInterface.OnClickListener() { 
			                     
			                    @Override 
			                    public void onClick(DialogInterface dialog, int which) { 
			                        // TODO Auto-generated method stub  
			                    	TIMGroupManager.getInstance().quitGroup(entity.getID(), new TIMCallBack(){
										@Override
										public void onError(int arg0, String arg1) {
											// TODO Auto-generated method stub
											Log.e(TAG,"quit group error:" + arg0 + ":" + arg1);
										}
			
										@Override
										public void onSuccess() {
										//	loadGroupList();
											Map<String,String> groupID2Name = UserInfoManager.getInstance().getGroupID2Name();
											groupID2Name.remove(entity.getID());
											loadGroupList();
										}
			        					
			        				});			                    	
			                    } 
			                }). 
			                setPositiveButton("取消", new DialogInterface.OnClickListener() { 
			                     
			                    @Override 
			                    public void onClick(DialogInterface dialog, int which) { 
			                        // TODO Auto-generated method stub  
			                    } 
			                }). 			               
			                create(); 
			        alertDialog.show(); 	     
					 

				return true;
			}
		});			
	
	}	


	@Override
	public void onResume() {		
		super.onResume();			
		Log.d(TAG,"activity resume ,refresh list");
		loadGroupList();		
	}	
	
	private void loadGroupList(){ 
	 	mListGroup.clear();
		for(Map.Entry<String, String> entity : UserInfoManager.getInstance().getGroupID2Name().entrySet()){
			GroupEntity tmp = new GroupEntity();
			tmp.setID(entity.getKey());
			tmp.setName(entity.getValue());
			mListGroup.add(tmp);
		}
    	mLVGroup.setVisibility(View.VISIBLE);
    	mAdapter.notifyDataSetChanged();			
       
//        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
//            @Override
//            public void onError(int code, String desc) {
//                Log.e(TAG, "get gruop list failed: " + code + " desc");
//            }
//      
//			@Override
//			public void onSuccess(List<TIMGroupBaseInfo> arg0) {
//				// TODO Auto-generated method stub
//
// 	           	mListGroup.clear();
//	            mListGroup.addAll(arg0); 
//            	mLVGroup.setVisibility(View.VISIBLE);
//            	mAdapter.notifyDataSetChanged();				
//				//缓存群列表。生成群id和群名称的对应关系
//				Map<String,String> mGroup = new HashMap<String,String>();
//				for(TIMGroupBaseInfo baseInfo :arg0){
//					mGroup.put(baseInfo.getGroupId(),baseInfo.getGroupName());
//				}
//				UserInfoManager.getInstance().setGroupID2Name(mGroup);
//				            	
//			}
//        });
        
	}
	
	public void onBack(View view){	
		finish();
	}
	

}
