package com.cnet.def.http;

import android.content.Context;
import android.widget.Toast;

import com.cnet.R;
import com.cnet.def.caller.IReqCallback;
import com.cnet.def.caller.IReqTipCallback;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.request.CRequestConstants;
import com.cnet.def.http.request.IBaseRequest;
import com.cnet.def.http.resp.IErrResp;
import com.cnet.def.http.resp.IRespBase;
import com.cuckoo95.cutillib.ST;

import java.util.HashSet;

/**
 *
 * @author Cuckoo
 * @date 2016-09-05
 * @description
 *      To manage the callback of {@link ImplHttpServer}
 */
public class HttpServerHelper {
    protected static HashSet<Integer> innerErrStatusSet = null ;

    /**
     * Callback to show loading dialog
     * @param callback
     */
    public static void showLoading(IReqCallback callback){
        if( callback != null ) {
            callback.showLoading();
        }
    }

    /**
     * Callback to show loading dialog
     * @param request
     * @param callback
     */
    public static void showLoading(IBaseRequest request, IReqCallback callback){
        if( callback != null && request != null ) {
            boolean isShowLoading = request.isShowLoadding();
            if (isShowLoading) {
                callback.showLoading();
            }
        }
    }

    /**
     * Callback to show loading dialog and callback request is started
     * @param request
     * @param callback
     */
    public static void showLoadingAndStartRequest(IBaseRequest request, IReqCallback callback){
        if( callback != null && request != null ) {
            boolean isShowLoading = request.isShowLoadding();
            if (isShowLoading) {
                callback.showLoading();
            }
            callback.onRequestStart(request.getMarjorReqeust().getReqCode(),request.getMarjorReqeust());
        }
    }

    /**
     * Callback to dismiss loading dialog
     * @param request
     * @param callback
     */
    public static void dismissLoading(IBaseRequest request, IReqCallback callback){
        if( callback != null && request != null ) {
            boolean isShowLoading = request.isShowLoadding();
            if (isShowLoading) {
                callback.dismissLoading();
            }
        }
    }

    /**
     * Callback to dismiss loading dialog
     * @param callback
     */
    public static void dismissLoading(IReqCallback callback){
        if( callback != null ) {
            callback.dismissLoading();
        }
    }

    /**
     * Callback to request start.
     * @param request
     * @param callback
     */
    public static void onRequestStart(CAbstractRequst request, IReqCallback callback){
        if( callback != null && request != null  ) {
            final int reqCode = request.getReqCode();
            callback.onRequestStart(reqCode,request);
        }
    }

    /**
     * Callback request failure.
     * @param request
     * @param errResp
     * @param callback
     */
    public static void onRequestFailure(CAbstractRequst request, IErrResp errResp, IReqCallback callback){
        if( callback != null && request!= null && errResp != null ){
            callback.onRequestFailure(request.getReqCode(),request, errResp);
        }
    }

    /**
     * Callback request success.
     * @param request
     * @param respObj
     * @param callback
     * @param <T>
     */
    public static<T> void onRequestSuccess(CAbstractRequst request, IRespBase<T> respObj, IReqCallback callback){
        if (callback != null && respObj != null ) {
            callback.onInflateRequest(request.getReqCode(), request,
                    respObj.getRespData(), respObj);
        }
    }

    /**
     * Deal with error response. and show error tip
     * @param context
     * @param errResp
     * @param majorRequest
     * @param reqTipCallback
     */
    public static void respErr(Context context, IErrResp errResp, IBaseRequest majorRequest, CAbstractRequst targetRequest, IReqTipCallback reqTipCallback){
        if( errResp != null && majorRequest != null ){
            if(majorRequest.getShowMsgType() != CRequestConstants.SHOW_MSGTYPE_NONE){
                //Need show error message.
                String message = HttpServerHelper.getErrorMessage(context,errResp,targetRequest);
                if( majorRequest.getShowMsgType() == CRequestConstants.SHOW_MSGTYPE_DIALOG){
                    if(reqTipCallback != null ){
                        reqTipCallback.showTipDialog(message);
                    }
                }else {
                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public static String getErrorMessage(Context context, IErrResp errResp, CAbstractRequst  request){
        if( context == null || errResp == null || request == null ){
            return null ;
        }
        if( isInnerError(errResp.getStatus())){
            return context.getString(R.string.net_request_error) + errResp.getStatus();
        }
        String errMsg = errResp.getErrMsg();
        if(ST.isEmpty(errMsg)){
            //Get default message.
            errMsg = getString(request.getDefaultFailureMsg(),context);
        }
        return errMsg;
    }

    /**
     * Is inner error.
     * @param status
     * @return
     */
    protected static boolean isInnerError(int status){
        if( innerErrStatusSet == null ){
            innerErrStatusSet = new HashSet<>();
            innerErrStatusSet.add(IErrResp.STATUS_RESPNULL);
            innerErrStatusSet.add(IErrResp.STATUS_REQUESTNULL);
            innerErrStatusSet.add(IErrResp.STATUS_RESP_NON200);
            innerErrStatusSet.add(IErrResp.STATUS_BUSINESS_FAILURE);
            innerErrStatusSet.add(IErrResp.STATUS_PARSE_JSON_ERROR);
            innerErrStatusSet.add(IErrResp.STATUS_BASERESPONSE_ERROR);
            innerErrStatusSet.add(IErrResp.STATUS_UNKNOWN);
        }
        return innerErrStatusSet.contains(status);
    }

    /**
     * According resource id to find string.
     * @param resId
     * @param context
     * @return
     */
    public static String getString(int resId, Context context){
        try{
            if( resId != -1 && context != null ){
                return context.getString(resId);
            }
        }catch (Exception e){

        }
        return null ;
    }
}
