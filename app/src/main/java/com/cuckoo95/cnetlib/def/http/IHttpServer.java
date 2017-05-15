package com.cuckoo95.cnetlib.def.http;

import android.content.Context;

import com.cuckoo95.cnetlib.def.http.request.CAbstractRequst;
import com.cuckoo95.cnetlib.def.http.resp.IHttpResp;
import com.cuckoo95.cnetlib.def.http.resp.IRespBase;

import java.util.HashMap;

/**
 * @author Cuckoo
 * @date 2016-09-02
 * @description To manage all http request.
 */
public interface IHttpServer extends IHttpDownloadServer {

    /**
     * Get request by sync.
     *
     * @param method           Http request method. the vlaue is {@link CHttpMethod}
     * @param url
     * @param postParams       Params.
     * @param postParamsByJson 通过json上传参数， 与postParams互斥
     * @param headerMap        Headers
     * @param respObjClass     json中返回data节点
     * @param baseRespClass    json中的首节点，主要用于表示当前请求成功失败与否以及失败原因等。 需要实现{@link IHttpResp}
     * @param isReturnJson     Is Need return json?
     * @param request          The request infos
     * @param <T>
     * @param <E>
     * @return
     */
    <T, E extends IRespBase> E getDataBySync(Context context, CHttpMethod method, String url, HashMap<String, Object> postParams,
                                             String postParamsByJson,
                                             HashMap<String, String> headerMap,
                                             Class<T> respObjClass,
                                             Class<T> baseRespClass,
                                             boolean isReturnJson,
                                             CAbstractRequst request);


    /**
     * Get request by async
     *
     * @param method           Http request method. the vlaue is {@link CHttpMethod}
     * @param url
     * @param postParams       Params.
     * @param postParamsByJson 通过json上传参数， 与postParams互斥
     * @param headerMap        Headers
     * @param respObjClass     json中返回data节点
     * @param baseRespClass    json中的首节点，主要用于表示当前请求成功失败与否以及失败原因等。 需要实现{@link IHttpResp}
     * @param isReturnJson     Is Need return json?
     * @param request          The request infos
     * @param <T>
     * @return
     */
    <T> boolean getData(Context context, CHttpMethod method, String url, HashMap<String, Object> postParams,
                        String postParamsByJson,
                        HashMap<String, String> headerMap,
                        Class<T> respObjClass,
                        Class<T> baseRespClass,
                        boolean isReturnJson,
                        IHttpServerCallback callback, CAbstractRequst request);

}
