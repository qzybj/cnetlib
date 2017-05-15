package com.cuckoo95.cnetlib.def.http.dispatcher;

import com.cuckoo95.cnetlib.def.caller.IReqCallback;
import com.cuckoo95.cnetlib.def.http.HttpServerHelper;
import com.cuckoo95.cnetlib.def.http.IHttpServerCallback;
import com.cuckoo95.cnetlib.def.http.exception.HttpException;
import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.resp.IErrResp;
import com.cuckoo95.cnetlib.def.http.resp.IRespBase;

/**
 *
 * @author Cuckoo
 * @date 2016-12-26
 * @description
 *      接收单个请求的结果，并处理
 */

public class SingleReqResper implements IHttpServerCallback{
    private CAbstractRequst request = null ;
    private IReqCallback reqCallback = null ;
    private IReqCompleteCallback completeCallback;
    public SingleReqResper(CAbstractRequst request,
                           IReqCallback reqCallback,
                           IReqCompleteCallback completeCallback){
        this.request = request ;
        this.reqCallback = reqCallback;
        this.completeCallback = completeCallback ;
    }

    /**
     * 请求开始
     * @param request
     */
    @Override
    public void onStart(CAbstractRequst request) {
        HttpServerHelper.onRequestStart(request, reqCallback);
    }

    /**
     * 接口请求成功
     * @param respObj
     *      The bean of json
     * @param req
     * @param <T>
     */
    @Override
    public <T> void onResponse(IRespBase<T> respObj, CAbstractRequst req) {
        if(respObj == null ){
            HttpException exception = new HttpException(IErrResp.STATUS_RESPNULL,"Response is null");
            onErrResponse(exception,request);
        }else {
            if( respObj.isSuccess() ){
                HttpServerHelper.onRequestSuccess(request,respObj, reqCallback);
                onRequestComplete(null);
            }else {
                //Business failure.
                int errCode = respObj.getResultStatus();
                HttpException exception = new HttpException(errCode,respObj.getMessage());
                onErrResponse(exception,request);
            }

        }
    }

    /**
     * 接口请求失败
     * @param errResp
     * @param req
     */
    @Override
    public void onErrResponse(IErrResp errResp, CAbstractRequst req) {
        //Callback request failure.
        HttpServerHelper.onRequestFailure(request,errResp, reqCallback);
        onRequestComplete(errResp);
    }

    /**
     * 请求结束
     * @param errResp
     *  错误信息
     */
    private void onRequestComplete(IErrResp errResp){
        if( completeCallback != null ){
            completeCallback.onComplete(null,request,errResp);
        }
    }
}
