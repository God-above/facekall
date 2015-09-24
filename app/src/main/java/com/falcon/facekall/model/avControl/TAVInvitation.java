package com.falcon.facekall.model.avControl;

import com.tencent.av.sdk.AVInvitation;

/**
 * Created by falcon on 2015/7/29.
 */
public class TAVInvitation {
    private AVInvitation mAv= new AVInvitation();;

    // 接收方收到邀请通知
    private AVInvitation.Delegate mAVInvitationDelegate = new AVInvitation.Delegate() {
        // 接收方收到邀请通知
        @Override
        protected void onInvitationReceived(String identifier, long roomId,
                                            int avMode) {
            super.onInvitationReceived(identifier, roomId, avMode);

        }

        // 发起方收到接收方接受邀请通知
        @Override
        protected void onInvitationAccepted() {
            super.onInvitationAccepted();
        }

        // 发起方收到接收方拒绝邀请通知
        @Override
        protected void onInvitationRefused() {
            super.onInvitationRefused();
        }

        // 发起方取消邀请通知
        @Override
        protected void onInvitationCanceled(String identifier) {
            super.onInvitationCanceled(identifier);
        }
    };

    public TAVInvitation(){
        mAv.init();
        mAv.setDelegate(mAVInvitationDelegate);
    }


    public void onDestroy(){
        mAv.uninit();
    }
}
