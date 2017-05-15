package com.cuckoo95.cnetlib.def.http.dispatcher;

import android.content.Context;

import com.cuckoo95.cnetlib.def.caller.IReqCallback;
import com.cuckoo95.cnetlib.def.caller.IReqRefactor;
import com.cuckoo95.cnetlib.def.http.HttpServerHelper;
import com.cuckoo95.cnetlib.def.http.IHttpServer;
import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.request.IBaseRequest;
import com.cuckoo95.cnetlib.def.http.resp.IErrResp;
import com.cuckoo95.cutillib.CListUtil;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Cuckoo
 * @date 2016-12-26
 * @description 用于管理和分发一次行为中的所有请求
 */

public class ReqDispatcher {
    private Context context = null;
    //网络请求的具体实现
    private IHttpServer httpServer = null;
    //请求状态回调
    private IReqCallback reqCallback = null;
    //请求结果的回调通知
    private IReqCompleteCallback allReqsComplete;
    private Vector<IBaseRequest> allMajorRequests = null;
    //为正常执行的请求列表
    private ArrayList<IBaseRequest> unExecReqList = null;

    //是否返回json
    private boolean isReturnJson = false;
    //接口重置回调
    private IReqRefactor reqRefactor = null ;
    //是否需要显示loading
    private boolean isShowLoading = false;
    //错误异常分发
    private IReqErrorDispatcher reqErrorReceiver = null ;
    private Class baseRespClass = null ;
    public ReqDispatcher(Context context,
                         IBaseRequest[] requests,
                         IReqCallback reqCallback,
                         IReqRefactor reqRefactor,
                         Class baseRespClass,
                         boolean isReturnJson,
                         IHttpServer httpServer) {
        this.context = context;
        this.reqCallback = reqCallback;
        this.isReturnJson = isReturnJson;
        this.reqRefactor = reqRefactor;
        this.httpServer = httpServer;
        this.baseRespClass = baseRespClass;
        parseRequests(requests);
    }

    /**
     * 设置错误异常接收器
     * @param reqErrorReceiver
     */
    public void setReqErrorReceiver(IReqErrorDispatcher reqErrorReceiver) {
        this.reqErrorReceiver = reqErrorReceiver;
    }

    /**
     * 将请求数组转成ArrayList
     *
     * @param requests
     */
    private void parseRequests(IBaseRequest[] requests) {
        if (requests != null) {
            this.allMajorRequests = new Vector<>();
            for (IBaseRequest request : requests) {
                allMajorRequests.add(request);
            }
        }

    }

    /**
     * 所有请求的分发入口
     *
     * @return
     */
    public boolean onDispatch() {
        if (CListUtil.isEmpty(allMajorRequests)) {
            return false;
        }
        //并发执行所有请求
        IBaseRequest request = null;
        for (int i = 0; i < allMajorRequests.size(); i++) {
            request = allMajorRequests.get(i);
            if (i == 0) {
                isShowLoading = request.isShowLoadding();
                if (isShowLoading) {
                    //显示loading
                    HttpServerHelper.showLoading(reqCallback);
                }
            }
            SyncReqsDispatcher dispatcher = new SyncReqsDispatcher(context,
                    request,
                    reqCallback,
                    reqRefactor,
                    getCurrentReqCompleteCallback(),
                    baseRespClass,
                    isReturnJson,
                    httpServer);
            //开始执行请求
            boolean isDispatched = dispatcher.onDispatch();
            if (!isDispatched) {
                addUnExecRequest(request);
            }
        }
        return true;
    }

    /**
     * 添加未正常执行请求
     *
     * @param req
     */
    private void addUnExecRequest(IBaseRequest req) {
        if (unExecReqList == null) {
            unExecReqList = new ArrayList<>();
        }
        unExecReqList.add(req);
    }

    /**
     * 统一处理请求结果
     *
     * @param majorReq
     *  主请求，既{@link IBaseRequest#getSyncReqList()}中包含子请求
     * @param request
     *  子请求，既{@link IBaseRequest#getSyncReqList()}中的请求
     * @param errResp 请求是否失败， 如果失败返回具体错误信息
     */
    private void respComplete(IBaseRequest majorReq, CAbstractRequst request, IErrResp errResp) {
        synchronized (this) {
            if (errResp != null ) {
                if(reqErrorReceiver != null ){
                    reqErrorReceiver.onReceiveError(majorReq,request,errResp);
                }

            }
            if(!CListUtil.isEmpty(unExecReqList)){
                //移除未正常执行的请求
                ArrayList tempList = (ArrayList<IBaseRequest>)unExecReqList.clone();
                boolean isSuccess = allMajorRequests.removeAll(tempList);
                if( isSuccess ){
                    unExecReqList.removeAll(tempList);
                }
            }
            if (!CListUtil.isEmpty(allMajorRequests)) {
                allMajorRequests.remove(majorReq);

            }
            if(CListUtil.isEmpty(allMajorRequests)){
                //请求完全结束
                if( isShowLoading ){
                    HttpServerHelper.dismissLoading(reqCallback);
                }
                if(reqErrorReceiver != null ){
                    //处理并显示错误信息
                    reqErrorReceiver.onShowError(reqCallback);
                }
                release();
            }
        }
    }

    /**
     * 处理所有请求的请求结果，按照{@link #allMajorRequests}的顺序依次执行请求
     *
     * @return
     */
    private IReqCompleteCallback getCurrentReqCompleteCallback() {
        if (allReqsComplete == null) {
            allReqsComplete = new IReqCompleteCallback() {
                @Override
                public void onComplete(IBaseRequest majorReq, CAbstractRequst subReq, IErrResp errResp) {
                    respComplete(majorReq,subReq, errResp);
                }
            };
        }
        return allReqsComplete;
    }

    public void release(){
        if(allMajorRequests != null ){
            allMajorRequests.clear();
            allMajorRequests = null ;
        }
        if( unExecReqList != null ){
            unExecReqList.clear();
            unExecReqList = null ;
        }
    }
}
