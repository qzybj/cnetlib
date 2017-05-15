package com.cnet.def.http;

import android.content.Context;

import com.cnet.def.caller.IDownloadCallback;

/**
 *
 * @author Cuckoo
 * @date 2016-09-24
 * @description
 *      下载文件接口
 */

public interface IHttpDownloadServer {
    /**
     * 下载文件
     * @param url
     *      Url地址
     * @param destinationPath
     *      下载文件存放路径
     * @param  isAllow3G
     *      是否允许3G下下载
     * @param  downloadCallback
     *      下载回调
     * @return
     */
    boolean downloadFile(Context context, String url, String destinationPath,
                         boolean isAllow3G, IDownloadCallback downloadCallback);
}
