package com.cuckoo95.cnetlib.def.caller;

import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;

/**
 *
 * @author Cuckoo
 * @date 2016-12-26
 * @description
 *      重构请求参数， 主要用于同步请求多个接口时，
 *      后面的接口依赖于前一个接口的结果
 *
 */

public interface IReqRefactor {
    /**
     * 重新更改请求参数，如果不需要更改返回null
     * @param reqCode
     *      接口请求标示码
     * @param originalRequest
     *      原始请求信息
     * @return
     *  返回更改之后的请求参数。 如果不需要更改返回null
     */
    CAbstractRequst onRequestRefactor(int reqCode, CAbstractRequst originalRequest);
}
