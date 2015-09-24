package com.tencent.avsdk.Utils;


import com.falcon.facekall.FacekallApplication;

public class Constant {
		public static final String NEW_FRIENDS_USERNAME = "system_new_friends";
		public static final String NEW_FRIENDS_NICK = "新的朋友";
		public static final String GROUP_USERNAME = "system_group_container";
		public static final String INVITE_FRIEND_TO_GROUP = "system_invite_friend_to_group";
		public static final String DELETE_GROUP_MEMBER = "system_delete_group_member";
		public static final String GROUP_NICK = "群聊";
		public static final int RECENT_MSG_NUM = 500;		
		public static String OPEN_APPID = "1104620500";
		public static int SDK_APPID = 1104620500;
		public static String ACCOUNT_TYPE = "107";
		public static final int MAX_GROUP_NAME_LENGTH=30;
		public static final int MAX_GROUP_MEMBER_NUM=2000;
		public static final int MAX_TEXT_MSG_LENGTH=1*1024;
		
		public static final int TEXT_MSG_FAILED_FOR_TOO_LOGNG = 85;
		public static final int SEND_MSG_FAILED_FOR_PEER_NOT_LOGIN = 6011;
		public static final int MIN_VOICE_RECORD_TIME = 1000;
		
		public static final String myDEMO_NEW_MESSAGE = "com.example.mydemo.new_message";
		public final static String BASE_URL = "http://203.195.198.121/index.php/Home/User/";
		public final static String BASE_URL_LIST = "http://203.195.198.121/index.php/Home/List/";		
		public static String TH_IMG_CACHE_DIR  = FacekallApplication.getInstance().getFilesDir().getAbsolutePath() + "/TH_IMG/";
		public static String ORG_IMG_CACHE_DIR  = FacekallApplication.getInstance().getFilesDir().getAbsolutePath() + "/ORG_IMG/";
		public static final int THUMB_IMG_MAX_HEIGHT=198;
		public static final int THUMB_IMG_MAX_WIDTH=66;
}
