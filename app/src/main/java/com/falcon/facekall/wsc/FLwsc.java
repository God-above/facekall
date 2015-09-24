package com.falcon.facekall.wsc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import de.tavendo.autobahn.WebSocket.ConnectionHandler;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;

/**
 * Created by Administrator on 2015/7/1 0001.
 */
public class FLwsc extends Handler implements ConnectionHandler {
    private final String TAG = "flwsc";
    //断线重连的时间
    private final int    RECONNECT_TIME=5000;
    //断线重连的code
    private final int    RECONNECT_CODE=1;
    private final int    MESSAGE_CODE=2;

    private  FLwscListener m_listener;

    private String m_wsUrl = "";
    private WebSocketConnection m_wsC= new WebSocketConnection();

    private  String m_cacheBuf="";

    public  FLwsc(String wsUrl,FLwscListener listener) {
        Log("wsUrl:"+wsUrl);
        m_wsUrl=wsUrl;
        m_listener=listener;

        connect();
    }

    /**
     * 连接服务器
     */
    public void connect(){
        try{
            m_wsC.connect(m_wsUrl,this);
        }catch (WebSocketException e){
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public  void sendText(String content){
        if(m_wsC.isConnected()==true) {
            //当前websocket连接成功,直接发送
            m_wsC.sendTextMessage(content);
        }else{
            //先连接到服务,然后发送
            m_cacheBuf=content;
            connect();
        }
    }


    public void Log(String s) {
        android.util.Log.d(TAG, s);
    }

    @Override
    public void onOpen() {
        Log("Status: Connected to " + m_wsUrl);
        //wsC.sendTextMessage( "Hello, world!" );
        //检测是否需要发送缓存数据
        if(m_cacheBuf.length()>0){
            m_wsC.sendTextMessage(m_cacheBuf);
            m_cacheBuf="";
        }
    }

    @Override
    public void onTextMessage(String payload) {
        android.util.Log.d(TAG, "Got echo: " + payload);

        Bundle b = new Bundle();//传输数据
        b.putString("data", payload);

        Message msg = this.obtainMessage();
        msg.what = MESSAGE_CODE;
        msg.setData(b);

        this.sendMessageDelayed(msg,1);
        this.handleMessage(msg);
    }

    @Override
    public void onRawTextMessage(byte[] buf) {
        //toastLog( "Got echo: " + payload );
    }

    @Override
    public void onBinaryMessage(byte[] buf) {
        //toastLog( "Got echo: " + payload );
    }

    @Override
    public void onClose(int code, String reason) {
        Log("Connection lost.");

        //断线重连
        this.sendMessageDelayed(this.obtainMessage(RECONNECT_CODE), RECONNECT_TIME);
    }


    /**
     * 消息处理.
     */
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what){
            case RECONNECT_CODE:
                connect();
                break;
            case MESSAGE_CODE:
                Bundle b = msg.getData();
                String data = b.getString("data");
                m_listener.receTextMessage(data);
                break;
        }
    }
}
