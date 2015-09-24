package com.falcon.HTTPDownLoad;

import android.util.Log;

import com.duowan.mobile.netroid.toolbox.FileDownloader;

/**
 * Created by Administrator on 2015/7/1 0001.
 */
public class FLDownloadTask {
    FileDownloader.DownloadController controller;
    String fileName;
    String filePath;
    String url;
    int   tag;
    FLDownloadListener listener;

    public FLDownloadTask(String url,String path,int tag,FLDownloadListener listener){
        this.filePath=path;
        this.url=url;
        this.tag=tag;
        this.listener=listener;
    }
}
