package com.cuckoo95.cnetlib.impl.volley.resp;

import com.cuckoo95.cnetlib.def.http.resp.IRespBase;

/**
 *
 * @author Cuckoo
 * @date 2016-12-28
 * @desciption
 *      当调用方没有设置或者设置错误的JSON所对应的顶级类时，采用当前默认类
 */

public class DefBaseResponse implements IRespBase {
    private boolean isSuccess = false ;
    private String message = null ;
    private int resultStatus ;
    private String json ;
    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getResultStatus() {
        return resultStatus;
    }

    @Override
    public void setResultStatus(int status) {
        this.resultStatus = status;
    }

    @Override
    public Object getRespData() {
        return null;
    }

    @Override
    public String getJson() {
        return json;
    }

    @Override
    public void setJson(String json) {
        this.json = json ;
    }

    @Override
    public Object getTag() {
        return null;
    }
}
