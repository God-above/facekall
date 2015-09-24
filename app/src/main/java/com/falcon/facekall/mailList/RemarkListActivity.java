package com.falcon.facekall.mailList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.falcon.facekall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class RemarkListActivity extends Activity {
    private ListView remarkList;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tag_list);
        initView();
        initData();
    }

    private void initData() {//先来点假数据
        list = new ArrayList<String>();
        list.add("前男友");
        list.add("现男友");
        remarkList.setAdapter(new ArrayAdapter<String>(RemarkListActivity.this,
                android.R.layout.simple_expandable_list_item_1, list));
        remarkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RemarkListActivity.this,AddRemarkActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        remarkList = (ListView) findViewById(R.id.listview_remark_list);
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.tv_tag_list_back:
                finish();
                break;
            case R.id.ic_tag_list_add:
                intent.setClass(RemarkListActivity.this,AddRemarkActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
