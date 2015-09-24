package com.falcon.Utils.Node;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class UserNode {
    private  String    mID;
    private  long      mCreateTime;
    private  String    mUid;
    private  String    mPhone;
    private  String    mPassword;
    private  String    mNickname;
    private  String    mToken;

    //av的信息
    public AVUserNode  avNode;

    public void setID(String id){
        this.mID=id;
    }

    public void setCreateTime(long createTime){
        this.mCreateTime=createTime;
    }

    public void setUID(String uid){
        this.mUid=uid;
    }

    public void setPhone(String phone){
        this.mPhone=phone;
    }

    public void setPassword(String password){
        this.mPassword=password;
    }

    public void setNickname(String nickname){
        this.mNickname=nickname;
    }

    public void setToken(String token){
        this.mToken=token;
    }

    public String getUID(){
        return this.mUid;
    }

    public String getID(){
        return this.mID;
    }

    public long getCreateTime(){
        return this.mCreateTime;
    }

    public String getPhone(){
        return this.mPhone;
    }

    public String getPassword(){
        return this.mPassword;
    }

    public String getNickname(){
        return this.mNickname;
    }

    public String getToken(){
        return this.mToken;
    }

    public JSONObject toJson(){
        JSONObject obj=new JSONObject();
        try{
            obj.put("uid",this.mUid);
            obj.put("phone",this.mPhone);
            obj.put("password",this.mPassword);
            obj.put("nickname",this.mNickname);
            obj.put("token",this.mToken);
            obj.put("id",this.mID);
            obj.put("createTime",this.mCreateTime);
        }catch (JSONException e){

        }
        return  obj;
    }

    public  void fromJson(String json){
        try{
            JSONObject obj=new JSONObject(json);
            this.mUid=obj.getString("uid");
            this.mPhone=obj.getString("phone");
            this.mPassword=obj.getString("password");
            this.mNickname=obj.getString("nickname");
            this.mToken=obj.getString("token");
            this.mID=obj.getString("id");
            this.mCreateTime=obj.getLong("createTime");
        }catch (JSONException e){

        }
    }
}
