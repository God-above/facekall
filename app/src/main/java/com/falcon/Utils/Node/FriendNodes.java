package com.falcon.Utils.Node;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class FriendNodes {
    private List<UserNode> mList=new ArrayList<UserNode>();
    private static FriendNodes instance=null;

    public static FriendNodes getInstance() {
        if(instance==null){
            instance=new FriendNodes();
        }

        return instance;
    }

    public void  setFriendList(List<UserNode> list){
        mList.clear();;
        mList=list;
    }

    public List<UserNode> list(){
        return mList;
    }
}
