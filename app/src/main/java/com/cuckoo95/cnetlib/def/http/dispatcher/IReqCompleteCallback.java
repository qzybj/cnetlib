package com.cuckoo95.cnetlib.def.http.dispatcher;

import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.request.IBaseRequest;
import com.cuckoo95.cnetlib.def.http.resp.IErrResp;

/**
 *
 * @author Cuckoo
 * @date 2016-12-26
 * @descripton
 *      请求结束回调
 */

public interface IReqCompleteCallback {
    /**
     * 通知请求结果
     * @param majorReq
     *  主请求，既{@link IBaseRequest#getSyncReqList()}中包含子请求
     * @param subReq
     *  子请求，既{@link IBaseRequest#getSyncReqList()}中的请求
     * @param errResp
     *  错误信息（如果是批量请求，返回的是第一个失败请求的信息），当值不为null，代表请求成功
     */
    void onComplete(IBaseRequest majorReq, CAbstractRequst subReq, IErrResp errResp);
}
