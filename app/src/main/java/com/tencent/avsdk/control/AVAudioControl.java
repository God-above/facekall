package com.tencent.avsdk.control;

import com.falcon.facekall.FacekallApplication;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVAudioCtrl.Delegate;
import com.tencent.avsdk.Utils.AVUtil;

import android.content.Context;
import android.content.Intent;

public class AVAudioControl {
	private Context mContext = null;

	private Delegate mDelegate = new Delegate() {
		@Override
		protected void onOutputModeChange(int outputMode) {
			super.onOutputModeChange(outputMode);
			mContext.sendBroadcast(new Intent(AVUtil.ACTION_OUTPUT_MODE_CHANGE));
		}
	};

	AVAudioControl(Context context) {
		mContext = context;
	}

	void initAVAudio() {
		QavsdkControl qavsdk = ((FacekallApplication) mContext)
				.getQavsdkControl();
		qavsdk.getAVContext().getAudioCtrl().setDelegate(mDelegate);
	}

	boolean getHandfreeChecked() {
		QavsdkControl qavsdk = ((FacekallApplication) mContext)
				.getQavsdkControl();
		return qavsdk.getAVContext().getAudioCtrl().getAudioOutputMode() == AVAudioCtrl.OUTPUT_MODE_HEADSET;
	}
}