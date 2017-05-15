package com.cuckoo95.cnetlib.def.http.resp;

/**
 *
 * @author Cuckoo
 * @date 2016-09-05
 * @description
 *      Error info when reqeust failure.
 */
public interface IErrResp extends IHttpResp{
    int STATUS_RESPNULL = 1000;
    int STATUS_REQUESTNULL = 1001;
    int STATUS_BUSINESS_FAILURE = 1002;
    int STATUS_RESP_NON200 = 1003;
    int STATUS_PARSE_JSON_ERROR = 1004;
    int STATUS_BASERESPONSE_ERROR = 1005;
    //不支持当前请求类型
    int STATUS_REQUEST_UNSUPPORT = 1006;
    //未知异常
    int STATUS_UNKNOWN = 1007;



    /**
     * Get status fo interface.
     * @return
     */
    int getStatus();

    /**
     * Get error message.
     * @return
     */
    String getErrMsg() ;
}
