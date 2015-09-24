package com.falcon.HTTPDownLoad;

import com.duowan.mobile.netroid.NetroidError;

/**
 * Created by Administrator on 2015/7/1 0001.
 */
public interface FLDownloadListener {
    public void onStart(String url,int tag);
    public void onProgress(String url,int tag,float progress);
    public void onFinish(String url,int tag,String path);
    public void onError(String url,int tag,NetroidError error);
}
