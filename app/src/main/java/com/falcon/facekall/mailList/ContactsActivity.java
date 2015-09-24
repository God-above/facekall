package com.falcon.facekall.mailList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.av.AddFriendActivity;
import com.falcon.facekall.av.ChatNewActivity;
import com.falcon.facekall.av.GroupListActivity;
import com.tencent.avsdk.Utils.ChnToSpell;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.adapter.ContactsListAdapter;
import com.tencent.avsdk.c2c.HttpProcessor;
import com.tencent.avsdk.c2c.UserInfo;
import com.tencent.avsdk.c2c.UserInfoManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends Activity {
	private static final String TAG = ContactsActivity.class.getSimpleName();
	private  List<UserInfo> contactList;
	private TextView tvSystemNews;

	private ContactsListAdapter adapter;
	private static StringBuilder mStrRetMsg = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		contactList = new ArrayList<UserInfo>();
		initView();
	}


	public void initView() {
		ListView lvContacts = (ListView) findViewById(R.id.list_contacts);
		tvSystemNews = (TextView) findViewById(R.id.tv_system_news);
		Button btAddFriend = (Button) findViewById(R.id.btn_goto_add_friend);
		btAddFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ContactsActivity.this, AddFriendActivity.class));
			}
		});

		loadContactsContent();
		adapter = new ContactsListAdapter(ContactsActivity.this,contactList);
		lvContacts.setAdapter(adapter);		
		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final UserInfo entity = (UserInfo) adapter.getItem(position);
				if (entity.getName() == Constant.GROUP_USERNAME) {
					Intent intent = new Intent(ContactsActivity.this, GroupListActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(ContactsActivity.this, ChatNewActivity.class);
					intent.putExtra("chatType", ChatNewActivity.CHATTYPE_C2C);
					intent.putExtra("userName", entity.getName());
					startActivity(intent);
				}
			}
		});
	}	
	
	private void  loadContactsContent()
	{			
		contactList.clear();	
		Map<String, UserInfo> users = UserInfoManager.getInstance().getContactsList();
		if(users == null){
			Log.e(TAG,"users null!");
			return;
		}
		for(Map.Entry<String,UserInfo> entry: users.entrySet())	{
			contactList.add(entry.getValue());				 
		}
		
		Collections.sort(contactList, new Comparator<UserInfo>() {
			@Override
			public int compare(final UserInfo user1, final UserInfo user2) {
				String str1, str2;
				str1 = (user1.getNick() == null ? user1.getName() : user1.getNick());
				str2 = (user2.getNick() == null ? user2.getName() : user2.getNick());
				//  PingYinUtil.getPingYin(str1).toLowerCase().compareTo(  PingYinUtil.getPingYin(str2).toLowerCase() );
				return ChnToSpell.MakeSpellCode(str1, ChnToSpell.TRANS_MODE_PINYIN_INITIAL).toLowerCase()
						.compareTo(ChnToSpell.MakeSpellCode(str2, ChnToSpell.TRANS_MODE_PINYIN_INITIAL).toLowerCase());
			}
		});	
			
		 UserInfo groupUsers = new UserInfo();                  
         groupUsers.setName(Constant.GROUP_USERNAME);
         groupUsers.setNick(Constant.GROUP_NICK);         
         contactList.add(0, groupUsers);		
	}



	@Override
	public void onResume() {		
		super.onResume();
		getContactsFromServer();
	}

	 private void getContactsFromServer(){

	 }

	//按钮点击事件
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final Intent intent=new Intent();
		switch (v.getId()) {
			case R.id.tv_system_news:
				intent.setClass(ContactsActivity.this,NewsManageActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_remark_list:
				intent.setClass(ContactsActivity.this,RemarkListActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_goto_add_friend:
				intent.setClass(ContactsActivity.this,AddFriendActivity.class);
				startActivity(intent);
				break;
			case R.id.tv_contacts_back:
				finish();
			default:
				break;
		}
	}

}
