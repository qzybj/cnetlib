package com.cuckoo95.cnetlib.impl.volley;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cuckoo95.cnetlib.def.caller.IDownloadCallback;
import com.cuckoo95.cnetlib.def.http.IHttpDownloadServer;
import com.cuckoo95.cnetlib.impl.volley.download.DownLoadFileHelper;

import java.io.File;

/**
 * @author Cuckoo
 * @date 2016-09-24
 * @description 下载文件管理类
 */

public class DownloadFileServer implements IHttpDownloadServer {

    @Override
    public boolean downloadFile(final Context context, final String url, final String destinationPath,
                                final boolean isAllow3G, final IDownloadCallback downloadCallback) {
        new Thread() {
            private final int MSG_ONSTART =1 ;
            private final int MSG_ONPROCESS = 2;
            private final int MSG_ONCOMPLETE = 3;
            private final int MSG_ONERROR = 4;
            private Handler handler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(downloadCallback == null ){
                        return ;
                    }
                    switch (msg.what){
                        case MSG_ONSTART:
                            downloadCallback.onStart();
                            break ;
                        case MSG_ONPROCESS:
                            if( msg.obj != null ){
                                double[] values = (double[])msg.obj;
                                if( values.length == 2){
                                    downloadCallback.onProgress(values[0],values[1]);
                                }
                            }
                            break ;
                        case MSG_ONCOMPLETE:
                            if( msg.obj instanceof String ){
                                downloadCallback.onComplete((String) msg.obj);
                            }
                            break ;
                        case MSG_ONERROR:
                            if( msg.obj instanceof ErrInfo ){
                                ErrInfo errInfo = (ErrInfo)msg.obj;
                                downloadCallback.onFailure(errInfo.errStatus,errInfo.errMsg);
                            }
                            break ;
                    }
                }
            };

            @Override
            public void run() {
                DownLoadFileHelper downloadHelper = new DownLoadFileHelper(context, destinationPath, url);
                downloadHelper.downDownloadFile(isAllow3G, new DownLoadFileHelper.DownloadCallback() {
                    @Override
                    public void onStart() {
                        handler.sendEmptyMessage(MSG_ONSTART);
                    }

                    @Override
                    public void onProgress(double downloadSize, double totalSize) {
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_ONPROCESS;
                        msg.obj = new double[]{downloadSize,totalSize};
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onComplete(String destiationPath) {
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_ONCOMPLETE;
                        msg.obj = destiationPath;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(int errStatus, String errMsg) {
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_ONERROR;
                        ErrInfo errInfo = new ErrInfo();
                        errInfo.errStatus = errStatus;
                        errInfo.errMsg = errMsg;
                        msg.obj = errInfo;
                        handler.sendMessage(msg);
                    }
                });
            }
            class  ErrInfo{
                public int errStatus ;
                public String errMsg ;
            }
        }.start();
        return false;
    }

    /**
     * 同步下载文件
     * @param context
     * @param url
     *      文件URL地址
     * @param destinationPath
     *      目标文件保存路径
     * @param isAllow3G
     *      是否允许3G下载
     * @return
     */
    public File downloadFileBySync(final Context context, final String url, final String destinationPath,
                                    final boolean isAllow3G) {
        DownLoadFileHelper downloadHelper = new DownLoadFileHelper(context, destinationPath, url);
        return downloadHelper.downDownloadFile(isAllow3G);
    }
}
