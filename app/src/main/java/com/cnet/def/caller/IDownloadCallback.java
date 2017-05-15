package com.cnet.def.caller;

/**
 *
 * @author Cuckoo
 * @date 2016-09-20
 * @description
 *      下载文件的接口回调
 */
public interface IDownloadCallback {
    //开始下载
    void onStart();

    /**
     * 下载进度
     * @param downloadSize
     *  下载大小
     * @param totalSize
     *  总大小
     */
    void onProgress(double downloadSize, double totalSize);

    /**
     * 下载成功
     * @param destiationPath
     *  文件存放路径
     */
    void onComplete(String destiationPath);

    /**
     * 下载失败
     * @param errStatus
     *  下载失败状态吗
     * @param errMsg
     *  下载失败原因
     */
    void onFailure(int errStatus, String errMsg);
}
