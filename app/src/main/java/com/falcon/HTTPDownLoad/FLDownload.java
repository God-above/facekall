package com.falcon.HTTPDownLoad;

import android.os.Build;
import android.util.Log;

import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.request.FileDownloadRequest;
import com.duowan.mobile.netroid.stack.HttpClientStack;
import com.duowan.mobile.netroid.stack.HttpStack;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
import com.duowan.mobile.netroid.Listener;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Administrator on 2015/7/1 0001.
 */
public class FLDownload {
    private  final  String TAG="FLDownload";
    private static FLDownload instance = null;
    //目标缓存
    private static final String mSaveDirPath = "/sdcard/falconCache/";

    // Netroid入口，私有该实例，提供方法对外服务。
    private static RequestQueue mRequestQueue;
    // 文件下载管理器，私有该实例，提供方法对外服务。
    private static FileDownloader mFileDownloader;

    public static FLDownload getInstance() {
        if(instance==null){
            instance=new FLDownload();
        }
        return instance;
    }

    public FLDownload(){
        Log.d(TAG,"FLDownload");
        // 创建下载的队列
        HttpStack stack;
        String userAgent = "falcon/0";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            stack = new HurlStack(userAgent, null);
        } else {
            stack = new HttpClientStack(userAgent);
        }

        Network network = new BasicNetwork(stack, HTTP.UTF_8);
        mRequestQueue = new RequestQueue(network, RequestQueue.DEFAULT_NETWORK_THREAD_POOL_SIZE, null);
        mRequestQueue.start();

        mFileDownloader = new FileDownloader(mRequestQueue, 1){
            @Override
            public FileDownloadRequest buildRequest(String storeFilePath, String url) {
                return new FileDownloadRequest(storeFilePath, url) {
                    @Override
                    public void prepare() {
                        Log.d(TAG,"buildRequest prepare");
                        addHeader("Accept-Encoding", "identity");
                        super.prepare();
                    }
                };
            }
        };

        //创建缓存目录
        File downloadDir = new File(mSaveDirPath);
        if (!downloadDir.exists()) downloadDir.mkdir();
    }

    /**
     * 添加下载文件
     */
    public  void addFile(String url,FLDownloadListener listener) {
        addFile(url,0,listener);
    }
    /**
     * 添加下载文件
     */
    public  void addFile(String url,int tag,FLDownloadListener listener){
        //String name=FLMD5.getMD5(url);
        String n[] = url.split("/");
        String name=n[n.length-1];
        String path=mSaveDirPath+name;


        Log.d(TAG,"addFile:"+url);
        Log.d(TAG, "addFile:" + path);


        //检测当前下载文件是否已经存在
        File file = new File(path);
        if (!file.exists()){
            //不存在,去下载
            download( path, url, tag, listener);
        }else{
            final FLDownloadTask task=new FLDownloadTask(url,path,tag,listener);
            task.listener.onStart(task.url, task.tag);
            task.listener.onFinish(task.url, task.tag, task.filePath);
        }
    }

    private void download(String path,String url,int tag,FLDownloadListener listener){
        final FLDownloadTask task=new FLDownloadTask(url,path,tag,listener);
        task.listener.onStart(task.url, task.tag);
        task.controller=mFileDownloader.add(path,url,new Listener<Void>() {
            @Override
            public void onPreExecute() {
                Log.d(TAG,"onPreExecute");
            }

            // 注：如果暂停或放弃了该任务，onFinish()不会回调
            @Override
            public void onFinish() {
                //下载完成
                Log.d(TAG, "onFinish:" + task.filePath);
            }

            // 注：如果暂停或放弃了该任务，onSuccess()不会回调
            @Override
            public void onSuccess(Void response) {
                //下载成功
                Log.d(TAG,"onSuccess");
                task.listener.onFinish(task.url, task.tag, task.filePath);
            }

            // 注：如果暂停或放弃了该任务，onError()不会回调
            @Override
            public void onError(NetroidError error) {
                //下载失败
                Log.d(TAG,"onError");
                task.listener.onError(task.url,task.tag,error);
            }

            // Listener添加了这个回调方法专门用于获取进度
            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                // 注：downloadedSize 有可能大于 fileSize，具体原因见下面的描述
                //Toast.makeText("下载进度：" + (downloadedSize * 1.0f / fileSize * 100) + "%").show();
                Log.d(TAG,"download progress：" + (downloadedSize * 1.0f / fileSize * 100) + "%");

                task.listener.onProgress(task.url,task.tag,(downloadedSize * 1.0f / fileSize * 100));
            }
        });
    }
}
