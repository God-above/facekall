package com.falcon.facekall.av;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.falcon.facekall.R;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMValueCallBack;
import com.tencent.avsdk.adapter.GroupMemberOperaAdapter;
import com.tencent.avsdk.c2c.UserInfo;
import com.tencent.avsdk.c2c.UserInfoManager;


public class DeleteGroupMemberActivity  extends MyBaseActivity {
	private final static String TAG = DeleteGroupMemberActivity.class.getSimpleName();
	private  List<UserInfo> mListUser;
	private ListView mLVUser;
	private ArrayList<String> mListMember;
	private String mStrGroupID;
	private Button mBtnDeleteMember;
	public GroupMemberOperaAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_to_group);		
		initView();	
	}
	
	public void initView() {		
		TextView tvName = (TextView) findViewById(R.id.chat_name);
		tvName.setText("删除群成员");
		mBtnDeleteMember = (Button) findViewById(R.id.btn_invite_group);
		mLVUser= (ListView)findViewById(R.id.lv_contacts);		
		mListUser = new ArrayList<UserInfo>();
		
		mListMember = getIntent().getStringArrayListExtra("members");
		mStrGroupID = getIntent().getStringExtra("groupID");
		loadContactsContent();
		mAdapter = new GroupMemberOperaAdapter(getBaseContext(),mListUser);		
		mLVUser.setAdapter(mAdapter);
	
		mBtnDeleteMember.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HashMap<Integer,Boolean> bMap = GroupMemberOperaAdapter.getIsSelected();
				ArrayList<String> lUsers = new ArrayList<String>();
				for(Map.Entry<Integer,Boolean> entry: bMap.entrySet()){
					if(entry.getValue()){
						lUsers.add(mListUser.get(entry.getKey()).getName());	
						Log.d(TAG,"selected user:" + mListUser.get(entry.getKey()).getName());						
					}
				}			
				
				if(lUsers.size() >= 1){
					DeleteGroupMember(lUsers);
				}else{
					Toast.makeText(getBaseContext(), "请选择至少一个好友",Toast.LENGTH_SHORT).show();
					return;
				}
			} 			
		});
		
		mLVUser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {               
				GroupMemberOperaAdapter.ViewHolder holder = (GroupMemberOperaAdapter.ViewHolder) view.getTag();
	                holder.bselect.toggle();  	     
	                GroupMemberOperaAdapter.getIsSelected().put(position, holder.bselect.isChecked()); 	               
	            }  
			});
	}	
	
	private void  loadContactsContent()
	{			
		mListUser.clear();
		Map<String, UserInfo> users = UserInfoManager.getInstance().getContactsList();
		if(users == null){
			Log.e(TAG,"users null!");
			return;
		}
		for(Map.Entry<String,UserInfo> entry: users.entrySet())	{
			if(mListMember.contains(entry.getKey())){ 
				mListUser.add(entry.getValue());		
			}
		}
	}
	
	private void DeleteGroupMember(ArrayList<String> list){   
		
		TIMValueCallBack<List<TIMGroupMemberResult>> cb = new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int code, String desc) {
            	finish();
                Log.e(TAG, "delete group member failed: " + code + " desc");
                Toast.makeText(getBaseContext(), "踢人失败:" + code + ":" + desc, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> arg0) {
                Log.d(TAG, "delete group member succ: " + arg0.size());
                finish();	
            }
        };

     //   TIMGroupManager.getInstance().deleteGroupMember(mStrGroupID, list, cb);		
	}
	public void onBack(View view){	
		finish();
	}
	

}
