package com.falcon.facekall.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.falcon.facekall.R;
import com.falcon.facekall.login.LoginActivity;
import com.falcon.facekall.login.RegisterPINActivity;

/**
 * Created by Administrator on 2015/7/9 0009.
 * 电话联系客服界面
 */
public class PhoneWeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_we);

    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.phone_we_back:
                finish();
                break;
            case R.id.call_400:
                Uri uri = Uri.parse("tel:" + "13220114442");
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(uri);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
