package com.cnet.impl.volley.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cnet.util.HttpHeaderUtil;
import com.cnet.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cuckoo
 * @date 2016-09-06
 * @description
 *      Custome StringRequest
 */
public class CStringReqeust extends StringRequest {
    private HashMap<String,String> postParams = null ;
    private HashMap<String,String> headerMap = null ;
    //通过JSON方式上传数据
    private String postJson = null ;
    public CStringReqeust(int method, String url, HashMap<String, Object> postParams,
                          String postParamsByJson,
                          HashMap<String, String> headerMap,
                          Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.postParams = (HashMap)postParams;
        this.headerMap = headerMap;
        this.postJson = postParamsByJson;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeaderUtil.appendAllHeader(headerMap);
    }

    @Override
    protected Map<String, String> getParams() {
        HashMap<String, String> map = new HashMap<String, String>();
        if( postParams != null ){
            map.putAll(postParams);
        }
        return map;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if(!StringUtil.isEmpty(postJson)){
            //已JSON方式上传数据
            return postJson.getBytes();
        }
        return super.getBody();
    }
}
