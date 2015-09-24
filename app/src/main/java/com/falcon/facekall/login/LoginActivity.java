package com.falcon.facekall.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.falcon.HTTP.FLHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.main.MainActivity;
import com.falcon.facekall.model.LoginModel;
import com.falcon.facekall.model.LoginModelListener;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMValueCallBack;
import com.tencent.av.sdk.AVConstants;
import com.tencent.avsdk.Utils.CommonHelper;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.c2c.HttpProcessor;
import com.tencent.avsdk.c2c.UserInfoManager;
import com.tencent.avsdk.control.QavsdkControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class LoginActivity extends Activity implements LoginModelListener {

    private static final String TAG = "LoginActivity";
    public static final int REQUESTCODESETPSW = 1000;
    //�����������
    private LoginPasswordView m_viewPassword;
    //������֤�����
    private LoginPINView m_viewPIN;
    //�ֻ��
    private EditText edPhone;

    //��ǰ����ʾ������滹����֤�����
    private  boolean  m_showPIN;
    //�Ƿ���ֵ�½�����е��о�����������
    private boolean isError;

    private FacekallApplication application;

    private int mLoginErrorCode = AVConstants.AV_ERROR_OK;
    boolean mBLogin=false;
    private StringBuilder mStrRetMsg,strUserSig;
    private String mPhone,mPassword;

    private LoginError loginError;
    private View mBackview;//����popupwindown�ı���
    private View.OnClickListener itemsOnClick;

    private LoginModel mLoginModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        refreshView();
    }

    //��ť����¼�
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.btnLogin:
                if(isError){
                    intent.setClass(LoginActivity.this, RegisterSetPassActivity.class);
                    LoginActivity.this.startActivityForResult(intent,REQUESTCODESETPSW);
                    return;
                }

                mPhone = edPhone.getText().toString();
                mPassword = m_viewPassword.getPassword();
                onLogin(mPhone,mPassword);
                break;
            case R.id.btnRegister:
                //��ע��ҳ�沢�رյ�ǰҳ��
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
            case R.id.btnCode:
                m_showPIN=true;
                refreshView();
                break;
            case R.id.btnPassword:
                m_showPIN=false;
                isError = false;
                refreshView();
                break;
            case R.id.btnLoginError:
                isError=true;
                loginError.showPopMenu(LoginActivity.this, itemsOnClick);
                break;
            default:
                break;
        }
    }

    //��ʼ����Ϣ
    private void initData(){
        mLoginModel=new LoginModel(this);
        m_showPIN=false;
        application = (FacekallApplication) this.getApplicationContext();
    }
    //��ʼ��������Ϣ
    private void initView(){
        setContentView(R.layout.activity_login);

        mBackview = findViewById(R.id.loginScollView);
        m_viewPassword=(LoginPasswordView)findViewById(R.id.viewPassword);
        m_viewPIN=(LoginPINView)findViewById(R.id.viewPIN);
        edPhone= (EditText) findViewById(R.id.etPhone);
        initPopupWindown();
    }

    private void initPopupWindown() {
        loginError = new LoginError();
        loginError.showOnView = mBackview;
        itemsOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.popLoginPINNO:
                        Toast.makeText(LoginActivity.this,"联系客服",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.popLoginPINYES:
                        m_showPIN = true;//�ܽ��յ����ž�ʹ����֤���½
                        refreshView();
                        loginError.mpopupWindow.dismiss();
                        break;
                }
            }
        };
    }

    //ˢ�½���
    private  void refreshView(){
        if(m_showPIN==false) {
            //��ʾ�������
            m_viewPassword.setVisibility(View.VISIBLE);
            m_viewPIN.setVisibility(View.GONE);
        }else {
            //��ʾ��֤�����
            m_viewPassword.setVisibility(View.GONE);
            m_viewPIN.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUESTCODESETPSW:
                Toast.makeText(LoginActivity.this,"�������óɹ��ص�LoginActivty",Toast.LENGTH_SHORT).show();
                mPhone = edPhone.getText().toString();
                mPassword = m_viewPassword.getPassword();
                onLogin(mPhone,mPassword);
                break;
        }
    }

    public void onLogin(final String mStrUserName,final String mStrPassWord) {
        //�Լ��������ĵ�¼

        Log.e("resultmStrUserName",mStrUserName);
        Log.e("resultmStrPassWord",mStrPassWord);
        mLoginModel.login(mStrUserName,mStrPassWord);
    }




    //��¼�ص�
    public void onLoginErr(String err){
        Toast.makeText(this,err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginDone(JSONObject result) throws JSONException {
        FacekallApplication.getInstance().setUserInfo(result);

        final Intent intent=new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }
}
