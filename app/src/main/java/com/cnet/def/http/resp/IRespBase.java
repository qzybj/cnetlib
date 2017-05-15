package com.cnet.def.http.resp;

/**
 *
 * @author Cuckoo
 * @date 2016-09-01
 * @description
 *      The base information of returned form server.
 *      The have request`s success or failure status, and some message from server.
 */
public interface IRespBase<T> extends IHttpResp{
    /**
     * Request result.
     * @return
     *      true: success<br>
     *      false:failure<br>
     */
    boolean isSuccess();

    /**
     * Some message from server.
     * @return
     */
    String getMessage();

    void setMessage(String message);

    /**
     * Get result status.
     * @return
     */
    int getResultStatus();

    void setResultStatus(int status);

    /**
     * Data object.
     * @return
     */
    T getRespData();

    /**
     * Return json
     * @return
     */
    String getJson();

    /**
     * Set json
     * @param json
     */
    void setJson(String json);
}
