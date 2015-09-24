package com.falcon.facekall.model.avControl;

import android.content.Context;
import android.util.Log;

import com.falcon.facekall.FacekallApplication;
import com.tencent.av.sdk.AVConstants;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVRoom;
import com.tencent.av.sdk.AVRoomPair;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class AVControl {
    private static final String TAG="AVControl";
    public static final String APP_ID_TEXT = "1104620500";
    public static final String UID_TYPE = "107";

    private TAVContext mTContext;
    private TAVInvitation mTInvitaion;

    private Context mCx;
    private AVControlListener  mlistener;

    public AVControl(Context cx){
        this.mCx=cx;
        this.mlistener=(AVControlListener)cx;

        mTContext=new TAVContext(cx,new TAVContext.TAVCallback(){
            protected void OnComplete(int status) {
                if(status==1){
                    Log.d(TAG,"初始化成功");
                    mTInvitaion=new TAVInvitation();
                    mlistener.onAVInitDone();
                }else {
                    mlistener.onAVInitErr("初始化错误");
                }
            }
        });
    }

    public void onDestroy(){
        mTContext.onDestroy();
        mTInvitaion.onDestroy();
    }

    // call friend
    public void callFriend(String avFriendID){
        AVRoom.Info roomInfo = new AVRoom.Info(AVRoom.AV_ROOM_PAIR, 0, 0, AVRoom.AV_MODE_VIDEO ,avFriendID, null, 0);
        mTContext.createRoom(roomInfo,roomDelegate);
    }



    private AVRoomPair.Delegate roomDelegate = new AVRoomPair.Delegate() {
        // 创建房间成功回调
        protected void OnRoomCreateComplete(int result) {
            Log.d(TAG, "OnRoomCreateComplete result = " + result);

            AVRoomPair roomPair = (AVRoomPair) mTContext.mAVContext.getRoom();
            if (roomPair != null && result == AVConstants.AV_ERROR_OK) {
                Log.d(TAG, "OnRoomCreateComplete. roomId = " + roomPair.getRoomId());
            } else {
                Log.e(TAG, "OnRoomCreateComplete. mRoomPair == null");
            }
        }
    };
}
