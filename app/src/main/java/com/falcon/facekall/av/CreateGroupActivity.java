package com.falcon.facekall.av;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//import com.example.mydemo.utils.PingYinUtil;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMUser;
import com.tencent.TIMValueCallBack;
import com.tencent.avsdk.Utils.ChnToSpell;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.adapter.GroupMemberOperaAdapter;
import com.tencent.avsdk.c2c.UserInfo;
import com.tencent.avsdk.c2c.UserInfoManager;


public class CreateGroupActivity  extends MyBaseActivity {
	private final static String TAG = CreateGroupActivity.class.getSimpleName();
	private  List<UserInfo> mListUser;
	private ListView mLVUser;
	private EditText mETGroupName;
	private Button mBtnCreateGroup;
	public GroupMemberOperaAdapter mAdapter;
	public static int HANDLE_MSG_GROUPINFO = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		initView();	
	}
	
	public void initView() {	
		mETGroupName = (EditText) findViewById(R.id.et_groupname);
		mBtnCreateGroup = (Button) findViewById(R.id.btn_create_group);
		mLVUser= (ListView)findViewById(R.id.lv_contacts);		
		mListUser = new ArrayList<UserInfo>();
		
		loadContactsContent();
		mAdapter = new GroupMemberOperaAdapter(getBaseContext(),mListUser);		
		mLVUser.setAdapter(mAdapter);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mBtnCreateGroup.setOnClickListener( new OnClickListener(){

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
				lUsers.add(FacekallApplication.getInstance().getUserInfo().getPhone());
			//	Log.d(TAG,"username:" + MyApplication.getInstance().getUserName() + ":" + MyApplication.getInstance().getUserName().length());
				String strGroupName =  mETGroupName.getText().toString().trim();
				if(strGroupName.isEmpty()){
					Toast.makeText(getBaseContext(), "请输入群名称",Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					byte[] byte_num = strGroupName.getBytes("utf8");
					if(byte_num.length > Constant.MAX_GROUP_NAME_LENGTH){
						Toast.makeText(getBaseContext(), "群名称不能超过" + Constant.MAX_GROUP_NAME_LENGTH + "个字符",Toast.LENGTH_SHORT).show();
						return;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
								
				if(lUsers.size() >= 3){
					createGroup(lUsers);
				}else{
					Toast.makeText(getBaseContext(), "请选择至少两个好友",Toast.LENGTH_SHORT).show();
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
			mListUser.add(entry.getValue());				 
		}
		
		Collections.sort(mListUser, new Comparator<UserInfo>() {
			@Override
			public int compare(final UserInfo user1, final UserInfo user2) {					
				String str1,str2;
				str1= (user1.getNick()==null?user1.getName():user1.getNick());
				str2= (user2.getNick()==null?user2.getName():user2.getNick());
				return ChnToSpell.MakeSpellCode(str1, ChnToSpell.TRANS_MODE_PINYIN_INITIAL)
									.compareTo( ChnToSpell.MakeSpellCode(str2, ChnToSpell.TRANS_MODE_PINYIN_INITIAL) );
			}
		});				
		
	}
	
	private void createGroup(ArrayList<String> list){   

        TIMGroupManager.getInstance().createGroup("Private",list, mETGroupName.getText().toString().trim(),
	        		new TIMValueCallBack<String>() {
	            @Override
	            public void onError(int code, String desc) {
	                Log.e(TAG, "create group failed: " + code + " desc");
	                Toast.makeText(getBaseContext(), "创建群失败:" + code + ":" + desc, Toast.LENGTH_SHORT).show();
	            }
	
	            @Override
	            public void onSuccess(String s) {
	                Log.d(TAG, "create group succ: " + s);
	                 //维护本地群列表
	                Map<String,String> groupID2Name = UserInfoManager.getInstance().getGroupID2Name();						
					groupID2Name.put(s,mETGroupName.getText().toString().trim());
					
					Intent intent = new Intent(CreateGroupActivity.this, ChatNewActivity.class);
				    intent.putExtra("chatType", ChatNewActivity.CHATTYPE_GROUP);		     
				    intent.putExtra("groupID", s);	
			    	intent.putExtra("groupName", mETGroupName.getText().toString().trim());
			    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    startActivity(intent);		
	            }
	        });		
	}
	public void onBack(View view){	
		finish();
	}
	

}
