package com.falcon.HTTP;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public interface FLHttpListener {
    public void onHttpDone(JSONObject result) throws JSONException;
    public void onHttpErr(String err);
}
