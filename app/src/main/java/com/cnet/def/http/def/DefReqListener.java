package com.cnet.def.http.def;

import com.cnet.def.caller.IReqCallback;
import com.cnet.def.caller.IReqTipCallback;
import com.cnet.def.http.request.CAbstractRequst;
import com.cnet.def.http.resp.IErrResp;
import com.cnet.def.http.resp.IRespBase;

/**
 *
 * @author Cuckoo
 * @date 2016-09-02
 * @description
 *      Monitior status of network request.
 */
public class DefReqListener implements IReqCallback {
    private IReqTipCallback tipCallback = null ;
    public DefReqListener(IReqTipCallback tipCallback){
        this.tipCallback = tipCallback;
    }

    /*****************************************************/
    /**********************Request callback******************/
    /*****************************************************/

    @Override
    public void onRequestStart(int reqCode, CAbstractRequst request) {

    }

    @Override
    public <T> void onInflateRequest(int reqCode, CAbstractRequst request, T respDataObj, IRespBase<T> resp) {

    }

    @Override
    public void onRequestFailure(int reqCode, CAbstractRequst request, IErrResp errResp) {

    }


    /*****************************************************/
    /**********************Loading dialog******************/
    /*****************************************************/
    @Override
    public void showLoading() {
        if( tipCallback != null ){
            tipCallback.showLoading();
        }
    }

    @Override
    public void dismissLoading() {
        if( tipCallback != null ){
            tipCallback.dismissLoading();
        }
    }

    @Override
    public void showTipDialog(String message) {
        if( tipCallback != null ){
            tipCallback.showTipDialog(message);
        }
    }

}
