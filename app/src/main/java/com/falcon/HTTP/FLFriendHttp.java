package com.falcon.HTTP;

import android.nfc.Tag;
import android.util.Log;

import com.falcon.facekall.FacekallApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by falcon on 2015/7/28.
 */
public class FLFriendHttp extends FLHttp{
    //4.添加好友
    public void addFriend(String uid,String targetUid,FLHttpListener listener){
        //{"c":{"uid":"EF4AB658666441EC94B8DC1CA972A38D","targetUid":"D6E27747A02B4D98ADD31B3EEDCD649A"},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("uid",uid);
            c.put("targetUid",targetUid);

            object.put("c",c);
        }catch (JSONException e) {
        }
        super.post(FacekallApplication.getInstance().HTTP_ADD_FEIEND,object,listener);
    }

    //5.查询好友列表
    public void queryFriendList(String userId,FLHttpListener listener){
        //{"c":{"userId":"EF4AB658666441EC94B8DC1CA972A38D","pagination":1,"pageSize":100},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("userId",userId);
            c.put("pagination",1);
            c.put("pageSize",999999);

            object.put("c",c);
        }catch (JSONException e) {
        }
        super.post(FacekallApplication.getInstance().HTTP_QUERY_FRIDEN_LIST,object,listener);
    }

    //6.删除好友关系
    public void delFriend(String id,FLHttpListener listener){
        //{"c":{"id":"JP5ShdY1UJz7IRcU"},"m":"","n":"","r":"","u":"","v":""}
        JSONObject object = new JSONObject();
        try {
            JSONObject c = new JSONObject();
            c.put("id",id);

            object.put("c",c);
        }catch (JSONException e) {
        }
        Log.d("delFriend",object.toString());
        super.post(FacekallApplication.getInstance().HTTP_DEL_FRIEND,object,listener);
    }


    //3.根据userId获取用户信息
    public void queryFriendInfo(String userId,FLHttpListener listener){
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
}
