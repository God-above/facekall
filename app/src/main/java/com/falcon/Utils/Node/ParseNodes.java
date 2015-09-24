package com.falcon.Utils.Node;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by falcon on 2015/7/29.
 */
public class ParseNodes {

    public static UserNode parseUserNode(JSONObject data){
        UserNode node = new UserNode();
        try{
            String id=data.getString("id");
            String pwd=data.getString("pwd");
            long createDate=data.getLong("createDate");
            String userId=data.getString("userId");
            String name=data.getString("name");
            String mobile=data.getString("mobile");
            String token="";//data.getString("token");

            node.setNickname(name);
            node.setPassword(pwd);
            node.setToken(token);
            node.setPhone(mobile);
            node.setUID(userId);
            node.setID(id);
            node.setCreateTime(createDate);
        }catch (JSONException e){

        }
        return node;
    }

    public static List<UserNode> parseFriendInfo(JSONArray data){
        List<UserNode> list=new ArrayList<UserNode>();
        try{
            for (int i=0;i<data.length();i++){
                JSONObject obj=data.getJSONObject(i);
                UserNode node=ParseNodes.parseUserNode(obj);
                list.add(node);
            }
        }catch (JSONException e){

        }

        return list;
    }
}
