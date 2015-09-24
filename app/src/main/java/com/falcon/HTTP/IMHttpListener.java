package com.falcon.HTTP;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by falcon on 2015/7/28.
 */
public interface IMHttpListener {
    public void onIMHttpErr(String err);
    public void onIMHttpDone(JSONObject result) throws JSONException;
}
