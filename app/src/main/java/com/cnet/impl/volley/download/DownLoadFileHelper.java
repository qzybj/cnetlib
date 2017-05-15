/*
 * Copyright (C) 2015 Vince Styling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cnet.impl.volley.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.cuckoo95.cutillib.ST;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

/**
 * @author Cuckoo
 * @date 2016-09-24
 * @description
 *      下载文件
 */
public class DownLoadFileHelper {
    //未知错误
    public static final int ERRCODE_UNKNOWN = 100000;
    //要下载的文件长度为0
    public static final int ERRCODE_FILE_EMPTY = 200000;
    //下载完文件 重命名失败
    public static final int ERRCODE_RENAME_FAILURE = 300000;
    //下载IO异常
    public static final int ERRCODE_IOEXCEPTION = 400000;
    //Http状态码不正确,错误码>ERRCODE_HTTPSTATUS_ERR
    public static final int ERRCODE_HTTPSTATUS_ERR = 500000;
    private File mStoreFile;
    private File mTemporaryFile;
    private HttpClient mHttpCient = null;
    private HttpGet mHttpGet= null;
    private String mRequestUrl;
    private Context context = null ;
    public DownLoadFileHelper(Context context, String storeFilePath, String url) {
        this(context,new File(storeFilePath), url);
    }

    public DownLoadFileHelper(Context context, File storeFile, String url) {
        this.context = context ;
        mStoreFile = storeFile;
        mTemporaryFile = new File(storeFile + ".tmp");
        try {
            mTemporaryFile.getParentFile().mkdirs();
            if(!mTemporaryFile.exists()){
                mTemporaryFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRequestUrl=url;
    }

    public File downDownloadFile(boolean isAllow3){
        return downDownloadFile(isAllow3,null);
    }

    /**
     * In this method, we got the Content-Length, with the TemporaryFile length,
     * we can calculate the actually size of the whole file, if TemporaryFile not exists,
     * we'll take the store file length then compare to actually size, and if equals,
     * we consider this download was already done.
     * We used {@link RandomAccessFile} to continue download, when download success,
     * the TemporaryFile will be rename to StoreFile.
     * @param  isAllow3G
     *      is can download in 3g env. if false, just download in wifi
     * @param callback
     *      Download callback.
     */
    public File downDownloadFile(boolean isAllow3G,DownloadCallback callback){
        InputStream in = null;
        HttpEntity entity =null;
        RandomAccessFile tmpFileRaf =null;
        boolean isSuccess = true;
        //错误码
        int errCode = ERRCODE_UNKNOWN ;
        //错误信息
        String errMsg = null ;
        try {
            //回调开始下载
            onStart(callback);
            mHttpCient = new DefaultHttpClient();
            mHttpGet = new HttpGet(mRequestUrl);
            mHttpGet.addHeader("Range", "bytes=" + mTemporaryFile.length() + "-");
            //mHttpGet.addHeader("Accept-Encoding", "identity");
            HttpResponse response = mHttpCient.execute(mHttpGet);
            int respStatus = response.getStatusLine().getStatusCode();
            if(200==respStatus || 206 == respStatus){
                // Content-Length might be negative when use HttpURLConnection because it default header Accept-Encoding is gzip,
                // we can force set the Accept-Encoding as identity in prepare() method to slove this problem but also disable gzip response.
                entity = response.getEntity();
                long fileSize = entity.getContentLength();
                long downloadedSize = mTemporaryFile.length();
                boolean isSupportRange = isSupportRange(response);
                if (isSupportRange) {
                    fileSize += downloadedSize;

                    // Verify the Content-Range Header, to ensure temporary file is part of the whole file.
                    // Sometime, temporary file length add response content-length might greater than actual file length,
                    // in this situation, we consider the temporary file is invalid, then throw an exception.
                    String realRangeValue = getHeader(response, "Content-Range");
                    // response Content-Range may be null when "Range=bytes=0-"
                    if (!TextUtils.isEmpty(realRangeValue)) {
                        String assumeRangeValue = "bytes " + downloadedSize + "-" + (fileSize - 1);
                        if (TextUtils.indexOf(realRangeValue, assumeRangeValue) == -1) {
                            onFailure(callback, ERRCODE_FILE_EMPTY,"File is empty");
                            return null;
                        }
                    }
                }

                // Compare the store file size(after download successes have) to server-side Content-Length.
                // temporary file will rename to store file after download success, so we compare the
                // Content-Length to ensure this request already download or not.
                if (fileSize > 0 && mStoreFile.length() == fileSize) {
                    // Rename the store file to temporary file, mock the download success. ^_^
                    //mStoreFile.renameTo(mTemporaryFile);
                    onComplete(callback,mStoreFile.getAbsolutePath());
                    return mStoreFile;
                }

                tmpFileRaf = new RandomAccessFile(mTemporaryFile, "rw");

                // If server-side support range download, we seek to last point of the temporary file.
                if (isSupportRange) {
                    tmpFileRaf.seek(downloadedSize);
                } else {
                    // If not, truncate the temporary file then start download from beginning.
                    tmpFileRaf.setLength(0);
                    downloadedSize = 0;
                }

                in = entity.getContent();
                // Determine the response gzip encoding, support for HttpClientStack download.
                if (isGzipContent(response) && !(in instanceof GZIPInputStream)) {
                    in = new GZIPInputStream(in);
                }
                byte[] buffer = new byte[128 * 1024]; // 128K buffer
                int offset;
                while ((offset = in.read(buffer)) != -1) {
                    //当前环境为wifi, 或者允许3g下载时,开始写文件
                    if( isAllow3G ||
                            NetTools.isWifi(context) ){
                        tmpFileRaf.write(buffer, 0, offset);
                        downloadedSize += offset;
                        //下载进度回调
                        onProgress(callback,downloadedSize,fileSize);
                    }else{
                        isSuccess=false;
                        break;
                    }
                }
                //下载完毕
                if(isSuccess){
                    if (mTemporaryFile.canRead() && mTemporaryFile.length() > 0) {
                        boolean isExist = mStoreFile.exists();
                        if( isExist ){
                            //如果文件存在, 先删除
                            mStoreFile.delete();
                        }
                        boolean success = mTemporaryFile.renameTo(mStoreFile);
                        if (success) {
                            onComplete(callback,mStoreFile.getAbsolutePath());
                            return mStoreFile;
                        }else {
                            errCode = ERRCODE_RENAME_FAILURE;
                            errMsg = "Rename file failure.";
                        }
                    }
                }
            }else {
                errCode = ERRCODE_HTTPSTATUS_ERR + respStatus;
                errMsg = "Http status is not 200 or 206";
            }
        } catch (Exception e) {
            errCode =ERRCODE_IOEXCEPTION;
            errMsg = ST.f(e.getLocalizedMessage());
        }finally {
            try {
                // Close the InputStream
                if (in != null) in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(null!=entity){
                try {
                    // release the resources by "consuming the content".
                    entity.consumeContent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(null!=tmpFileRaf){
                try {
                    tmpFileRaf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //下载失败
        onFailure(callback,errCode,errMsg);
        return null;
    }

    /*************************************************************/
    /**************************下载回调****************************/
    /*************************************************************/
    /**
     * 开始下载
     * @param callback
     */
    public void onStart(DownloadCallback callback){
        if( callback != null ){
            callback.onStart();
        }
    }

    /**
     * 下载进度
     * @param downloadSize
     *  下载大小
     * @param totalSize
     *  总大小
     */
    public void onProgress(DownloadCallback callback,double downloadSize, double totalSize){
        if( callback != null ){
            callback.onProgress(downloadSize,totalSize);
        }
    }

    /**
     * 下载成功
     * @param destiationPath
     *  文件存放路径
     */
    public void onComplete(DownloadCallback callback,String destiationPath){
        if( callback != null ){
            callback.onComplete(destiationPath);
        }
    }

    /**
     * 下载失败
     * @param errStatus
     *  下载失败状态吗
     * @param errMsg
     *  下载失败原因
     */
    public void onFailure(DownloadCallback callback,int errStatus, String errMsg){
        if( callback != null ){
            callback.onFailure(errStatus,errMsg);
        }
    }

    /*************************************************************/
    /**********************下载相关工具类****************************/
    /*************************************************************/

    public static String getHeader(HttpResponse response, String key) {
        Header header = response.getFirstHeader(key);
        return header == null ? null : header.getValue();
    }

    public static boolean isSupportRange(HttpResponse response) {
        if (TextUtils.equals(getHeader(response, "Accept-Ranges"), "bytes")) {
            return true;
        }
        String value = getHeader(response, "Content-Range");
        return value != null && value.startsWith("bytes");
    }

    public static boolean isGzipContent(HttpResponse response) {
        return TextUtils.equals(getHeader(response, "Content-Encoding"), "gzip");
    }

    /**
     *  下载回调
     */
    public interface DownloadCallback{
        /**
         * 开始下载
         */
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

    /**
     * 网络工具类
     */
    static class NetTools {
        private static String NET_TYPE_WIFI="wifi";
        private static String NET_TYPE_3G_GPRS="3G/GPRS";

        /**
         * 获取网络类型
         *
         * @param context
         *            上下文
         * @return String 返回网络类型
         */
        public static String getAccessNetworkType(Context context) {
            int type = 0;
            ConnectivityManager connManager = (ConnectivityManager) context .getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connManager!=null && connManager.getActiveNetworkInfo()!=null){
                type = connManager.getActiveNetworkInfo().getType();
            }
            if (type == ConnectivityManager.TYPE_WIFI) {
                return NET_TYPE_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                return NET_TYPE_3G_GPRS;
            }
            return "";
        }

        /**
         * 网络环境是否是Wifi
         * @param context
         * @return
         */
        public static boolean isWifi(Context context){
            return NET_TYPE_WIFI.equals(getAccessNetworkType(context));
        }
    }
}
