package com.cuckoo95.cnetlib.def.http.request;

/**
 *
 * @author Cuckoo
 * @date 2016-09-01
 * @description
 *      Http request`s constants
 */
public class CRequestConstants {
    //The field will be ignore int http request
    public static final String TAG_IGNORE = "ignore_field";
    /**When request complete will not show success or failure messag.*/
    public static final int SHOW_MSGTYPE_NONE = 1 ;
    /**Use toast to show success or failure message when request complete.*/
    public static final int SHOW_MSGTYPE_TOAST = 2 ;
    /**Use dialog to show success or failure message when request complete.*/
    public static final int SHOW_MSGTYPE_DIALOG = 3 ;

}
