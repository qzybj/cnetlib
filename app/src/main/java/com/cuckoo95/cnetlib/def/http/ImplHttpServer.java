package com.cuckoo95.cnetlib.def.http;

import android.content.Context;

import com.cuckoo95.cnetlib.def.caller.IDownloadCallback;
import com.cuckoo95.cnetlib.def.caller.IReqCallback;
import com.cuckoo95.cnetlib.def.caller.IReqRefactor;
import com.cuckoo95.cnetlib.def.http.def.DefReqErrReceiver;
import com.cuckoo95.cnetlib.def.http.dispatcher.ReqDispatcher;
import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.request.IBaseRequest;
import com.cuckoo95.cnetlib.def.http.resp.IRespBase;
import com.cuckoo95.cutillib.log.CLog;
import com.cuckoo95.cutillib.log.ILog;

import java.util.HashMap;

/**
 *
 * @author Cuckoo
 * @date 2016-09-03
 * @description
 *      The implements class of {@link HttpServer}, to manange http request.
 */
public class ImplHttpServer {
    private static Context context = null ;
    //Is need return json to caller. default is not.
    private static boolean isReturnJson = false ;
    private static IHttpServer httpServerImpl = null ;
    //接口返回json第一级的节点信息， 所有接口通用
    private static Class baseRespClass = null ;
    protected static void init(Context ctx, Class implClass, Class respClass, boolean isNeedReturnJson,
                               ILog logImpl){
        setLogImpl(logImpl);
        if( ctx == null || implClass == null  || respClass == null ){
            new RuntimeException("HttpServer init failure, context, baseRespClass or implClass is null");
        }
        IHttpServer httpServer = null ;
        try {
            Object obj = implClass.newInstance();
            if( obj instanceof IHttpServer){
                httpServer = (IHttpServer)obj;
            }
        }catch (Exception e){

        }
        if( httpServer == null ){
            new RuntimeException("HttpServer impl class is incorrect." + implClass.toString());
        }
        IRespBase respBase = null ;
        try {
            Object obj = respClass.newInstance();
            if( obj instanceof IRespBase){
                respBase = (IRespBase)obj;
            }
        }catch (Exception e){

        }
        if( respBase == null ){
            new RuntimeException("Base response class is incorrect." + respClass.toString());
        }
        context = ctx.getApplicationContext();
        httpServerImpl = httpServer;
        isReturnJson = isNeedReturnJson ;
        baseRespClass = respClass;
    }

    /**
     *
     * @param logImpl
     */
    protected static void setLogImpl(ILog logImpl){
        CLog.setLogImpl(logImpl);
    }

    /**
     * Check this sdk is initted.
     * @return
     */
    protected static boolean isInit(){
        if( context == null || httpServerImpl == null  ){
            throw new RuntimeException("HttpServer init failure, context is null");
        }
        return true ;
    }

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
    protected static boolean downloadFile(String url, String destinationPath,
                                          boolean isAllow3G, IDownloadCallback downloadCallback){
        if( !isInit() ){
            return false ;
        }
        return httpServerImpl.downloadFile(context,url,destinationPath,isAllow3G,downloadCallback) ;
    }

    /**
     * Get default http executor
     * @return
     */
    protected static IHttpServer getDefHttpExecutor(){
        return httpServerImpl;
    }

    /**
     * Get data by sync. Just support one request.
     * @param request
     * @param <E>
     * @return
     */
    protected static  <T,E extends IRespBase> E getDataBySync(CAbstractRequst<T> request){
        if( !isInit() ){
            return null ;
        }
        if( request == null ){
            return null ;
        }

        HashMap<String,Object> postParamMap = null ;
        String postParamByJson = null ;
        if( request.isPostJson()  ){
            postParamByJson = request.getRequestParamsByJson();
        }else {
            postParamMap = request.getRequestParams();
        }
        //获取顶级解析类
        Class rootResp = HttpServerHelper.parseRootRespClass(baseRespClass,
                request);
        IHttpServer httpExecutor = HttpServerHelper.parseHttpExecutor(httpServerImpl,request);
        return (E)httpExecutor.getDataBySync(context,request.getReqMethod(),
                request.getFullUrl(),
                postParamMap,
                postParamByJson,
                request.getRequestHeader(context),
                request.getRespObjClass(),
                rootResp,isReturnJson,request);
    }

    /******************************************************/
    /******************Async request************************/
    /******************************************************/
    /**
     * Get data by async request list.
     * @param requestList
     * @param requestCallback
     * @param requestRefactor 重置请求参数回调
     * @return
     */
    protected static boolean getData(IReqCallback requestCallback, IReqRefactor requestRefactor,
                                     IBaseRequest... requestList){
        if( !isInit() ){
            return false ;
        }
        //执行请求
        ReqDispatcher dispatcher = new ReqDispatcher(context,
                requestList,requestCallback,requestRefactor,
                baseRespClass,
                isReturnJson,httpServerImpl);
        DefReqErrReceiver reqErrReceiver = new DefReqErrReceiver(context);
        dispatcher.setReqErrorReceiver(reqErrReceiver);
        return dispatcher.onDispatch();
    }
}
