package com.falcon.facekall.mailList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.falcon.facekall.R;

/**
 * Created by Administrator on 2015/7/9 0009.
 * 好友信息--好友设置  加标签
 */
public class FriendsInfoAddRemarkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friend_info_add_remark);

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
