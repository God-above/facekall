package com.falcon.facekall.model.avControl;

import android.content.Context;
import android.util.Log;

import com.falcon.facekall.FacekallApplication;
import com.tencent.av.sdk.AVConstants;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomPair;

/**
 * Created by falcon on 2015/7/29.
 */
public class TAVContext {
    private static final String TAG="TAVContext";

    public AVContext mAVContext;
    private AVContext.Config mConfig = null;
    private Context mCx;

    public TAVContext(Context cx,final TAVCallback callback){
        this.mCx=cx;

        String identifier= FacekallApplication.getInstance().getUserInfo().avNode.identifier;
        Log.d(TAG, "identifier:" + identifier);
        String usersig=FacekallApplication.getInstance().getUserInfo().avNode.sig;
        mConfig = new AVContext.Config(AVControl.UID_TYPE,AVControl.APP_ID_TEXT, identifier, usersig, AVControl.APP_ID_TEXT, "");
        mAVContext=AVContext.createContext(mConfig);

        if(mAVContext==null){
            Log.e(TAG, "创建失败");
            callback.OnComplete(-1);
            return;
        }

        mAVContext.startContext(this.mCx, AVRoom.AV_ROOM_PAIR, new AVContext.StartContextCompleteCallback() {
            public void OnComplete(int result) {
                if (result == AVConstants.AV_ERROR_INITSDKFAIL) {
                    callback.OnComplete(-1);
                    return;
                } else {
                    Log.d(TAG, "启动成功");
                    callback.OnComplete(1);
                }

            }
        });
    }

    public void createRoom(AVRoom.Info roomInfo,AVRoomPair.Delegate delegate){
        mAVContext.createRoom(delegate,roomInfo);
    }

    public void onDestroy(){
        mConfig=null;
        mAVContext.closeContext(new AVContext.CloseContextCompleteCallback() {
            public void OnComplete() {

            }
        });
    }


    public static class TAVCallback {
        static final String TAG = "TAVCallback";

        public TAVCallback() {
        }

        protected void OnComplete(int status) {
            Log.d("SdkJni", "CloseRoomCompleteCallback.OnComplete");
        }
    }
}
