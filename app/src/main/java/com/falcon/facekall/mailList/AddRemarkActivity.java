package com.falcon.facekall.mailList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.falcon.facekall.R;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class AddRemarkActivity extends Activity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_remark);
        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview_add_remark);
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.tv_add_remark_back:
                finish();
                break;
            case  R.id.tv_add_remark_save:
                break;
            default:
                break;
        }
    }
}
