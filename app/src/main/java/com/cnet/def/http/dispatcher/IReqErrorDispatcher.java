package com.cnet.def.http.dispatcher;

import com.cnet.def.caller.IReqTipCallback;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.request.CRequestConstants;
import com.cnet.def.http.request.IBaseRequest;
import com.cnet.def.http.resp.IErrResp;

/**
 *
 * @author Cuckoo
 * @date 2016-12-27
 * @description
 *      请求错误分发
 */

public interface IReqErrorDispatcher {

    /**
     * 接收到错误信息
     * @param majorReq
     *  主请求，既{@link IBaseRequest}
     * @param targetReq
     *  目标请求，可能是{@link IBaseRequest}也可能是{@link IBaseRequest#getSyncReqList()}中的请求
     * @param errResp
     *  targetReq请求的错误信息
     */
    void onReceiveError(IBaseRequest majorReq,
                        CAbstractRequst targetReq, IErrResp errResp);
    /**
     * 根据{@link #onReceiveError(IBaseRequest, CAbstractRequst, IErrResp)}收到的信息进行显示
     * @param dialogCallback
     *    通过{@link IReqTipCallback#showTipDialog(String)} 显示错误信息（如果错误显示类型为{@link CRequestConstants#SHOW_MSGTYPE_DIALOG}）
     */
    void onShowError(IReqTipCallback dialogCallback);
}
