package com.cuckoo95.cnetlib.def.http.dispatcher;

import android.content.Context;

import com.cuckoo95.cnetlib.def.caller.IReqCallback;
import com.cuckoo95.cnetlib.def.caller.IReqRefactor;
import com.cuckoo95.cnetlib.def.http.IHttpServer;
import com.cuckoo95.cnetlib.def.http.exception.HttpException;
import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.request.IBaseRequest;
import com.cuckoo95.cnetlib.def.http.resp.IErrResp;
import com.cuckoo95.cutillib.CListUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Cuckoo
 * @date 2016-12-26
 * @description 分发同步请求多个接口
 */

public class SyncReqsDispatcher {
    private Context context = null;
    //网络请求的具体实现
    private IHttpServer httpServer = null;
    //请求接口列表
    private IBaseRequest syncRequests = null;
    //请求状态回调
    private IReqCallback reqCallback = null;
    //请求信息重置
    private IReqRefactor reqRefactor = null;
    //同步请求接口完的回调
    private IReqCompleteCallback parentReqComplete;
    //本次请求结果的的回调通知
    private IReqCompleteCallback currentReqComplete;
    private ArrayList<CAbstractRequst> allRequests = null;

    //是否返回json
    private boolean isReturnJson = false;
    private Class baseRespClass = null ;
    public SyncReqsDispatcher(Context context,
                              IBaseRequest syncRequests,
                              IReqCallback reqCallback,
                              IReqRefactor reqRefactor,
                              IReqCompleteCallback parentReqComplete,
                              Class baseRespClass,
                              boolean isReturnJson,
                              IHttpServer httpServer) {
        this.context = context;
        this.syncRequests = syncRequests;
        this.reqCallback = reqCallback;
        this.reqRefactor = reqRefactor;
        this.parentReqComplete = parentReqComplete;
        this.isReturnJson = isReturnJson;
        this.httpServer = httpServer;
        this.baseRespClass = baseRespClass;
    }

    /**
     * 分发请求
     *
     * @return
     */
    public boolean onDispatch() {
        if (CListUtil.isEmpty(getAllRequests()) || httpServer == null) {
            return false;
        }
        CAbstractRequst request = getNextRequest();
        if (request == null) {
            return false;
        }
        execOneRequest(request);
        return true;
    }

    /**
     * 执行单个请求
     *
     * @param request
     */
    private void execOneRequest(CAbstractRequst request) {
        SingleReqResper singleReqResper = new SingleReqResper(request, reqCallback, getCurrentReqComplete());
        singleReqResper.onStart(request);

        if (reqRefactor != null) {
            //判断是否需要更改请求信息
            CAbstractRequst newRequest = reqRefactor.onRequestRefactor(request.getReqCode(), request);
            if (newRequest != null) {
                request = newRequest;
            }
        }
        boolean isPostByJson = request.isPostJson();
        HashMap<String,Object> postParamMap = null ;
        String postParamByJson = null ;
        if( isPostByJson  ){
            postParamByJson = request.getRequestParamsByJson();
        }else {
            postParamMap = request.getRequestParams();
        }
        //开始同步执行请求
        httpServer.getData(context, request.getReqMethod(), request.getFullUrl(),
                postParamMap,postParamByJson, request.getRequestHeader(context),
                request.getRespObjClass(),baseRespClass,
                isReturnJson, singleReqResper, request);
    }

    /**
     * 获取下一个请求
     *
     * @return
     */
    private CAbstractRequst getNextRequest() {
        if (CListUtil.isEmpty(allRequests)) {
            return null;
        } else {
            //继续执行下一个请求
            return allRequests.get(0);
        }
    }

    /**
     * 统一处理请求结果
     *
     * @param request 请求信息
     * @param errResp 请求是否失败， 如果失败返回具体错误信息
     */
    private void respComplete(CAbstractRequst request, IErrResp errResp) {
        synchronized (this) {
            if (!CListUtil.isEmpty(allRequests)) {
                allRequests.remove(request);
            } else {
                errResp = new HttpException(IErrResp.STATUS_UNKNOWN, "requests of SyncReqsDispatcher is null.");
            }
            if(errResp != null ){
                //请求结束，通知处理结果
                if (parentReqComplete != null) {
                    parentReqComplete.onComplete(syncRequests,request, errResp);
                }
                return ;
            }
            CAbstractRequst nextRequest = getNextRequest();
            if (nextRequest == null) {
                //请求结束，通知处理结果
                if (parentReqComplete != null) {
                    parentReqComplete.onComplete(syncRequests,request, errResp);
                }
            } else {
                //继续执行下一个请求
                execOneRequest(nextRequest);
            }
        }
    }

    /**
     * 处理所有请求的请求结果，按照{@link #allRequests}的顺序依次执行请求
     *
     * @return
     */
    private IReqCompleteCallback getCurrentReqComplete() {
        if (currentReqComplete == null) {
            currentReqComplete = new IReqCompleteCallback() {

                @Override
                public void onComplete(IBaseRequest majorReq, CAbstractRequst subReq, IErrResp errResp) {
                    respComplete(subReq, errResp);
                }
            };
        }
        return currentReqComplete;
    }

    /**
     * 整合所有请求
     *
     * @return
     */
    private ArrayList<CAbstractRequst> getAllRequests() {
        if (syncRequests != null) {
            allRequests = new ArrayList<>();
            allRequests.add(syncRequests.getMarjorReqeust());
            ArrayList<CAbstractRequst> subList = syncRequests.getSyncReqList();
            if (!CListUtil.isEmpty(subList)) {
                allRequests.addAll(subList);
            }
        }
        return allRequests;
    }
}
