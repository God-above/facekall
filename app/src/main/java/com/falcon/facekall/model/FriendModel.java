package com.falcon.facekall.model;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.falcon.HTTP.FLFriendHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.IMHttp;
import com.falcon.HTTP.IMHttpListener;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by falcon on 2015/7/28.
 */
public class FriendModel {
    private final String TAG="FriendModel";
    private static FriendModel instance=null;
    private String mMyPhone= FacekallApplication.getInstance().getUserInfo().getPhone();
    private String mMyUID=FacekallApplication.getInstance().getUserInfo().getUID();
    private String mMyID=FacekallApplication.getInstance().getUserInfo().getID();

    private String mFriendID;
    private String mFriendUID;
    private String mFriendPhone;

    private FLFriendHttp mHttp=new FLFriendHttp();
    private FLHttpListener mListener;
    private IMHttp mIM=new IMHttp();

    public static FriendModel getInstance() {
        if(instance==null){
            instance=new FriendModel();
        }

        return instance;
    }

    public void searchUserInfo(String friendUID,FLHttpListener listener){
        this.mListener=listener;
        mHttp.queryFriendInfo(friendUID, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) throws JSONException {
                mListener.onHttpDone(result);
            }

            @Override
            public void onHttpErr(String err) {

            }
        });
    }

    public void getFriends(FLHttpListener listener){
        this.mListener=listener;
        mHttp.queryFriendList(mMyUID, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) throws JSONException {
                mListener.onHttpDone(result);
            }

            @Override
            public void onHttpErr(String err) {

            }
        });
    }

    public void addFriend(String friendUID,String friendPhone,FLHttpListener listener) {
        this.mFriendUID=friendUID;
        this.mFriendPhone=friendPhone;
        this.mListener=listener;

        addFriendFromMyServer();
    }

    public void delFriend(String friendID,String friendPhone,FLHttpListener listener) {
        this.mFriendID=friendID;

        this.mFriendPhone=friendPhone;
        this.mListener=listener;

        delFriendFromMyServer();
    }

    private void addFriendFromMyServer(){
        mHttp.addFriend(mMyUID, mFriendUID, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) throws JSONException {
                Log.d(TAG, result.toString());
                String status = result.getString("r");
                if (status.equals("0001")) {
                    addFriendFromIM();
                } else {
                    mListener.onHttpErr(result.getString("m"));
                }
            }

            @Override
            public void onHttpErr(String err) {

            }
        });
    }

    private void addFriendFromIM(){
        Log.d(TAG,"addFriendFromIM");
        mIM.addFriend(mMyPhone, mFriendPhone, new IMHttpListener() {
            @Override
            public void onIMHttpErr(String err) {
                Log.d(TAG,err);
            }

            @Override
            public void onIMHttpDone(JSONObject result) throws JSONException {
                Log.d(TAG,result.toString());

                int code=result.getInt("ret_code");
                if(code==0){
                    //添加成功,获取这个用户的信息
                    getFriendInfo();
                }else{
                    mListener.onHttpErr(result.getString("ret_msg"));
                }
            }
        });
    }

    private  void getFriendInfo(){
        mHttp.queryFriendInfo(mFriendUID, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) {
                try {
                    mListener.onHttpDone(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ;

            @Override
            public void onHttpErr(String err) {

            }
        });

    }


    private void delFriendFromMyServer(){
        mHttp.delFriend(mFriendID, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) throws JSONException {
                Log.d(TAG, result.toString());
                String status = result.getString("r");
                if (status.equals("0001")) {
                    delFriendFromIM();
                } else {
                    mListener.onHttpErr(result.getString("m"));
                }
            }

            @Override
            public void onHttpErr(String err) {

            }
        });
    }

    private void delFriendFromIM(){
        Log.d(TAG,"addFriendFromIM");
        mIM.delFriend(mMyPhone, mFriendPhone, new IMHttpListener() {
            @Override
            public void onIMHttpErr(String err) {
                Log.d(TAG, err);
            }

            @Override
            public void onIMHttpDone(JSONObject result) throws JSONException {
                Log.d(TAG, result.toString());

                int code = result.getInt("ret_code");
                if (code == 0) {
                    //添加成功,获取这个用户的信息
                    mListener.onHttpDone(result);
                } else {
                    mListener.onHttpErr(result.getString("ret_msg"));
                }
            }
        });
    }
}
