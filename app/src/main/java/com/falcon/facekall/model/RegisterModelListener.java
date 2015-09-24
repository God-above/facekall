package com.falcon.facekall.model;

import com.duowan.mobile.netroid.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2015/7/24 0024.
 */
public interface RegisterModelListener {
    public void onRegisterErr(String err);
    public void onRegisterDone(JSONObject obj) throws JSONException;
}
