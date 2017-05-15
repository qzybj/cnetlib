/*******************************************************************************
 * @project:
 * @file: ST.java
 * @author: Cuckoo
 * @created: 2012-03-28
 * @purpose:
 *
 * @version: 1.0
 *
 * Revision History at the end of file.
 *
 * Copyright 2012 Cuckoo All rights reserved.
 ******************************************************************************/
package com.cuckoo95.cnetlib.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.cuckoo95.cutillib.ST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class StringUtil {
	public static final String UTF_8 = "utf-8";
	
	public final static String ZERO = "0";
	
	public final static String EMPTY = "";
	
	/**
	 * Check current string is null.
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		return str == null ;
	}
	
	/**
	 * Check current string is empty.
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if( str != null && str.trim().length() > 0){
			return false ;
		}
		return true ;
	}

    /**
     * 判断EditText是否为空
     * @param et
     * @return
     */
    public static boolean isEmpty(EditText et){
        if( et != null){
            return isEmpty(et.getText().toString());
        }
        return true ;
    }
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	/**
	 * Filter string.
	 * @param str
	 * @return
	 */
	public static String f(String str){
		if( isEmpty(str)){
			return "" ;
		}
		return str.trim();
	}

	/**
	 * 如果字符串为空或者空串 用0代替
	 * @param target
	 * @return
	 */
	public static String parseEmpty2Zero(String target){
		if( isEmpty(target)){
			return ZERO;
		}
		return target;
	}
	
	public static int getLength(String str){
		return f(str).length();
	}

	/**
	 * 获取EditText的长度
	 * @param target
	 * @return
	 */
	public static int getLength(EditText target){
		if( target == null ){
			return 0 ;
		}
		return f(target.getText().toString()).length();
	}
	
	/**
	 * According the id to find the string.
	 * @param context
	 * @param id
	 * @return
	 */
	public static String getStringByID(Context context, int id){
		try{
			return context.getResources().getString(id);
		}catch (Exception e) {
			Log.e("", e.getMessage());
			return null ;
		}
	}

	/**
	 * 获取EditText内容
	 * @param et
	 * @return
	 */
	public static String getString(EditText et){
		if( et == null ){
			return "";
		}
		return ST.f(et.getText().toString());
	}

	/**
	 * @author Vanceinfo
	 * @return 返回当前日期
	 * @throws ParseException
	 * 
	 */
	public static Date getCurrentDate() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String date = sdf.format(new Date());
		return sdf.parse(date);
	}
	/**
	 * @author Vanceinfo
	 * @param currentDate
	 * @param date2
	 * @return 比较2个日期相差多少天
	 */
	public static int compareDate(Date currentDate,Date date2){
		long day = 24L*60L*60L*1000L;
		return (int)((date2.getTime()-currentDate.getTime())/day);
	}

	/**
	 * 将字符串转成整型，转换失败返回-1
	 * @param value
	 * @return
	 */
	public static int parseInt(String value){
		try{
			return Integer.parseInt(ST.f(value));
		}catch (Exception e){

		}
		return -1;
	}

	/**
	 * 将字符串转成double
	 * @param value
	 * @return
	 */
	public static double parseDouble(String value){
		try{
			return Double.parseDouble(ST.f(value));
		}catch (Exception e){

		}
		return 0;
	}

	/**
	 * 将字符串转成long
	 * @param value
	 * @return
	 */
	public static long parseLong(String value){
		try{
			return Long.parseLong(ST.f(value));
		}catch (Exception e){

		}
		return 0;
	}

	public static Date changeString2Date(String str) {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			return sdf.parse(str);
		}catch (Exception e) {
			
		}
		return null ;
	}
	
	/**
	 * Transfer the number to the specify length string.
	 * @param num
	 * @param maxCount
	 * @param prefix
	 * @return
	 */
	public static String transferNum2String(int num, int maxCount, String prefix ){
		String str = num +"";
		for(int i = 0 ;i < maxCount ;i ++){
			if( str.length() < maxCount){
				str = prefix + str ;
			}else {
				break ;
			}
		}
		return str;
	}
	
	/**
	 * 
	 * 方法说明：判断给定字符串，非NULL，非空。
	 * 
	 * @param str
	 * @return
	 * 
	 */
	public static boolean isEmptyAndBlank(String str) {

		return (str == null) || str.trim().equals("null")
				|| (str.trim().length() < 1);
	}
	
	/**
	 * 字符串连接工具，避免使用String+问题<br/>
	 * 只支持 String对象以及普通类型
	 * 
	 * @param str
	 * @return
	 */
	public static String join(Object... str) {
		StringBuilder _temp = null;
		try {
			if (str != null && str.length > 0) {
				_temp = new StringBuilder();
				for (Object _s : str) {
					if (_s != null) {
						_temp.append(_s.toString());
					}
				}
				return _temp.toString();
			}
		} catch (Exception e) {

		} finally {
			_temp = null;
		}
		return "";
	}
	
	/**
	 * 计算字符串长度， 中文占2位
	 * @param s
	 * @return
	 */
	public static int getStringLength(String s){
		if(ST.isEmpty(s)){
			return 0 ;
		}
		int count = s.length() ;
		for(int i = 0 ;i < s.length() ;i ++){
			if( isChinese(s.charAt(i))){
				count ++;
			}
		}
		return count;
	}
	
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

	/**
	 * 检测正则是否匹配
	 * @param regex
	 * @param targetString
	 * @return
	 */
	public static final boolean matcher(String regex, CharSequence targetString){
		return Pattern.matches(regex,targetString);
	}

	public static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * Get exception message.
	 * @param e
	 * @return
     */
	public static String getExceptionMessage(Exception e){
		if( e != null ){
			return e.getMessage();
		}
		return null ;
	}
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2012-3-28 21:42:09 by Cuckoo]<BR>
 * Create a tool for String.
 * Copyright 2011 Cuckoo Systems All rights reserved.
 ******************************************************************************/