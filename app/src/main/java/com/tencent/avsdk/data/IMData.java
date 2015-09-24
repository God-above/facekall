package com.tencent.avsdk.data;

import android.content.Context;

public class IMData {
	private String KEY_SETTING_NOTIFICATION = "key_setting_notification";
	private String KEY_SETTING_SOUND = "key_setting_sound";
	private String KEY_SETTING_VIBRATE = "key_setting_vibrate";
	private String KEY_USER_NAME = "key_username";
	private String KEY_PASSWORD = "key_pwd";
    private String KEY_TOKEN = "key_token";
	public IMData(Context context){
		SharedPreferencesHelper.init(context);
	}
	
	
    public void setSettingNotification(boolean bFlag) {        	
    	SharedPreferencesHelper.getInstance().saveData(KEY_SETTING_NOTIFICATION, bFlag);
    }

   
    public boolean getSettingNotification() {
    	return (Boolean) SharedPreferencesHelper.getInstance().getData(KEY_SETTING_NOTIFICATION, false);
    }

    public void setSettingSound(boolean bFlag) {
     
    	SharedPreferencesHelper.getInstance().saveData(KEY_SETTING_SOUND, bFlag);
    }

    public boolean getSettingSound() {
    	return (Boolean)SharedPreferencesHelper.getInstance().getData(KEY_SETTING_SOUND, false);
    }

  
    public void setSettingVibrate(boolean bFlag) {
      
    	SharedPreferencesHelper.getInstance().saveData(KEY_SETTING_VIBRATE, bFlag);
    }


    public boolean getSettingVibrate() {   
    	   return (Boolean)SharedPreferencesHelper.getInstance().getData(KEY_SETTING_VIBRATE, false);
    }

  
    public void setUserName(String name) {
    	SharedPreferencesHelper.getInstance().saveData(KEY_USER_NAME, name);
    }


    public String getUserName() {
    	return (String) SharedPreferencesHelper.getInstance().getData(KEY_USER_NAME, "");
    }

  
    public void setPassword(String pwd) {
    	SharedPreferencesHelper.getInstance().saveData(KEY_PASSWORD, pwd); 
    }

    public String getPassword() {
    	return (String) SharedPreferencesHelper.getInstance().getData(KEY_PASSWORD, "");
    }

    public void setToken(String token) {
        SharedPreferencesHelper.getInstance().saveData(KEY_TOKEN, token);
    }

    public String getToken() {
        return (String) SharedPreferencesHelper.getInstance().getData(KEY_TOKEN, "");
    }
	


}
