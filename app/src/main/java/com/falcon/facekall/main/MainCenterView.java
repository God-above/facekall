package com.falcon.facekall.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.falcon.facekall.R;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class MainCenterView extends LinearLayout {

    private DigitalClock digitalClock;
    private ImageView centerLeft,centerRight;

    public MainCenterView(Context context) {
        super(context);
    }

    public MainCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_maincenter, this);
        initView();
    }

    private void initView() {
        digitalClock = (DigitalClock)findViewById(R.id.dc_center_time);
        centerLeft = (ImageView) findViewById(R.id.center_left_arrow);
        centerRight = (ImageView) findViewById(R.id.center_right_arrow);
    }

    public void changeArrow(boolean isLeft){
        if(isLeft){
            centerLeft.setImageResource(R.drawable.tab_setting);
            centerRight.setImageResource(R.drawable.tab_setting_pressed);
        }else{
            centerLeft.setImageResource(R.drawable.tab_setting_pressed);
            centerRight.setImageResource(R.drawable.tab_setting);
        }
    }

}
