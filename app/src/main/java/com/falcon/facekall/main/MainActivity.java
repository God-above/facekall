package com.falcon.facekall.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.falcon.HTTP.FLHttpListener;
import com.falcon.Utils.Node.FriendNodes;
import com.falcon.Utils.Node.ParseNodes;
import com.falcon.Utils.Node.UserNode;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.av.AvActivity;
import com.falcon.facekall.model.FriendModel;
import com.falcon.facekall.model.avControl.AVControl;
import com.falcon.facekall.model.avControl.AVControlListener;
import com.nineoldandroids.view.ViewHelper;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMUser;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVConstants;
import com.tencent.avsdk.DialogWaitingAsyncTask;
import com.tencent.avsdk.Utils.AVUtil;
import com.tencent.avsdk.Utils.CommonHelper;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.c2c.HttpProcessor;
import com.tencent.avsdk.c2c.UserInfoManager;
import com.tencent.avsdk.control.QavsdkControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class MainActivity extends FragmentActivity implements AVControlListener{
    private static final String TAG = "MainActivity";

    private FacekallApplication application;
    private AVControl mAvControl;
    private MainTopView topView;
    private MainCenterView centerView;
    private MainBottomView bottomView;
    public DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initData();
        initView();
    }



    @Override
    protected void onResume() {
        super.onResume();
        //bottomView.loadContactsContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAvControl.onDestroy();
    }


    private void initData(){
        //获取好友信息
        getFriendList();
    }

    private void getFriendListDone(){
        mAvControl=new AVControl(this);
    }

    //获取好友信息
    private  void getFriendList(){
        FriendModel.getInstance().getFriends(new FLHttpListener() {
            @Override
            public void onHttpDone(JSONObject result) throws JSONException {
                Log.d("test", result.toString());
                String status = result.getString("r");
                if (status.equals("0001")) {
                    JSONArray list = result.getJSONObject("c").getJSONArray("friendList");
                    List<UserNode> fList = ParseNodes.parseFriendInfo(list);
                    FriendNodes.getInstance().setFriendList(fList);
                    getFriendListDone();
                }
            }

            @Override
            public void onHttpErr(String err) {
                Log.d("test err", err);
            }
        });
    }

    private void initView() {
        centerView = (MainCenterView) findViewById(R.id.centerView);
        bottomView = (MainBottomView) findViewById(R.id.bottomView);
        bottomView.setCenterView(centerView);
        topView = (MainTopView) findViewById(R.id.topView);
        topView.setBottomView(bottomView);
        initDraLayout();

    }

    private void initDraLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);
                ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
                ViewHelper.setScaleX(mContent, rightScale);
                ViewHelper.setScaleY(mContent, rightScale);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }



    private void startAV() {//暂定页面跳转吧。
        Log.e(TAG, "WL_DEBUG onReceive action = " + "startAv");
//        startActivityForResult(
//                new Intent(Intent.ACTION_MAIN)
//                        .putExtra(AVUtil.EXTRA_IDENTIFIER, "123456789")
//                        .putExtra(AVUtil.EXTRA_SELF_IDENTIFIER, FacekallApplication.getInstance().username())
//                        .setClass(this, AvActivity.class),
//                0);
    }



    public void onAVInitErr(String err){

    }

    public void onAVInitDone(){
        Log.d(TAG,"onAVInitDone");
        mAvControl.callFriend("10086");
    }

}
