package com.falcon.facekall;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.login.LoginActivity;
import com.falcon.facekall.main.MainActivity;
import com.falcon.facekall.model.FriendModel;
import com.falcon.facekall.model.LoginModel;
import com.falcon.facekall.model.LoginModelListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomActivity extends Activity implements LoginModelListener {
    private LoginModel mLoginModel;


    //test
    private  void test(){


//        FriendModel.getInstance().delFriend("JPNkf+nm-y8xRZcE", "990998", new FLHttpListener() {
//            @Override
//            public void onHttpDone(JSONObject result) throws JSONException {
//                Log.d("test", result.toString());
//            }
//
//            @Override
//            public void onHttpErr(String err) {
//                Log.d("test err", err);
//            }
//        });

//        FriendModel.getInstance().addFriend("7D89FD8B32C64D17A215C6F9861278E0", "990998", new FLHttpListener() {
//            @Override
//            public void onHttpDone(JSONObject result) throws JSONException {
//                Log.d("test",result.toString());
//            }
//
//            @Override
//            public void onHttpErr(String err) {
//                Log.d("test err",err);
//            }
//        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        FacekallApplication.getInstance().init(this);

        mLoginModel=new LoginModel(this);

        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                String phone=FacekallApplication.getInstance().getUserInfo().getPhone();
                String password=FacekallApplication.getInstance().getUserInfo().getPassword();
                if(!TextUtils.isEmpty(phone)){
                    //{"id":"JPNkf+nm-y8xRZcE","uid":"7D89FD8B32C64D17A215C6F9861278E0","createTime":1438012800000,"phone":"990998","token":"","nickname":"user990998","password":"123456"}
                    //{"id":"JPM+Kknm-y8xRZcD","pwd":"123456","createDate":1437926400000,"userId":"72D3B53C566844F19A3D53BC084A5187","name":"user10086","mobile":"10086"}
                    Log.d("auto",FacekallApplication.getInstance().getUserInfo().toJson().toString());
                    //自动登录
                    mLoginModel.login(phone,password);
                }else{
                    final Intent intent=new Intent();
                    intent.setClass(WelcomActivity.this, LoginActivity.class);
                    WelcomActivity.this.startActivity(intent);
                    WelcomActivity.this.finish();
                }

            }
        };
        timer.schedule(task,1000*1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onLoginErr(String err){
        final Intent intent=new Intent();
        intent.setClass(WelcomActivity.this, LoginActivity.class);
        WelcomActivity.this.startActivity(intent);
        WelcomActivity.this.finish();
    }

    public void onLoginDone(JSONObject result){
        Log.d("aa",result.toString());
        FacekallApplication.getInstance().setUserInfo(result);

        final Intent intent=new Intent();
        intent.setClass(WelcomActivity.this, MainActivity.class);
        WelcomActivity.this.startActivity(intent);
        WelcomActivity.this.finish();
    }
}
