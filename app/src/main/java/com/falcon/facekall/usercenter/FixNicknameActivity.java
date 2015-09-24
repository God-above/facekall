package com.falcon.facekall.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.falcon.facekall.R;
import com.falcon.facekall.login.LoginActivity;
import com.falcon.facekall.login.RegisterPINActivity;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class FixNicknameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            default:
                break;
        }
    }
}
