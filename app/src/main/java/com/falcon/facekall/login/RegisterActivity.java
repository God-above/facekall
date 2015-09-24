package com.falcon.facekall.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.falcon.facekall.R;
import com.falcon.facekall.model.RegisterModel;
import com.falcon.facekall.model.RegisterModelListener;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class RegisterActivity extends Activity {
    private EditText etMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etMobile = (EditText) findViewById(R.id.etRegisterPhone);
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.tvBackRegister:
                finish();
                break;
            case R.id.btnLogin:
                //打开登录页面并关闭当前页面
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                RegisterActivity.this.finish();

                break;
            case R.id.btnRegister:
                //进入输入验证码页面
                String mobile=etMobile.getText().toString();
                if(mobile.length()>0){
                    intent.setClass(RegisterActivity.this, RegisterPINActivity.class);
                    intent.putExtra("mobile",mobile);
                    RegisterActivity.this.startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.tvTerms:
                Toast.makeText(RegisterActivity.this,"同意条款",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
