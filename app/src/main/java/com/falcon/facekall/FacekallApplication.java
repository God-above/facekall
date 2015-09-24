package com.falcon.facekall;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.falcon.Utils.Node.ParseAV;
import com.falcon.Utils.Node.ParseNodes;
import com.falcon.Utils.Node.UserNode;
import com.falcon.facekall.wsc.FLwsc;
import com.tencent.avsdk.SDKHelper;
import com.tencent.avsdk.control.QavsdkControl;
import com.tencent.avsdk.data.IMData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class FacekallApplication extends Application {
    private static FacekallApplication instance;
    private  Context mCX;
    private SDKHelper helper = new SDKHelper();
    private QavsdkControl mQavsdkControl = null;

    private IMData imdata = null;

    //websocket信息
    private String WS_SERVER="ws://115.29.168.74:8080/websocket?userId=";
    private FLwsc mWs=null;

    //HTTP地址
    private String HTTP_URL="http://115.29.168.74:8099";
    public String HTTP_REGISTER=HTTP_URL+"/register";
    public String HTTP_LOGIN=HTTP_URL+"/login";
    public String HTTP_GET_USER_INFO=HTTP_URL+"/getUserInfoByUserId";
    public String HTTP_ADD_FEIEND=HTTP_URL+"/addFriend";
    public String HTTP_QUERY_FRIDEN_LIST=HTTP_URL+"/getFriendList";
    public String HTTP_DEL_FRIEND=HTTP_URL+"/delFriend";
    public String HTTP_SEARECH_USER_LIST=HTTP_URL+"/searchUserList";

    //自己服务器的用户名和密码
    private UserNode mUserInfo;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        this.mUserInfo=new UserNode();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public  void init(Context cx){
        this.mCX=cx;

        SharedPreferences sp =mCX.getSharedPreferences("FACEKALL", MODE_PRIVATE);
        String json=sp.getString("Userinfo","");
        this.mUserInfo.fromJson(json);

        mQavsdkControl = new QavsdkControl(this);
        helper.init(this);
        imdata= new IMData(this);
    }

    public UserNode getUserInfo(){
        return this.mUserInfo;
    }


    public void setUserInfo(JSONObject obj){
        try {

            JSONObject c= obj.getJSONObject("myServer").getJSONObject("c").getJSONObject("user");
            this.mUserInfo= ParseNodes.parseUserNode(c);
            String a= obj.getJSONObject("avServer").getJSONObject("ret_data").getString("UserSig");
            if(a != null && a.startsWith("\ufeff")) {
                a =  a.substring(1);
            }

            JSONObject ao=new JSONObject(a);
            this.mUserInfo.avNode= ParseAV.parseAVUserNode(ao);
        }catch (JSONException e){
            Log.e("setUserInfo", e.toString());
        }


        SharedPreferences sp =mCX.getSharedPreferences("FACEKALL", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Userinfo", this.mUserInfo.toJson().toString());

        editor.commit();
    }

    public QavsdkControl getQavsdkControl() {
        return mQavsdkControl;
    }

    public static FacekallApplication getInstance() {
        return instance;
    }

    public boolean isClientStart() {
        return helper.getClientStarted();
    }


    public boolean getSettingNotification(){
        return imdata.getSettingNotification();
    }

    public void setSettingNotification(boolean  bFlag){
        imdata.setSettingNotification(bFlag);
    }

    public boolean getSettingSound(){
        return imdata.getSettingSound();
    }

    public void setSettingSound(boolean  bFlag){
        imdata.setSettingSound(bFlag);
    }

    public boolean getSettingVibrate(){
        return imdata.getSettingVibrate();
    }

    public void setSettingVibrate(boolean  bFlag){
        imdata.setSettingVibrate(bFlag);
    }

}
