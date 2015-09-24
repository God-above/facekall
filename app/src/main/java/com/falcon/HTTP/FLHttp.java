package com.falcon.HTTP;

import android.os.Build;
import android.util.Log;

import com.duowan.mobile.netroid.*;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.request.JsonArrayRequest;
import com.duowan.mobile.netroid.request.JsonObjectRequest;
import com.duowan.mobile.netroid.request.StringRequest;
import com.duowan.mobile.netroid.stack.HttpClientStack;
import com.duowan.mobile.netroid.stack.HttpStack;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class FLHttp {
    private static String TAG="FLHttp";
    private RequestQueue mRequestQueue;
    public static String RESULTSUCCESS = "0001";

    public FLHttp(){
        HttpStack stack;
        String userAgent = "falcon/0";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            stack = new HurlStack(userAgent, null);
        } else {
            stack = new HttpClientStack(userAgent);
        }

        Network network = new BasicNetwork(stack, HTTP.UTF_8);
        mRequestQueue = new RequestQueue(network, RequestQueue.DEFAULT_NETWORK_THREAD_POOL_SIZE, null);
        mRequestQueue.start();
    }

    public void post(String url,JSONObject body,FLHttpListener listener){
        final FLHttpTask task=new FLHttpTask(listener);
        JsonObjectRequest request = new JsonObjectRequest(url, body,
                new Listener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d(TAG,"onSuccess:"+response.toString());
                        try {
                            task.listener.onHttpDone(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mRequestQueue.add(request);
    }
}
