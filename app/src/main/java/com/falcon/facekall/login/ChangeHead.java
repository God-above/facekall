package com.falcon.facekall.login;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.falcon.facekall.R;


public class ChangeHead {
	public PopupWindow mpopupWindow;
	public View showOnView;
	public OnClickListener onClickListener;

	public void showPopMenu(Context context, OnClickListener itemsOnClick) {
		if (mpopupWindow != null) {
			mpopupWindow.dismiss();
			mpopupWindow = null;
		}

		View view = View.inflate(context, R.layout.popup_change_head, null);
		Button btnCamera = (Button) view.findViewById(R.id.popHeadCamera);
		Button btnGallery = (Button) view.findViewById(R.id.popHeadGallery);

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mpopupWindow.dismiss();
			}
		});
		btnCamera.setOnClickListener(itemsOnClick);
		btnGallery.setOnClickListener(itemsOnClick);

		if (mpopupWindow == null) {
			mpopupWindow = new PopupWindow(context);
			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(true);
		}

		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(showOnView, Gravity.TOP, 0, 0);
		mpopupWindow.update();
	}
	public void dissMissPop(){
		mpopupWindow.dismiss();
	}

}
