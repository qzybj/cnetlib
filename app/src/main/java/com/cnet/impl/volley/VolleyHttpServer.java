package com.cnet.impl.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.cnet.def.caller.IDownloadCallback;
import com.cnet.def.http.CHttpMethod;
import com.cnet.def.http.IHttpServer;
import com.cnet.def.http.IHttpServerCallback;
import com.cnet.def.http.exception.HttpException;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.resp.IErrResp;
import com.cnet.def.http.resp.IHttpResp;
import com.cnet.def.http.resp.IRespBase;
import com.cnet.impl.volley.request.CMultiRequest;
import com.cnet.impl.volley.request.CStringReqeust;
import com.cnet.impl.volley.resp.DefBaseResponse;
import com.cuckoo95.cutillib.CListUtil;
import com.cuckoo95.cutillib.ST;
import com.cnet.util.gson.GsonUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Cuckoo
 * @date 2016-09-03
 * @description Use volley to get data.
 */
public class VolleyHttpServer implements IHttpServer {
    private static HashMap<CHttpMethod, Integer> supportMethodMap = null;
    //Just use to post string.
    private static RequestQueue requestQueue = null ;

    static {
        supportMethodMap = new HashMap<>();
        supportMethodMap.put(CHttpMethod.GET, Request.Method.GET);
        supportMethodMap.put(CHttpMethod.POST, Request.Method.POST);
        supportMethodMap.put(CHttpMethod.PUT, Request.Method.PUT);
        supportMethodMap.put(CHttpMethod.DELETE, Request.Method.DELETE);
        supportMethodMap.put(CHttpMethod.HEAD, Request.Method.HEAD);
        supportMethodMap.put(CHttpMethod.OPTIONS, Request.Method.OPTIONS);
        supportMethodMap.put(CHttpMethod.TRACE, Request.Method.TRACE);
        supportMethodMap.put(CHttpMethod.PATCH, Request.Method.PATCH);
    }

    /**
     * Json request just in same queue
     * @param context
     * @return
     */
    private static RequestQueue getRequestQueue(Context context){
        if( requestQueue == null ){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    @Override
    public <T, E extends IRespBase> E getDataBySync(Context context, CHttpMethod method, String url,
                                                    HashMap<String, Object> postParams,
                                                    String postParamsByJson,
                                                    HashMap<String, String> headerMap,
                                                    Class<T> respObjClass,
                                                    Class<T> baseRespClass,boolean isReturnJson,
                                                    CAbstractRequst request) {
        String returnJson = null;
        HttpException errException = null;
        try {
            RequestFuture future = RequestFuture.newFuture();
            Request req = parseRequest(method, url, postParams, postParamsByJson, headerMap, future, future);
            getRequestQueue(context).add(req);
            returnJson = (String) future.get();
        } catch (Exception e) {
            errException = new HttpException(IErrResp.STATUS_RESP_NON200,
                    ST.getExceptionMessage(e));
        }
        return (E) parse2Result(returnJson, respObjClass, baseRespClass,errException, isReturnJson);
    }

    @Override
    public <T> boolean getData(Context context, CHttpMethod method, String url,
                               HashMap<String, Object> postParams,
                               String postParamsByJson,
                               HashMap<String, String> headerMap,
                               final Class<T> respObjClass,
                               final Class<T> baseRespClass,
                               final boolean isReturnJson, final IHttpServerCallback callback,
                               final CAbstractRequst request) {
        Request req = parseRequest(method, url
                , postParams, postParamsByJson, headerMap, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (callback != null) {
                            IRespBase<T> resp = parse2Result(response, respObjClass, baseRespClass,null, isReturnJson);
                            callback.onResponse(resp, request);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback != null) {
                            HttpException exception = new HttpException(IErrResp.STATUS_RESP_NON200,
                                    error.getMessage());
                            exception.setTag(error);
                            callback.onErrResponse(exception, request);
                        }
                    }
                });
        boolean result = true ;
        if( req instanceof CStringReqeust ||
                req instanceof CMultiRequest){
            getRequestQueue(context).add(req);
        }else {
            result = false;
        }
        return result;
    }

    /**
     * 下载文件
     * @param context
     * @param url
     *      Url地址
     * @param destinationPath
     *      下载文件存放路径
     * @param  isAllow3G
     *      是否允许3G下下载
     * @param  downloadCallback
     *      下载回调
     * @return
     */
    @Override
    public boolean downloadFile(Context context, String url, String destinationPath,
                                boolean isAllow3G, IDownloadCallback downloadCallback) {
        return new DownloadFileServer().downloadFile(context,url,destinationPath,isAllow3G,downloadCallback);
    }

    /**
     * Parse result to {@link IRespBase}
     * @param json
     * @param respObj
     *      json中返回data节点所对应的类
     * @param baseRespClass
     *      json中的首节点，主要用于表示当前请求成功失败与否以及失败原因等。 需要实现{@link IHttpResp}
     * @param errResp
     * @param isReturnJson
     * @param <T>
     * @return
     */
    private <T> IRespBase<T> parse2Result(String json, Class<T> respObj,
                                          Class<T> baseRespClass,
                                          IErrResp errResp, boolean isReturnJson) {
        IRespBase<T> response = null;
        int errCode = IErrResp.STATUS_RESP_NON200;
        String message = null;
        boolean isError = false;
        if (errResp != null) {
            //Request error.
            errCode = errResp.getStatus();
            message = errResp.getErrMsg();
            isError = true;
        } else {
            if (ST.isEmpty(json)) {
                errCode = IErrResp.STATUS_RESPNULL;
                isError = true;
            } else if (respObj == null) {
                errCode = IErrResp.STATUS_REQUESTNULL;
                isError = true;
            }
        }

        if (!isError) {
            //Parse json

            try {
                response = (IRespBase<T>)GsonUtil.parseJsonByArgument(baseRespClass,json,respObj);
                if (isReturnJson) {
                    response.setJson(json);
                }
            } catch (Exception e) {
                //parse json error.
                errCode = IErrResp.STATUS_PARSE_JSON_ERROR;
                message = ST.getExceptionMessage(e);
            }
        }
        if (response == null) {
            try {
                response = (IRespBase<T>)baseRespClass.newInstance();
            } catch (Exception e) {
                message = ST.getExceptionMessage(e);
                errCode = IErrResp.STATUS_BASERESPONSE_ERROR;
            }
            if( response == null ){
                response = new DefBaseResponse();
            }
            response.setResultStatus(errCode);
            response.setMessage(message);
        }
        return response;
    }

    /**
     * Pase params to Volley request.
     *
     * @param method
     * @param url
     * @param postParams
     * @param headerMap
     * @param listener
     * @param errorListener
     * @return
     */
    private Request parseRequest(CHttpMethod method, String url,
                                 HashMap<String, Object> postParams,
                                 String postParamsByJson,
                                 HashMap<String, String> headerMap,
                                 Response.Listener listener,
                                 Response.ErrorListener errorListener) {
        //Check postParams is all String
        boolean isNeedPostFiles = isNeedPostFiles(postParams);
        if (!isNeedPostFiles) {
            return new CStringReqeust(getMethod(method), url,
                    postParams, postParamsByJson, headerMap,
                    listener, errorListener);
        } else {
            return new CMultiRequest(getMethod(method), url,
                    postParams, headerMap,
                    listener, errorListener);
//            return new CMultiPartRequest(getMethod(method),url,
//                    postParams,headerMap,listener,errorListener);
        }
    }

    /**
     * Get http method
     *
     * @param httpMethod
     * @return
     */
    private int getMethod(CHttpMethod httpMethod) {
        if (httpMethod != null &&
                supportMethodMap.containsKey(httpMethod)) {
            return supportMethodMap.get(httpMethod);
        }
        throw new HttpException("Can not support http method:" + httpMethod);
    }

    /**
     * Check is neet upload files.
     * The file type likes: File, Bitmap
     *
     * @param postParams
     * @return
     */
    private boolean isNeedPostFiles(HashMap<String, Object> postParams) {
        if (!CListUtil.isEmpty(postParams)) {
            Iterator<Map.Entry<String, Object>> it = postParams.entrySet().iterator();
            Object value = null;
            while (it.hasNext()) {
                value = it.next().getValue();
                if (!(value instanceof String)) {
                    return true;
                }
            }
        }
        return false;
    }
}
