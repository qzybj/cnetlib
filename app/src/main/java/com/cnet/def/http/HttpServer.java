package com.cnet.def.http;

import android.content.Context;

import com.cnet.def.caller.IDownloadCallback;
import com.cnet.def.caller.IReqCallback;
import com.cnet.def.caller.IReqRefactor;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.request.IBaseRequest;
import com.cnet.def.http.resp.IRespBase;
import com.cnet.impl.volley.VolleyHttpServer;

/**
 * @author Cuckoo
 * @date 2016-09-02
 * @description To manage all http(https) request. such as one request in sync(or async).
 * More than one requests in once user operation.
 */
public class HttpServer {

    /**
     * 采用默认是现实类
     * @param context
     * @param baseRespClass
     *      返回json的第一级节点信息， 需要实现{@link IRespBase}接口
     */
    public static void initDefault(Context context,Class baseRespClass, boolean isNeedReturnJson){
        init(context, VolleyHttpServer.class, baseRespClass,isNeedReturnJson);
    }

    /**
     * 初始化具体执行网络请求的类
     * @param ctx
     * @param implClass
     *      网络请求具体类， 需要实现{@link IHttpServer}接口
     * @param baseRespClass
     *      返回json的第一级节点信息， 需要实现{@link IRespBase}接口
     * @param isNeedReturnJson
     *      返回值中是否需要返回JSON，默认不返回
     */
    public static void init(Context ctx, Class implClass, Class baseRespClass,boolean isNeedReturnJson) {
        ImplHttpServer.init(ctx, implClass, baseRespClass,isNeedReturnJson);
    }

    /**
     * Get data by sync. Just support one request.
     *
     * @param request
     * @param <E>
     * @return
     */
    public static <T, E extends IRespBase> E getDataBySync(CAbstractRequst<T> request) {
        return ImplHttpServer.getDataBySync(request);
    }

    /**
     * 异步请求数据，请求的接口可以是多个也可以是单个
     *
     * @param requestCallback
     * @param requestList 请求列表
     * @return
     */
    public static boolean getData(IReqCallback requestCallback, IBaseRequest... requestList) {
        return getData(requestCallback,null,requestList);
    }

    /**
     * 异步请求数据， 请求接口可是是多个也可以是单个。 在请求每次接口之前会调用{@link IReqRefactor#onRequestRefactor(int, CAbstractRequst)} 方法判断是否需要更改请求参数
     * @param requestCallback
     *      请求接口回调
     * @param requestRefactor
     *      请求参数更改回调
     * @param requestList
     *      请求接口信息
     * @return
     */
    public static boolean getData(IReqCallback requestCallback, IReqRefactor requestRefactor, IBaseRequest... requestList) {
        return ImplHttpServer.getData(requestCallback,requestRefactor,requestList);
    }

    /**
     * 下载文件
     *
     * @param url              Url地址
     * @param destinationPath  下载文件存放路径
     * @param isAllow3G        是否允许3G下下载
     * @param downloadCallback 下载回调
     * @return
     */

    public static boolean downloadFile(String url, String destinationPath, boolean isAllow3G, IDownloadCallback downloadCallback) {
        return ImplHttpServer.downloadFile(url, destinationPath,isAllow3G,downloadCallback);
    }

}
