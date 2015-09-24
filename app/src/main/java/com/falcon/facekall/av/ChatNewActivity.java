package com.falcon.facekall.av;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.facekall.R;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMImageElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.avsdk.Utils.ChatEntity;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.Utils.EmojiUtil;
import com.tencent.avsdk.adapter.ChatMsgListAdapter;
import com.tencent.avsdk.adapter.EmojiAdapter;
import com.tencent.avsdk.adapter.EmojiViewPagerAdapter;
import com.tencent.avsdk.c2c.UserInfoManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import android.graphics.Bitmap;

public class ChatNewActivity extends MyBaseActivity implements OnClickListener {	
	private final static String TAG = ChatNewActivity.class.getSimpleName();
	private ListView mLVChatItems;
	private ImageButton mImgBtnEmoji;
	private ImageButton mImgBtnMedioPlus;
	private EditText mETMsgInput;
	private ImageButton mImgBtnVoiceArrow;
	private Button mBtnSendMsg;
	private Button mBtnVoice;
	private RelativeLayout mRLVoice;
	private Button mBtnSendVoice;
	
	private LinearLayout mLLMedia;
	private Button mBtnSendPhoto;
	private Button mBtnToolCamera;
	private Button mBtnSendFile;
	private Button mBtnSendVedio;
	private Button mBtnMsgRemove;
	private Button mBtnGroupMember;
	private ChatMsgListAdapter adapter;
	private LinearLayout mLLemojis;
	private InputMethodManager inputKeyBoard;
	private TIMConversation conversation;
	private String mStrGroupName;
	public static int CHATTYPE_C2C = 0;
	public static int CHATTYPE_GROUP = 1;
	private int mChatType;
	private List<ChatEntity> listChatEntity;
	private File mPhotoFile;
	private String mStrPhotoPath;
	private File mPttFile;
	private long mPttRecordTime;
	
	private final static int FOR_SELECT_PHOTO = 1;
	private final static int FOR_START_CAMERA = 2;
	private final static int FOR_SELECT_FILE = 3;
	
	private MediaRecorder mRecorder = null;
	
	private ViewPager vpEmoji; 
	private ArrayList<View> pageViews;
	private List<EmojiAdapter> emojiAdapters;
	private List<List<String>> emojis;
	private int current = 0;
	private boolean mIsLoading = false;
	private boolean mBMore = true;
	private final int  MAX_PAGE_NUM = 20;
	private int mLoadMsgNum =MAX_PAGE_NUM;
	private ProgressBar mPBLoadData;
	private boolean mBNerverLoadMore = true;
	private String mStrPeerName;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		onInit();
	}

	protected void onInit() {
		mLVChatItems = (ListView)findViewById(R.id.lv_msg_items);
		mImgBtnEmoji = (ImageButton)findViewById(R.id.btn_emoji);
		mImgBtnEmoji.setOnClickListener(this);
		mImgBtnMedioPlus = (ImageButton)findViewById(R.id.btn_media_pls);
		mImgBtnMedioPlus.setOnClickListener(this);
		mETMsgInput = (EditText)findViewById(R.id.et_msg_input);
		mETMsgInput.setOnClickListener(this);
		mImgBtnVoiceArrow = (ImageButton)findViewById(R.id.iv_voice_arrow);
		mImgBtnVoiceArrow.setOnClickListener(this);
		mBtnSendMsg = (Button)findViewById(R.id.btn_send_msg);
		mBtnSendMsg.setOnClickListener(this);
		mBtnVoice = (Button)findViewById(R.id.btn_voice);
		mBtnVoice.setOnClickListener(this);
		mRLVoice = (RelativeLayout)findViewById(R.id.rl_voice);
		mBtnSendVoice = (Button)findViewById(R.id.btn_send_voice);
		
		mLLMedia = (LinearLayout)findViewById(R.id.ll_media);
		mBtnSendPhoto = (Button)findViewById(R.id.btn_send_photo);
		mBtnSendPhoto.setOnClickListener(this);
		mBtnToolCamera = (Button)findViewById(R.id.btn_camera);
		mBtnToolCamera.setOnClickListener(this);
		mBtnSendFile = (Button)findViewById(R.id.btn_send_file);
		mBtnSendFile.setOnClickListener(this);
		mBtnSendVedio = (Button)findViewById(R.id.btn_vedio);
		mBtnSendVedio.setOnClickListener(this);		
		
		mLLemojis = (LinearLayout)findViewById(R.id.ll_emojis);
		mBtnMsgRemove = (Button)findViewById(R.id.btn_msg_remove);
		mBtnGroupMember = (Button)findViewById(R.id.btn_group_members);
		mPBLoadData = (ProgressBar) findViewById(R.id.pb_load_more);
		
		inputKeyBoard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);	

		mChatType = getIntent().getIntExtra("chatType", CHATTYPE_C2C);

		if (mChatType == CHATTYPE_C2C) { 
			mBtnGroupMember.setVisibility(View.GONE);
			mStrPeerName = getIntent().getStringExtra("userName");
			((TextView) findViewById(R.id.chat_name)).setText(mStrPeerName);
			conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mStrPeerName);
		} else {
			mBtnMsgRemove.setVisibility(View.GONE);
			mBtnGroupMember.setVisibility(View.VISIBLE);
			mStrPeerName = getIntent().getStringExtra("groupID");
			mStrGroupName = getIntent().getStringExtra("groupName");
			conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mStrPeerName);
			Log.d(TAG,"group chat, id:" + mStrGroupName + ":" + mStrPeerName +":" + conversation );
			((TextView) findViewById(R.id.chat_name)).setText(mStrGroupName);			
		}
		listChatEntity = new ArrayList<ChatEntity>();
		adapter = new ChatMsgListAdapter(ChatNewActivity.this,listChatEntity);
	    mLVChatItems.setAdapter(adapter);
	    if(mLVChatItems.getCount() > 1){
	    	mLVChatItems.setSelection(mLVChatItems.getCount() - 1);
	    }		
	    
		getMessage();
		SetMessageListener();
		InitViewPager();		
   
		mETMsgInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					mBtnVoice.setVisibility(View.GONE);
					mBtnSendMsg.setVisibility(View.VISIBLE);
					
				} else {
					mBtnVoice.setVisibility(View.VISIBLE);
					mBtnSendMsg.setVisibility(View.GONE);
				}
			}
	
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
	
			@Override
			public void afterTextChanged(Editable s) {
	
			}
		});	
		
		mBtnSendVoice.setOnTouchListener( new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					LayoutParams params = (LayoutParams)mBtnSendVoice.getLayoutParams();
					params.height = (int)(params.height*0.8);
					params.width = (int)(params.width*0.8);
					mBtnSendVoice.setLayoutParams(params);
					startRecording();
					return true;
				case MotionEvent.ACTION_MOVE:
					return true;
				
				case MotionEvent.ACTION_UP:
					params = (LayoutParams)mBtnSendVoice.getLayoutParams();
					params.height = (int)(params.height*1.25);
					params.width = (int)(params.width*1.25);
					mBtnSendVoice.setLayoutParams(params);		
					Log.d(TAG,"stop record");
					if( stopRecording()==false){
						Log.d(TAG,"recording ret false");
						return true;
					}
					
					sendFile(mPttFile.getAbsolutePath(),TIMElemType.Sound);
					return true;
				default:
					return true;
				}
			}
		});
		mLVChatItems.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideMsgIputKeyboard();
				mImgBtnVoiceArrow.setVisibility(View.GONE);
				mRLVoice.setVisibility(View.GONE);
				mLLMedia.setVisibility(View.GONE);
				mETMsgInput.setVisibility(View.VISIBLE);
				return false;
			}
		});	
		
		mLVChatItems.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					Log.d(TAG,view.getFirstVisiblePosition() + ":" + mIsLoading + ":" +mBMore);
					if (view.getFirstVisiblePosition() == 0 && !mIsLoading && mBMore) {
						mPBLoadData.setVisibility(View.VISIBLE);	
						mBNerverLoadMore = false;
						mIsLoading =true;
						mLoadMsgNum += MAX_PAGE_NUM;
						Log.d(TAG,"num:" + mLoadMsgNum);
						getMessage();					
						
//						mIsLoading = false;

					}
					break;
				}			
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mBtnGroupMember.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatNewActivity.this,GroupInfoActivity.class);
				intent.putExtra("groupID", mStrPeerName);
				intent.putExtra("groupName", mStrGroupName);
				startActivity(intent);
			}			
		});
	}
	
	private void InitViewPager() {
		EmojiUtil.getInstace().initData();
		emojis = EmojiUtil.getInstace().mEmojiPageList;
		vpEmoji = (ViewPager) findViewById(R.id.vPagerEmoji);	
		
		pageViews = new ArrayList<View>();	
		View nullView = new View(getBaseContext());
		nullView.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView);

		emojiAdapters = new ArrayList<EmojiAdapter>();
		for (int i = 0; i < emojis.size(); i++) {
			GridView view = new GridView(getBaseContext());
			EmojiAdapter adapter = new EmojiAdapter(getBaseContext(), emojis.get(i));
		//	Log.d(TAG,"InitViewPager:" + emojis.get(i).size());
			view.setAdapter(adapter);
			emojiAdapters.add(adapter);
			view.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {	
					
					String strEmoji = (String) emojiAdapters.get(current).getItem(position);
					if (strEmoji.equals(EmojiUtil.EMOJI_DELETE_NAME)) {
						if(!TextUtils.isEmpty(mETMsgInput.getText())){
							int selection = mETMsgInput.getSelectionStart();
							String strInputText = mETMsgInput.getText().toString();
							if (selection > 0) {
								String strText = strInputText.substring(selection - 1);
								if ("]".equals(strText)) {
									int start = strInputText.lastIndexOf("[");
									int end = selection;
									mETMsgInput.getText().delete(start, end);
									return;
								}
								mETMsgInput.getText().delete(selection - 1, selection);
							}
						}
					}	
					else{
						SpannableString spannableString = EmojiUtil.getInstace().addEmoji(getBaseContext(),strEmoji);
						mETMsgInput.append(spannableString);
					}
				}
			});			
			view.setBackgroundColor(Color.TRANSPARENT);			
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setNumColumns(7);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setPadding(5, 0, 5, 0);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
		}

		View nullView2 = new View(getBaseContext());
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);

		vpEmoji.setAdapter(new EmojiViewPagerAdapter(pageViews));
		vpEmoji.setCurrentItem(1);
		vpEmoji.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				if (arg0 == pageViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						vpEmoji.setCurrentItem(arg0 + 1);
					} else {
						vpEmoji.setCurrentItem(arg0 - 1);
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
		});

				
	}
    
	private boolean isTopActivity()  
    {       
		boolean isTop = false;
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);  
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;          
        if (cn.getClassName().contains(TAG)){  
        	isTop = true;  
        }  
        Log.d(TAG,"is Top Activity:" + isTop);
        return isTop;  
    }  
	
	private TIMMessageListener msgListener = new TIMMessageListener() {
		
		@Override
		public boolean onNewMessages(List<TIMMessage> arg0) {
			// TODO Auto-generated method stub
			Log.d(TAG,"new messge listnener:" + arg0.size());
			if(isTopActivity()){
				for(TIMMessage msg: arg0){	
					if( mStrPeerName.equals(msg.getConversation().getPeer())){
						//如果是群名称修改通知，改标题名称
						for(int i=0;i<msg.getElementCount();i++){								
							TIMElem elem = msg.getElement(i);
							if(elem.getType() == TIMElemType.GroupTips){
								TIMGroupTipsElem tipsElem = (TIMGroupTipsElem)elem; 								
								if(tipsElem.getTipsType() == TIMGroupTipsType.ModifyGroupName){
									mStrGroupName = tipsElem.getGroupName();
									((TextView) findViewById(R.id.chat_name)).setText(mStrGroupName);	
								}									
							}
						}
						getMessage();
						return false;
					//	return true;
					}
				}				
			}
			return false;
		}
     
   }; 
	
	private void SetMessageListener(){
	 //设置消息监听器，收到新消息时，通过此监听器回调
	   TIMManager.getInstance().addMessageListener(msgListener);
	}	

	public void onBack(View view) {
		Log.d(TAG,"finish:" + mStrPeerName);
		finish();
	}

	@Override
	public void onResume() {		
		super.onResume();	
		//群名称可能已经修改
		if(mChatType == CHATTYPE_GROUP){
			if(UserInfoManager.getInstance().getGroupID2Name().containsKey(mStrPeerName)){
				((TextView) findViewById(R.id.chat_name)).setText(UserInfoManager.getInstance().getGroupID2Name().get(mStrPeerName));
	    	}
			
		}
		getMessage();
	}	
		
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		mBtnVoice.setVisibility(View.VISIBLE);
		mBtnSendMsg.setVisibility(View.GONE);	
		
		if(id == R.id.btn_emoji){
			hideMsgIputKeyboard();
			if(	mLLemojis.getVisibility() == View.GONE){
				mLLemojis.setVisibility(View.VISIBLE);				
				mImgBtnEmoji.setImageResource(R.drawable.aio_keyboard);
			}else{
				mLLemojis.setVisibility(View.GONE);
				mImgBtnEmoji.setImageResource(R.drawable.aio_emoji);
			}
			mImgBtnMedioPlus.setImageResource(R.drawable.aio_fold);
			mBtnVoice.setBackgroundResource(R.drawable.aio_voice);
			mETMsgInput.setVisibility(View.VISIBLE);		
			mRLVoice.setVisibility(View.GONE);
			mLLMedia.setVisibility(View.GONE);		
			mImgBtnVoiceArrow.setVisibility(View.GONE);	
		}else if(id == R.id.btn_media_pls){
			hideMsgIputKeyboard();
			mImgBtnVoiceArrow.setVisibility(View.GONE);
			mLLemojis.setVisibility(View.GONE);			
			mRLVoice.setVisibility(View.GONE);
			mETMsgInput.setVisibility(View.VISIBLE);
			if(mLLMedia.getVisibility() == View.GONE){
				mLLMedia.setVisibility(View.VISIBLE);
				mImgBtnMedioPlus.setImageResource(R.drawable.aio_keyboard);
			}else{
				mLLMedia.setVisibility(View.GONE);
				mImgBtnMedioPlus.setImageResource(R.drawable.aio_fold);
			}	
			mImgBtnEmoji.setImageResource(R.drawable.aio_emoji);
			mBtnVoice.setBackgroundResource(R.drawable.aio_voice);			
		}else if(id == R.id.btn_voice){
			hideMsgIputKeyboard();
			mLLemojis.setVisibility(View.GONE);
			mBtnVoice.setVisibility(View.VISIBLE);
			if(mRLVoice.getVisibility() == View.GONE){
				mRLVoice.setVisibility(View.VISIBLE);
				mBtnVoice.setBackgroundResource(R.drawable.aio_keyboard);
				mImgBtnVoiceArrow.setVisibility(View.VISIBLE);		
				mImgBtnVoiceArrow.setImageResource(R.drawable.aio_audio_button_down_selector);
				mETMsgInput.setVisibility(View.GONE);
			}else
			{
				mRLVoice.setVisibility(View.GONE);
				mETMsgInput.setVisibility(View.VISIBLE);
				mImgBtnVoiceArrow.setVisibility(View.GONE);					
				mBtnVoice.setBackgroundResource(R.drawable.aio_voice);				
			}
			mLLMedia.setVisibility(View.GONE);
			mImgBtnEmoji.setImageResource(R.drawable.aio_emoji);
			mImgBtnMedioPlus.setImageResource(R.drawable.aio_fold);
		}else if( id == R.id.et_msg_input){			
			mETMsgInput.setVisibility(View.VISIBLE);
			mLLemojis.setVisibility(View.GONE);
			mRLVoice.setVisibility(View.GONE);
			mLLMedia.setVisibility(View.GONE);
			mImgBtnVoiceArrow.setVisibility(View.GONE);	
			mImgBtnEmoji.setImageResource(R.drawable.aio_emoji);
			mImgBtnMedioPlus.setImageResource(R.drawable.aio_fold);
			mBtnVoice.setBackgroundResource(R.drawable.aio_voice);
		}else if(id == R.id.btn_send_msg){
			mImgBtnEmoji.setImageResource(R.drawable.aio_emoji);
			mImgBtnMedioPlus.setImageResource(R.drawable.aio_fold);
			mBtnVoice.setBackgroundResource(R.drawable.aio_voice);			
			mLLemojis.setVisibility(View.GONE);
			mRLVoice.setVisibility(View.GONE);
			mLLMedia.setVisibility(View.GONE);	
			mImgBtnVoiceArrow.setVisibility(View.GONE);
			String msg = mETMsgInput.getText().toString();
			sendText(msg);
			mETMsgInput.setText("");
		}else if(id == R.id.iv_voice_arrow){
			if(mRLVoice.getVisibility() == View.VISIBLE){
				mImgBtnVoiceArrow.setImageResource(R.drawable.aio_audio_button_up_selector);
				mRLVoice.setVisibility(View.GONE);
			}else {
				mImgBtnVoiceArrow.setImageResource(R.drawable.aio_audio_button_down_selector);
				mRLVoice.setVisibility(View.VISIBLE);				
			}
		}else if(id == R.id.btn_camera){
			startCamera();
		}else if(id == R.id.btn_send_photo){
			selectPhoto();
		}else if(id == R.id.btn_send_file){
			selectFile();
		}
			
	}

	
	private void hideMsgIputKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				inputKeyBoard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}	
	
	private void getMessage()
	{
		Log.d(TAG,"getMessage begin");
		if(conversation == null){
			Log.e(TAG,"conversation null");
			return;
		}
        conversation.getMessage(mLoadMsgNum, null, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int code, String desc) {
                Log.e(TAG, "get msgs failed:" + code + ":"+ desc);
                mPBLoadData.setVisibility(View.GONE);
				mIsLoading = false;
            }

            @Override
            public void onSuccess(List<TIMMessage> msgs) {
            
            	final List<TIMMessage> tmpMsgs = msgs;
            	Log.d(TAG,"getMessage success:" + msgs.size() + "|" + mLoadMsgNum + "|mIsLoading:" + mIsLoading);
            	if(msgs.size()>0){
            		conversation.setReadMessage(msgs.get(0));
            	}
            	if( !mBNerverLoadMore && (msgs.size() < mLoadMsgNum) ){
            		mBMore = false;
            	}
							
				listChatEntity.clear();
				for(int i=0;i<tmpMsgs.size();i++){					
					TIMMessage msg = tmpMsgs.get(i);
					for(int j=0;j<msg.getElementCount();j++){
						if(msg.getElement(j) == null){
							continue;
						}
						ChatEntity entity = new ChatEntity();
						entity.setElem(msg.getElement(j));
						entity.setIsSelf(msg.isSelf());
						entity.setTime(msg.timestamp());
						entity.setType(msg.getConversation().getType());				
						entity.setSenderName(msg.getSender());
						entity.setStatus(msg.status());
						listChatEntity.add(entity);
					}
				}
   
				Collections.reverse(listChatEntity);
             	adapter.notifyDataSetChanged();
             	mLVChatItems.setVisibility(View.VISIBLE);
             	if(mLVChatItems.getCount() > 1){
             		if(mIsLoading){
             			mLVChatItems.setSelection(0);
             		}else{
             			mLVChatItems.setSelection(mLVChatItems.getCount() - 1);
             		}
             	}
             	mIsLoading = false;	
				}
            });
            	
            mPBLoadData.setVisibility(View.GONE);
	}
	
	                  
	private void sendText(String str){
		if(str.length()==0){
			return;
		}
		
		try{
			byte[] byte_num = str.getBytes("utf8");
			if(byte_num.length > Constant.MAX_TEXT_MSG_LENGTH){
				Toast.makeText(getBaseContext(), "消息太长，最多" + Constant.MAX_GROUP_NAME_LENGTH + "个字符",Toast.LENGTH_SHORT).show();
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}		
		TIMMessage msg = new TIMMessage();
	//	msg.addTextElement(str);
		TIMTextElem elem = new  TIMTextElem();
		elem.setText(str);
		int iRet = msg.addElement(elem);
		if(iRet!=0){
			Log.d(TAG,"add element error:" + iRet);
			return;
		}
		Log.d(TAG,"ready send text msg");		
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //服务器返回了错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见附录
            	if( code == Constant.TEXT_MSG_FAILED_FOR_TOO_LOGNG){
            		desc = "消息太长";
            	}else if(code == Constant.SEND_MSG_FAILED_FOR_PEER_NOT_LOGIN){
            		desc = "对方账号不存在或未登陆过！";
            	}
                Log.e(TAG, "send message failed. code: " + code + " errmsg: " + desc);
                Toast.makeText(getBaseContext(), "发送消息失败. code: " + code + " errmsg: " + desc, Toast.LENGTH_SHORT).show();
                getMessage();
            }

			@Override
			public void onSuccess(TIMMessage arg0) {
				Log.e(TAG, "Send text Msg ok");
				getMessage();
				
			}
        });	
        getMessage();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) { 
//			if (requestCode == FOR_START_CAMERA) {			
//					sendFile(mStrPhotoPath,TIMElemType.Image);						
//			} else if (requestCode == FOR_SELECT_PHOTO) {   				
				if(requestCode == FOR_START_CAMERA || requestCode == FOR_SELECT_PHOTO){
				ContentResolver resolver = getContentResolver(); 	
				Cursor cursor = null; 
				try { 
					Uri originalUri = data.getData();
					String[] proj = {MediaStore.Images.Media.DATA}; 

					cursor = resolver.query(originalUri, proj, null, null, null);
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
					cursor.moveToFirst(); 				
					String path = cursor.getString(column_index); 
					Log.d(TAG,"photo path:" + path);	
					sendFile(path,TIMElemType.Image);
				}catch (Exception e) { 
					Log.e(TAG,"Exception:" + e.toString()); 
	
				}finally{  
				    if(cursor != null){  
				    	cursor.close();  
				    }  
				}  
			} else if (requestCode == FOR_SELECT_FILE) {				
				String filePath = null;
				ContentResolver resolver = getContentResolver();
				  Cursor cursor = null;
				Uri originalUri = data.getData(); 				
		       if ("content".equalsIgnoreCase(originalUri.getScheme())) {
		            String[] projection = { "_data" };
		            try{
		          //  Cursor cursor = null;			 
		                cursor = resolver.query(originalUri, projection,null, null, null);
		                int column_index = cursor.getColumnIndexOrThrow("_data");
		                if (cursor.moveToFirst()) {
		                	filePath = cursor.getString(column_index);
		                }
	                }catch(Exception e){  
	                    e.printStackTrace();  
	                }finally{  
	                    if(cursor != null){  
	                        cursor.close();  
	                    }  
	                }  
		            
		        }else if ("file".equalsIgnoreCase(originalUri.getScheme())) {
		        	filePath = originalUri.getPath();
		        }
		       if(filePath != null){
		    	   Log.d(TAG,"ready send file:" + filePath);
		    //   sendFile(filePath,TIMElemType.File);
		       }				
			}
		}
	
	}
	
	private void selectPhoto(){
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent,FOR_SELECT_PHOTO);		
	}
	
	  private String getPhotoFileName() {
           Date date = new Date(System.currentTimeMillis());
           SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
           return dateFormat.format(date) + ".jpg";
      }
		 
	private void startCamera(){
		  String SDState = Environment.getExternalStorageState();
          if (SDState.equals(Environment.MEDIA_MOUNTED)) {
              Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
              try{            	
//            	  mStrPhotoPath = "mnt/sdcard/DCIM/Camera/" + MyApplication.getInstance().getUserName() + getPhotoFileName();
//            	  mPhotoFile = new File(mStrPhotoPath);
//            	  if (!mPhotoFile.getParentFile().exists()) {
//         	    	mPhotoFile.getParentFile().mkdirs();
//            	  }
//            	  if (!mPhotoFile.exists()) {
//            		    mPhotoFile.createNewFile();
//            	   }
//                   intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                   startActivityForResult(intent, FOR_START_CAMERA);
              }catch(Exception e){                          
                      Toast.makeText(getApplicationContext(), "启动失败："+ e.toString(),Toast.LENGTH_LONG).show();
              }                  
          } else {
              Toast.makeText(this, "sd卡不存在", Toast.LENGTH_LONG).show();
          }
	}
	
	private void selectFile(){

	   Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
	    intent.setType("*/*"); 
	    intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, FOR_SELECT_FILE);
	}
	
	private void sendFile(String path,TIMElemType type){
		if(path.length()==0){
			return;
		}			
        //从文件读取数据
        File f = new File(path);
        Log.d(TAG,"file len:" + f.length());
        if(f.length() == 0){
        	Log.e(TAG,"file empty!");
        	return;
        }
        byte[] fileData = new byte[(int) f.length()];
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(f));
            dis.readFully(fileData);
            dis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }     
 
        TIMMessage msg = new TIMMessage();
        try{
	        if(type == TIMElemType.Image){
	        	TIMImageElem elem = new TIMImageElem();
	        	elem.setData(fileData);	   
	        	
	        	BitmapFactory.Options options = new BitmapFactory.Options();  
	            options.inJustDecodeBounds = true;  
	            BitmapFactory.decodeFile(path, options);  	        	
	        	elem.setImageHeight(options.outHeight);
	        	elem.setImageWidth(options.outWidth);
	         	if(0 != msg.addElement(elem)){
	         		Log.e(TAG,"add image element error" );
	         		return; 
	         	}
	        }else if(type == TIMElemType.Sound){
	        	TIMSoundElem elem = new TIMSoundElem();        	
	        	elem.setData(fileData);	
	        	elem.setDuration(mPttRecordTime);
	        	Log.d("TAG","sound  size:" + fileData.length);
	        	if( 0 != msg.addElement(elem)){
	        		Log.e(TAG,"add sound element error" );
	        		return;
	        	}
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"ready send rich msg:" + type);
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
        	//发送消息回调
            @Override
            public void onError(int code, String desc) {
            	//发送消息失败
                //服务器返回了错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见附录
            	
                Log.e(TAG, "send message failed. code: " + code + " errmsg: " + desc);
                if(code == Constant.SEND_MSG_FAILED_FOR_PEER_NOT_LOGIN){
                	desc = "对方账号不存在或未登陆过！";
            	}
                final int err_code = code;
                final String str_desc = desc;
                runOnUiThread(new Runnable() {
                	public void run(){                		
                		Toast.makeText(getBaseContext(), "发送消息失败. code: " + err_code + " errmsg: " + str_desc, Toast.LENGTH_SHORT).show();
                		getMessage();
                	}
                });                
                
            }

            @Override
            public void onSuccess(TIMMessage msg) {
            	//发送消息成功
                Log.e(TAG, "SendMsg ok");
                getMessage();
            }
        });
 
      getMessage();		
	}
	  
    private void startRecording(){  
	    try{  

			File file = new File("record_tmp.mp3") ;
			if(file.exists())
			{
			Log.d(TAG,"file exist");
			file.delete();
			}
	    	mPttFile = File.createTempFile("record_tmp", ".mp3"); 	 
	    	if(mRecorder == null){
		        mRecorder = new MediaRecorder();	        
		        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  	 
		        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  
		        mRecorder.setOutputFile(mPttFile.getAbsolutePath());  	
		        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);  
		        mRecorder.setPreviewDisplay(null);
	            mRecorder.prepare();
            }
	    	mPttRecordTime = System.currentTimeMillis();
	    	mRecorder.setOnErrorListener(new OnErrorListener(){

				@Override
				public void onError(MediaRecorder mr, int what, int extra) {
					// TODO Auto-generated method stub
					stopRecording();
					Toast.makeText(getBaseContext(),"录音发生错误:" + what ,Toast.LENGTH_SHORT).show();
				}
	    		
	    	});
	    	mRecorder.start();
        } catch (IOException e) {  
        	Log.e(TAG,"start record error" + e.getMessage());
        	e.printStackTrace();  
        } catch(Exception  e){
        	Log.e(TAG,"start record error2" + e.getMessage());
        	e.printStackTrace();
        }  
       
    }  
	  
    private boolean stopRecording() {		
		if(mRecorder!=null){	
			mRecorder.setOnErrorListener(null);
			try{
				mRecorder.stop();
	        }catch (IllegalStateException e) {
				Log.e(TAG, "stop Record error:" + e.getMessage());
				mRecorder.release();
		        mRecorder = null;
				return false;
			}catch(Exception e){
				Log.e(TAG, "stop Record Exception:" + e.getMessage());
				mRecorder.release();
		        mRecorder = null;
				return false;
			}
	        mRecorder.release();
	        mRecorder = null;
		}
        mPttRecordTime = System.currentTimeMillis() - mPttRecordTime;
        if(mPttRecordTime < Constant.MIN_VOICE_RECORD_TIME){
        	Toast.makeText(this,"录音时间太短!" ,Toast.LENGTH_SHORT).show();
        	return false;
        }
        mPttRecordTime = mPttRecordTime/1000;	 
		return true;
	}    
    
    //不在当前界面，播放器释放
	@Override
	protected void onPause() {
		super.onPause();
		if(ChatMsgListAdapter.mPlayer!=null){
			ChatMsgListAdapter.mPlayer.stop();
			ChatMsgListAdapter.mPlayer.release();
			ChatMsgListAdapter.mPlayer =null;
		}
	}   
	
	@Override
	public void onDestroy() {
		Log.d(TAG,"onDestroy:" + this.mStrPeerName );
		 TIMManager.getInstance().removeMessageListener(msgListener);
		super.onDestroy();
	}		
	
}
