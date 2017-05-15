package com.cnet.def.caller;

import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.resp.IErrResp;
import com.cnet.def.http.resp.IRespBase;

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

}
