package com.falcon.facekall.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.falcon.HTTP.FLHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.HTTP.IMHttp;
import com.falcon.HTTP.IMHttpListener;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.main.MainActivity;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVConstants;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.c2c.HttpProcessor;
import com.tencent.avsdk.c2c.UserInfoManager;
import com.tencent.avsdk.control.QavsdkControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/24 0024.
 */
public class LoginModel {
    private final String TAG="LoginModel";
    FLHttpLogin mHttp=new FLHttpLogin();
    private  LoginModelListener   mlistener;
    IMHttp mIM=new IMHttp();
    private  String mPhone;
    private  String mPassword;

    private JSONObject mResult;
    private JSONObject avResult;

    public  LoginModel(LoginModelListener listener){
        this.mlistener=listener;
    }

    public void login(String phone,String password){
        //���Խ���,�̶�����
        this.mPhone=phone;
        this.mPassword=password;

        loginMyServer();
    }

    //��¼�����Լ��ķ�����
    private  void loginMyServer(){
        mHttp.login(this.mPhone, this.mPassword, new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) {
                Log.e("Login-result", result.toString());
                try {
                    if (result.getString("r").equals(FLHttp.RESULTSUCCESS)) {
                        mResult = result;
                        loginIMServer();
                    } else {
                        mlistener.onLoginErr(result.getString("m"));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mlistener.onLoginErr(e.toString());
                }
            }

            @Override
            public void onHttpErr(String err){

            }
        });
    }

    //��¼im������
    private void  loginIMServer(){
        mIM.login(mPhone,mPassword, new IMHttpListener() {
            @Override
            public void onIMHttpDone(JSONObject result) {
                Log.d("registerIM",result.toString());
                avResult=result;
                try {
                    int code=result.getInt("ret_code");
                    if(code==0){
                        //注册成功
                        //获取个人信息
                        getUserInfo();
                    }else{
                        //注册失败
                        mlistener.onLoginErr(result.getString("ret_msg"));
                    }
                }catch (JSONException e){
                    mlistener.onLoginErr(e.toString());
                }

            }

            @Override
            public  void onIMHttpErr(String err){
                mlistener.onLoginErr(err);
            }
        });
    }



    private  void getUserInfo(){
        try{
            JSONObject c  =  mResult.getJSONObject("c");
            String uid=c.getString("userId").toString();
            mHttp.queryUserInfo(uid, new FLHttpListener() {
                @Override
                public void onHttpDone(JSONObject result) {
                    try {
                        JSONObject obj=new JSONObject();
                        obj.put("avServer",avResult);
                        obj.put("myServer",result);
                        mlistener.onLoginDone(obj);
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
