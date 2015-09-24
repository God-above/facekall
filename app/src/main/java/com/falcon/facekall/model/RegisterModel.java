package com.falcon.facekall.model;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.HTTP.IMHttp;
import com.falcon.HTTP.IMHttpListener;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.main.MainActivity;
import com.tencent.av.sdk.AVConstants;
import com.tencent.avsdk.c2c.HttpProcessor;
import com.tencent.avsdk.control.QavsdkControl;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2015/7/24 0024.
 */
public class RegisterModel {
    private final String TAG="RegisterModel";
    FLHttpLogin mHttp=new FLHttpLogin();
    IMHttp      mIM=new IMHttp();
    private  RegisterModelListener   mlistener;
    private  String mUsername;
    private  String mNickname;
    private  String mPassword;
    private  String mCode;


    private JSONObject mResult;
    private JSONObject avResult;

    private StringBuilder mStrRetMsg;
    private QavsdkControl mQavsdkControl = FacekallApplication.getInstance().getQavsdkControl();;
    private int mLoginErrorCode = AVConstants.AV_ERROR_OK;

    public  RegisterModel(RegisterModelListener listener){
        this.mlistener=listener;
    }

    public void register(String username,String password,String nickname,String pinCode){
        //测试界面,固定密码
        this.mUsername=username;
        this.mNickname=nickname;
        this.mPassword=password;
        this.mCode=pinCode;

        registerIM();
    }

    //im的注册
    private void registerIM(){
        Log.d("registerIM","start registerIM");
        mIM.register(mUsername,mPassword, new IMHttpListener() {
            @Override
            public void onIMHttpDone(JSONObject result) {
                Log.d("registerIM",result.toString());
                avResult=result;
                try {
                    int code=result.getInt("ret_code");
                    if(code==0){
                        //注册成功
                        registerMyServer();
                    }else{
                        //注册失败
                        mlistener.onRegisterErr(result.getString("ret_msg"));
                    }
                }catch (JSONException e){
                    mlistener.onRegisterErr(e.toString());
                }

            }

            @Override
            public  void onIMHttpErr(String err){
                mlistener.onRegisterErr(err);
            }
        });
    }

    private void registerMyServer(){
        mHttp.register(mUsername, mPassword, mCode, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) {
                Log.e("register_result", result.toString());
                getUserInfo(result);
            }

            @Override
            public void onHttpErr(String err){

            }
        });
    }

    private  void getUserInfo(JSONObject obj){
        try{
            JSONObject c  =  obj.getJSONObject("c");
            String uid=c.getString("userId").toString();
            mHttp.queryUserInfo(uid, new FLHttpListener() {
                @Override
                public void onHttpDone(JSONObject result) {
                    try {
                        JSONObject obj=new JSONObject();
                        obj.put("avServer",avResult);
                        obj.put("myServer",result);
                        mlistener.onRegisterDone(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };

                @Override
                public void onHttpErr(String err){

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
