package com.falcon.facekall.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.falcon.facekall.R;


/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class LoginPasswordView extends LinearLayout {
    private TextView txtPassword;
    private EditText etPassword;

    public LoginPasswordView(Context context) {
        super(context);
    }

    public LoginPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater  inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_loginpassword,this);

        txtPassword=(TextView)findViewById(R.id.txtPassword);
        etPassword = (EditText) findViewById(R.id.etPassWord);
    }

    public String getPassword(){
       return  etPassword.getText().toString();
    }
}
