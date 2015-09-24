package com.falcon.HTTP;

import android.util.Log;

import com.falcon.facekall.FacekallApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class FLHttpLogin extends FLHttp{


    public void register(String phone,String pwd,String code,FLHttpListener listener){
        //{"c":{"verifyCode":"9527","mobile":"18611867451","pwd":"123456"},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("verifyCode",code);
            c.put("mobile",phone);
            c.put("pwd",pwd);

            object.put("c",c);
        }catch (JSONException e) {

        }
        super.post(FacekallApplication.getInstance().HTTP_REGISTER,object,listener);
    }

    public void login(String phone,String pwd,FLHttpListener listener){
        //{"c":{"mobile":"18611867451","pwd":"123456"},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("mobile",phone);
            c.put("pwd",pwd);

            object.put("c",c);
        }catch (JSONException e) {
        }
        super.post(FacekallApplication.getInstance().HTTP_LOGIN,object,listener);
    }

    //3.根据userId获取用户信息
    public void queryUserInfo(String userId,FLHttpListener listener){
        //{"c":{"userId":"EF4AB658666441EC94B8DC1CA972A38D"},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("userId",userId);

            object.put("c",c);
        }catch (JSONException e) {
        }
        super.post(FacekallApplication.getInstance().HTTP_GET_USER_INFO,object,listener);
    }


    //8.搜索用户（根据手机号，昵称模糊搜索）
    public void searchUserList(String content,String pagination,String pageSize,FLHttpListener listener){
        //{"c":{"content":"1","pagination":1,"pageSize":100},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("content",content);
            c.put("pagination",pagination);
            c.put("pageSize",pageSize);

            object.put("c",c);
        }catch (JSONException e) {
        }
        super.post(FacekallApplication.getInstance().HTTP_SEARECH_USER_LIST,object,listener);
    }


}
