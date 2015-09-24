package com.falcon.facekall.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.facekall.R;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class RegisterPINActivity extends Activity {
    private String mobileNum;
    private TextView tvNumShow;
    private LoginPINView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobileNum = getIntent().getStringExtra("mobile");
        setContentView(R.layout.activity_registerpin);
        initView();
    }

    private void initView() {
        pinView=(LoginPINView)findViewById(R.id.viewPIN);
        tvNumShow = (TextView) findViewById(R.id.tvRegPINPhone);
        tvNumShow.setText("验证码已发送至："+mobileNum);
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tvRegisterDoneBack:
                finish();
                break;
            case R.id.btnRegister:
                //现在缺少验证码验证接口 进入输入完成页面
                final Intent intent=new Intent();
                String pinCode=pinView.txtPIN.getText().toString();
                if(pinCode.length()>0){
                    intent.setClass(RegisterPINActivity.this, RegisterDoneActivity.class);
                    intent.putExtra("mobile", mobileNum);
                    intent.putExtra("pinCode",pinCode);
                    RegisterPINActivity.this.startActivity(intent);
                    RegisterPINActivity.this.finish();
                }else{
                    Toast.makeText(RegisterPINActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                }

                break;
            default:
                break;
        }
    }
}
