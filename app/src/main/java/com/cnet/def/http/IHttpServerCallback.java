package com.cnet.def.http;

import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.resp.IErrResp;
import com.cnet.def.http.resp.IRespBase;

/**
 *
 * @author Cuckoo
 * @date 2016-09-03
 * @description
 *      To callback the status of http server
 */
public interface IHttpServerCallback {

    /**
     * Http/Https request success
     * @param respObj
     *      The bean of json
     * @param request
     *      Origin request info.
     * @param <T>
     */
    <T>void onResponse(IRespBase<T> respObj, CAbstractRequst request);

    /**
     * 请求失败
     * @param errResp
     * @param request
     */
    void onErrResponse(IErrResp errResp, CAbstractRequst request);

    /**
     * 请求开始
     * @param request
     */
    void onStart(CAbstractRequst request);
}
