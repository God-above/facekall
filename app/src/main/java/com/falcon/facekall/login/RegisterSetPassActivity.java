package com.falcon.facekall.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.falcon.facekall.R;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class RegisterSetPassActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registerset);
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tvRegisterSetPwdBack:
                finish();
                break;
            case R.id.btnPswSure:
                finish();
                break;
            default:
                break;
        }
    }

}
