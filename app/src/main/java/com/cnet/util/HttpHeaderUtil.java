package com.cnet.util;

import java.util.HashMap;
import com.cuckoo95.cutillib.CListUtil;


/**
 *
 * @author Cuckoo
 * @date 2016-09-07
 * @description
 *      Used to manage http header
 */
public class HttpHeaderUtil {
    private static HashMap<String, String> defauleHeaderMap = null ;

    /**
     * Get default headers
     * @return
     */
    public static HashMap<String,String> getDefauleHeaderMap(){
        if(defauleHeaderMap == null ){
            defauleHeaderMap = new HashMap<>();
            HashMap<String, String> header = new HashMap<String, String>();
            header.put("content-Type", "application/json; charset=UTF-8");
        }
        return defauleHeaderMap;
    }

    /**
     * Append default header and customer header.
     * @param customerHeaderMap
     * @return
     */
    public static HashMap<String,String> appendAllHeader(HashMap<String,String> customerHeaderMap){
        HashMap<String, String> allHeaderMap = new HashMap<>();
        allHeaderMap.putAll(getDefauleHeaderMap());
        if(!CListUtil.isEmpty(customerHeaderMap)){
            allHeaderMap.putAll(customerHeaderMap);
        }
        return allHeaderMap;
    }
}
