package com.falcon.facekall.login;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.R;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class LoginPINView extends LinearLayout {
    public TextView txtPIN;
    private Button btnSend;
    private TimeCount time;

    public LoginPINView(Context context) {
        super(context);
    }

    public LoginPINView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_loginpin,this);

        time = new TimeCount(60000, 1000);
        txtPIN=(TextView)findViewById(R.id.txtPIN);
        btnSend=(Button)findViewById(R.id.btnSend);
        //发送验证码
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                time.start();
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSend.setClickable(false);
            btnSend.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            btnSend.setText("重新获取");
            btnSend.setClickable(true);

        }
    }
}
