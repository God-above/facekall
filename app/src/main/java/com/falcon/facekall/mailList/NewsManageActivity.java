package com.falcon.facekall.mailList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.falcon.facekall.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class NewsManageActivity extends Activity {
    private ListView listNews;
    private ArrayList<String> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_manage);
        initView();
    }

    private void initView() {
        listNews = (ListView) findViewById(R.id.listview_news);
        list = new ArrayList<String>();
        list.add("sji");
        list.add("lisi");
        list.add("ww");
        ArrayAdapter adapter = new ArrayAdapter<String>(
                NewsManageActivity.this,android.R.layout.simple_expandable_list_item_1,list
        );
        listNews.setAdapter(adapter);
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.tv_news_tag_back:
                finish();
                break;
            case R.id.tv_news_request:
                break;
            case R.id.tv_news_system:
                break;
            default:
                break;
        }
    }
}
