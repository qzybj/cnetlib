package com.cuckoo95.cnetlib.def.http.def;

import android.content.Context;

import com.cuckoo95.cnetlib.def.caller.IReqTipCallback;
import com.cuckoo95.cnetlib.def.http.HttpServerHelper;
import com.cuckoo95.cnetlib.def.http.dispatcher.IReqErrorDispatcher;
import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.request.IBaseRequest;
import com.cuckoo95.cnetlib.def.http.resp.IErrResp;

/**
 *
 * @author Cuckoo
 * @date 2016-12-27
 * @description
 *      处理请求异常，默认提示第一接收到的异常
 */

public class DefReqErrReceiver implements IReqErrorDispatcher{
    //第一个返回的错误信息，当整个请求结束，提示这个错误信息
    private IErrResp firstErrResp = null;
    private IBaseRequest errMajorRequest = null ;
    //具体出错的请求
    private CAbstractRequst errTargetRequest = null ;
    private Context context = null ;
    public DefReqErrReceiver(Context context){
        this.context = context;
    }

    @Override
    public void onReceiveError(IBaseRequest majorReq, CAbstractRequst targetReq, IErrResp errResp) {
        if (errResp != null && firstErrResp == null ) {
            firstErrResp = errResp;
            errMajorRequest = majorReq;
            errTargetRequest = targetReq;
        }
    }

    @Override
    public void onShowError(IReqTipCallback dialogCallback) {
        //显示错误信息
        HttpServerHelper.respErr(context,firstErrResp, errMajorRequest, errTargetRequest,dialogCallback);
    }
}
