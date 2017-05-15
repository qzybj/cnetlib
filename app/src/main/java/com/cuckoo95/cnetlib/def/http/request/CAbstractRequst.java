package com.cuckoo95.cnetlib.def.http.request;

import android.content.Context;

import com.cuckoo95.cnetlib.def.http.CHttpMethod;
import com.cuckoo95.cnetlib.def.http.HttpServer;
import com.cuckoo95.cnetlib.def.http.IHttpServer;
import com.cuckoo95.cnetlib.util.gson.GsonUtil;
import com.cuckoo95.cutillib.CListUtil;
import com.cuckoo95.cutillib.ST;
import com.cuckoo95.cutillib.log.ILog;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Cuckoo
 * @date 2016-09-03
 * @description
 *      The config of http request.
 *
 */
public class CAbstractRequst<T> {
    /****************************************************************/
    /*************************Http config ***************************/
    /****************************************************************/
    /** root url */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient  String baseUrl = null ;
    /** the post url of root url */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient String postFixUrl = null;
    /**Http request method.*/
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient CHttpMethod reqMethod = CHttpMethod.GET;
    /**Is need cache.*/
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient boolean isNeedCache = true ;
    /** connect time out time.*/
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient int connectTimeOut= -1;

    /****************************************************************/
    /*******************Default alert msg if need show***************/
    /****************************************************************/
    /**When request failure and server have not return error message. use this to instead */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient int defaultFailureMsg = -1 ;
    /**When request success and server have not return message. use this to instead */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient int defaultSuccssMsg = -1 ;

    /**Data class */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient Class<T> respObjClass = null  ;
    /**Root class */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient Class<T> respRootClass = null  ;
    /**Http executer, if have not set, used default executer,{@link HttpServer#init(Context, Class, Class, boolean, ILog)} */
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient IHttpServer httpExecuter = null ;
    /**Tag */
    private transient Object tag;
    /**Used to flag every request*/
    @SerializedName(CRequestConstants.TAG_IGNORE)
    private transient int reqCode ;
    /**是否通过JSON的方式上传参数,仅仅用于POST和PUT**/
    private transient boolean isPostJson = false ;

    public  CAbstractRequst(Class<T> respObjClass){
        this.respObjClass = respObjClass;
    }


    /**
     * Get full url.
     * @return
     */
    public String getFullUrl(){
        return ST.f(baseUrl) + ST.f(postFixUrl);
    }

    /**
     * Get all request params and parse to json.
     * @return
     */
    public String getRequestParamsByJson(){
        return GsonUtil.toJson(this);
    }

    /**
     * Get this params to key=value&key=value&key=value....
     * @return
     */
    public String getRequestParamsByKV(){
        StringBuffer sb = new StringBuffer();
        HashMap<String, Object> paramsMap = getRequestParams();
        if(!CListUtil.isEmpty(paramsMap)){
            Iterator<Map.Entry<String,Object>> it = paramsMap.entrySet().iterator();
            Map.Entry<String,Object> entry = null ;
            String value = null ;
            String key = null ;
            while(it.hasNext()){
                entry = it.next();
                key = entry.getKey();
                if(entry.getValue() == null ||
                        entry.getValue() instanceof String){
                    value = (String)entry.getValue();
                }else {
                    new RuntimeException("method:" + ST.f(postFixUrl) +
                            "`s param is not string" +
                            key + ":" + value);
                }
                if(ST.isEmpty(key)){
                    continue;
                }
                if(sb.length() > 0){
                    sb.append("&");
                }
                sb.append(key + "=");
                sb.append(URLEncoder.encode(value));
            }
        }
        return sb.toString();
    }

    /**
     * Get request`s params by hash map
     * @return
     */
    public HashMap<String, Object> getRequestParams() {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        try {
            Field[] fields = getClass().getDeclaredFields();
            Field t_field = null;
            Object value = null;
            //注解信息
            SerializedName annotationInfo=null;
            for (int i = 0; i < fields.length; i++) {
                t_field = fields[i];
                String key = null;
                annotationInfo = t_field.getAnnotation(SerializedName.class);
                if(annotationInfo!=null){
                    key = annotationInfo.value();
                    if(CRequestConstants.TAG_IGNORE.equals(key)){
                        //If current filed have annotation, ignore.
                        continue ;
                    }
                }
                if(ST.isEmpty(key)){
                    key = t_field.getName();
                }

                value = t_field.get(this);
                if (value != null) {
                    paramMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramMap;
    }

    /**
     * Get http request headers
     * @param cnt
     * @return
     */
    public HashMap<String, String> getRequestHeader(Context cnt) {
        if (cnt == null) {
            return null;
        }
        return null;
    }

    public boolean isPostJson() {
        return isPostJson;
    }

    public void setPostJson(boolean postJson) {
        isPostJson = postJson;
    }

    /************************************************************/
    /***********************Get and set method*******************/
    /************************************************************/
    public int getDefaultSuccssMsg() {
        return defaultSuccssMsg;
    }

    public void setDefaultSuccssMsg(int defaultSuccssMsg) {
        this.defaultSuccssMsg = defaultSuccssMsg;
    }

    public CHttpMethod getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(CHttpMethod reqMethod) {
        this.reqMethod = reqMethod;
    }

    public boolean isNeedCache() {
        return isNeedCache;
    }

    public void setNeedCache(boolean needCache) {
        isNeedCache = needCache;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getDefaultFailureMsg() {
        return defaultFailureMsg;
    }

    public void setDefaultFailureMsg(int defaultFailureMsg) {
        this.defaultFailureMsg = defaultFailureMsg;
    }

    public String getPostFixUrl() {
        return postFixUrl;
    }

    public void setPostFixUrl(String postFixUrl) {
        this.postFixUrl = postFixUrl;
    }

    public Class<T> getRespObjClass() {
        return respObjClass;
    }

    public void setRespObjClass(Class<T> respObjClass) {
        this.respObjClass = respObjClass;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public int getReqCode() {
        return reqCode;
    }

    public void setReqCode(int reqCode) {
        this.reqCode = reqCode;
    }

    public Class<T> getRespRootClass() {
        return respRootClass;
    }

    public void setRespRootClass(Class<T> respRootClass) {
        this.respRootClass = respRootClass;
    }

    public IHttpServer getHttpExecuter() {
        return httpExecuter;
    }

    public void setHttpExecuter(IHttpServer httpExecuter) {
        this.httpExecuter = httpExecuter;
    }
}
