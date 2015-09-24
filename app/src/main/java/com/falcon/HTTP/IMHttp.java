package com.falcon.HTTP;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.falcon.facekall.FacekallApplication;
import com.tencent.avsdk.Utils.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by falcon on 2015/7/28.
 */
public class IMHttp{
    private IMHttpListener mlistener;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                {
                    Bundle b = msg.getData();
                    String data = b.getString("data");
                    mlistener.onIMHttpErr(data);
                    break;
                }
                case 1:
                {
                    try{
                        Bundle b = msg.getData();
                        String data = b.getString("data");
                        JSONTokener jsonParser = new JSONTokener(data);
                        JSONObject result = (JSONObject) jsonParser.nextValue();
                        mlistener.onIMHttpDone(result);
                    }catch (JSONException e){

                    }

                    break;
                }
                default:
                    break;
            }
        }
    };


    public void register(final String phone, final String pwd,IMHttpListener listener){
        this.mlistener=listener;
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String strURL = Constant.BASE_URL + "register";
                NameValuePair pair1 = new BasicNameValuePair("name", phone);
                NameValuePair pair2 = new BasicNameValuePair("password", pwd);
                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                pairList.add(pair1);
                pairList.add(pair2);

                try {
                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity( pairList);
                    HttpPost httpPost = new HttpPost(strURL);
                    httpPost.setEntity(requestHttpEntity);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse response = httpClient.execute(httpPost);
                    if (null == response){
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误");

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                    int ret = response.getStatusLine().getStatusCode();
                    if (ret == 200) {
                        HttpEntity entity = response.getEntity();
                        String retSrc = EntityUtils.toString(entity);
                        Log.d("IMHttp",retSrc);

                        Bundle b = new Bundle();//传输数据
                        b.putString("data", retSrc);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }else{
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误:"+ret);

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void login(final String phone, final String pwd,IMHttpListener listener){
        this.mlistener=listener;
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String strURL = Constant.BASE_URL + "login";
                NameValuePair pair1 = new BasicNameValuePair("name", phone);
                NameValuePair pair2 = new BasicNameValuePair("password", pwd);
                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                pairList.add(pair1);
                pairList.add(pair2);

                try {
                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity( pairList);
                    HttpPost httpPost = new HttpPost(strURL);
                    httpPost.setEntity(requestHttpEntity);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse response = httpClient.execute(httpPost);
                    if (null == response){
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误");

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                    int ret = response.getStatusLine().getStatusCode();
                    if (ret == 200) {
                        HttpEntity entity = response.getEntity();
                        String retSrc = EntityUtils.toString(entity);
                        Log.d("IMHttp",retSrc);

                        Bundle b = new Bundle();//传输数据
                        b.putString("data", retSrc);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }else{
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误");

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void addFriend(String myPhone,String friendPhone,IMHttpListener listener){
        this.mlistener=listener;
        final String name=myPhone;
        final String friendname=friendPhone;
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String strURL =  Constant.BASE_URL_LIST + "addfriend";
                NameValuePair pair1 = new BasicNameValuePair("name", name);
                NameValuePair pair2 = new BasicNameValuePair("friendname", friendname);
                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                pairList.add(pair1);
                pairList.add(pair2);

                try {
                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity( pairList);
                    HttpPost httpPost = new HttpPost(strURL);
                    httpPost.setEntity(requestHttpEntity);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse response = httpClient.execute(httpPost);
                    if (null == response){
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误");

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                    int ret = response.getStatusLine().getStatusCode();
                    if (ret == 200) {
                        HttpEntity entity = response.getEntity();
                        String retSrc = EntityUtils.toString(entity);
                        Log.d("IMHttp",retSrc);

                        Bundle b = new Bundle();//传输数据
                        b.putString("data", retSrc);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }else{
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误:"+ret);

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void delFriend(String myPhone,String friendPhone,IMHttpListener listener){
        this.mlistener=listener;
        final String name=myPhone;
        final String friendname=friendPhone;
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String strURL =  Constant.BASE_URL_LIST + "delfriend";
                NameValuePair pair1 = new BasicNameValuePair("name", name);
                NameValuePair pair2 = new BasicNameValuePair("friendname", friendname);
                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                pairList.add(pair1);
                pairList.add(pair2);

                try {
                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity( pairList);
                    HttpPost httpPost = new HttpPost(strURL);
                    httpPost.setEntity(requestHttpEntity);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse response = httpClient.execute(httpPost);
                    if (null == response){
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误");

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                    int ret = response.getStatusLine().getStatusCode();
                    if (ret == 200) {
                        HttpEntity entity = response.getEntity();
                        String retSrc = EntityUtils.toString(entity);
                        Log.d("IMHttp",retSrc);

                        Bundle b = new Bundle();//传输数据
                        b.putString("data", retSrc);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }else{
                        Bundle b = new Bundle();//传输数据
                        b.putString("data", "网络错误:"+ret);

                        Message msg = new Message();
                        msg.what = -1;
                        msg.setData(b);
                        mHandler.sendMessage(msg);
                        return;
                    }
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
