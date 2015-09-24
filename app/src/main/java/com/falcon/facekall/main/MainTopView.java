package com.falcon.facekall.main;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.tencent.avsdk.Utils.AVUtil;
import com.tencent.avsdk.control.QavsdkControl;
import com.tencent.avsdk.view.CircleImageView;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class MainTopView extends LinearLayout implements View.OnClickListener{

    private TextView tvCallName,tvCallTime,tvCallHistory;
    private CircleImageView iconPeer;
    private ImageView icSwitchBottom;
    private MainBottomView bottomView;

    private QavsdkControl mQavsdkControl= FacekallApplication.getInstance().getQavsdkControl();
    private Context context;

    public MainTopView(Context context) {
        super(context);
    }

    public MainTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_maintop, this);
        initView();
    }

    private void initView() {
        iconPeer = (CircleImageView) findViewById(R.id.ic_peer_icon);
        iconPeer.setOnClickListener(this);
        findViewById(R.id.tv_call_status).setOnClickListener(this);
        tvCallName = (TextView) findViewById(R.id.tv_call_name);
        tvCallTime = (TextView) findViewById(R.id.tv_call_time);
        tvCallHistory = (TextView) findViewById(R.id.tv_call_history);
        icSwitchBottom = (ImageView) findViewById(R.id.ic_switch_bottom);
        icSwitchBottom.setOnClickListener(this);
        findViewById(R.id.ic_open_menu).setOnClickListener(this);
    }

    public void refreshView(){
//      iconPeer.setImageDrawable();
        tvCallName.setText("");
        tvCallTime.setText("");
        tvCallHistory.setText("");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_switch_bottom:
                changeBottom();
                break;
            case R.id.ic_peer_icon:
                invite(true);
                break;
            case R.id.tv_call_status:
                invite(true);
                break;
            case R.id.ic_open_menu:
                ((MainActivity)context).mDrawerLayout.openDrawer(Gravity.RIGHT);
                ((MainActivity)context).mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                        Gravity.RIGHT);
                break;
        }
    }

    //邀请某人 进行视频
    private void invite(boolean isVideo) {

    }

    public void setBottomView(MainBottomView bottomView){
        this.bottomView = bottomView;
    }

    public void changeBottom(){
        if(bottomView.viewPager.getCurrentItem()==0){
            bottomView.viewPager.setCurrentItem(1);
        }else{
            bottomView.viewPager.setCurrentItem(0);
        }
    }

}
