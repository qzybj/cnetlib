package com.cuckoo95.cnetlib.def.caller;

import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.resp.IErrResp;
import com.cuckoo95.cnetlib.def.http.resp.IRespBase;

/**
 *
 * @author Cuckoo
 * @date 2016-09-01
 * @description
 *      Monitior status of network request.
 */
public interface IReqCallback extends IReqTipCallback {
    /**
     * Start request.
     * @param reqCode
     * @param request
     */
    void onRequestStart(int reqCode, CAbstractRequst request);


    /**
     * Http/Https request success
     * @param reqCode
     *      Request identify
     * @param respDataObj
     *      Return from server. and is not contain whole json. just the business elements of json.
     * @param resp
     *      The whole result.
     * @param <T>
     */
    <T>void onInflateRequest(int reqCode, CAbstractRequst request, T respDataObj, IRespBase<T> resp);

    /**
     * Http/Https request failure.
     * @param reqCode
     * @param errResp
     *      Error info.
     *
     */
    void onRequestFailure(int reqCode, CAbstractRequst request, IErrResp errResp);

    /**
     * 所有请求结束
     * @param lastReqCode
     *  最后一个完成请求的请求码， 针对多个异步请求的情况
     * @param lastRequest
     *  最后一个完成请求的请求信息， 针对多个异步请求的情况
     */
    void onAllRequestDone(int lastReqCode, CAbstractRequst lastRequest);
}
