package com.cuckoo95.cnetlib.def.http.resp;

import java.io.Serializable;

/**
 *
 * @author Cuckoo
 * @date 2016-09-06
 * @description
 *      The base http response. it have two subclass, {@link IErrResp} and {@link IRespBase}
 */
public interface IHttpResp extends Serializable{

    /**
     * Some specify data in this
     * @return
     */
    Object getTag();
}
