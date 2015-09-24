package com.falcon.facekall.mailList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.falcon.facekall.R;

/**
 * Created by Administrator on 2015/7/9 0009.
 * 好友信息（已添加后）
 */
public class FriendsInfoAddedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friend_info_added);

    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.tv_friend_info_added_back:
                finish();
                break;
            case R.id.friend_info_added_more:
                break;
            case R.id.tv_video_record:
                break;
            case R.id.tv_message_record:
                break;
            case R.id.tv_weeks_record:
                break;
            case R.id.tv_friend_info_invite_video:
                break;
            case R.id.tv_friend_info_invite_message:
                break;
            default:
                break;
        }
    }
}
