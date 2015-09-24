package com.falcon.facekall.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/24 0024.
 */
public interface LoginModelListener {
    public void onLoginErr(String err);
    public void onLoginDone(JSONObject obj) throws JSONException;
}