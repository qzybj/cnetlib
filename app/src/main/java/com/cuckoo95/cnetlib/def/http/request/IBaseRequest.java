package com.cuckoo95.cnetlib.def.http.request;

import java.util.ArrayList;

/**
 *
 * @author Cuckoo
 * @date 2016-12-27
 * @description
 *      定义请求基类需要实现类内容, 其所有请求通过{@link CAbstractRequst#getReqCode()}标识进行区分
 *
 */

public interface IBaseRequest<T> {


    /**
     * 是否需要显示loading框
     * @return
     */
    boolean isShowLoadding();

    /**
     * 请求失败时，显示消息的类型，其值为{@link CRequestConstants#SHOW_MSGTYPE_NONE}
     * @return
     */
    int getShowMsgType();

    /**
     * 在当前请求之后需要同步执行的请求列表
     * @return
     */
    ArrayList<CAbstractRequst<T>> getSyncReqList();

    /**
     * 获取主请求, 主要用于控制是否显示loading框，以及当出现请求失败时是否显示错误提示
     * @return
     */
    CAbstractRequst<T> getMarjorReqeust();
}
