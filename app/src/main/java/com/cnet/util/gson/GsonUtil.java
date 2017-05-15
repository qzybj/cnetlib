/*******************************************************************************
 * @project: 
 * @file: GsonUtil.java
 * @author: Cuckoo
 * @created: 2012-05-6
 * @purpose:
 *
 * @version: 1.0
 *
 * Revision History at the end of file.
 *
 * Copyright 2012 Cuckoo All rights reserved.
 ******************************************************************************/
package com.cnet.util.gson;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.cnet.def.http.exception.HttpException;
import com.cuckoo95.cutillib.ST;

public class GsonUtil {
	private static Gson gson = null ;
	
	private static Gson getGson(){
		if( gson == null ){
			gson = new Gson() ;
		}
		return gson ;
	}
	
	/**
	 * Transfer the java bean to JSON string. 
	 * Notice the bean mast can be serialize.
	 * @param bean
	 * @return
	 */
	public static String toJson(Object bean){
		return getGson().toJson(bean);
	}
	
	/**
	 * 将bean转成数组形式
	 * @param bean
	 * @param typeOfSrc
	 * 		Type typeOfSrc = new TypeToken<ArrayList<Object>>(){}.getType();
	 * @return
	 */
	public static String toJson(Object bean,Type typeOfSrc){
		return getGson().toJson(bean, typeOfSrc);
	}
	
	
	/**
	 * 将字符串转成objClass类型的变量
	 * @param json
	 * @param objClass
	 * @return
	 */
	public static <T> T parseJson(String json, final Class<T> objClass){
		return parseJsonByArgumentArray(objClass,json,null);
	}

	/**
	 * 将json转成定级类为rootClass，objClass为rootClass中的泛型
	 * @param rootClass
	 * @param json
	 * @param objClass
	 * @param <T>
	 * @return
	 */
	public static <T> T parseJsonByArgumentArray(Class<T> rootClass, String json,  Class[] objClass){
		if( !ST.isEmpty(json) ){
			try{
				return getGson().fromJson(json,getNewType(rootClass,objClass));
			}catch (JsonSyntaxException e) {
				throw new HttpException("Parse json error" + e.getMessage());
			}
		}
		return null ;
	}

	/**
	 * 将json转成定级类为rootClass，objClass为rootClass中的泛型
	 * @param rootClass
	 * @param json
	 * @param objClass
	 * @param <T>
     * @return
     */
	public static <T> T parseJsonByArgument(Class<T> rootClass, String json,  Class... objClass){
		if( !ST.isEmpty(json) ){
			try{
				return getGson().fromJson(json,getNewType(rootClass,objClass));
			}catch (JsonSyntaxException e) {
				throw new HttpException("Parse json error" + e.getMessage());
			}
		}
		return null ;
	}

	/**
	 * 将root class 和其子class拼成新的泛型
	 * @param rootClass
	 * 		root class
	 * @param argumentClasses
	 * 		root class下的子class
     * @return
     */
	private static ParameterizedType getNewType(final Class rootClass, final Class... argumentClasses) {

		return new ParameterizedType() {
			private ArrayList<Type> argumentTypeList = null ;

			public Type getRawType() {
				return rootClass;
			}

			public Type[] getActualTypeArguments() {
				return getArgumentsTypes();
			}

			public Type getOwnerType() {
				return null;
			}

			private Type[] getArgumentsTypes(){
				if(argumentTypeList == null ){
					argumentTypeList = new ArrayList<>();
					if( argumentClasses != null && argumentClasses.length > 0){
						for(Class argClass: argumentClasses ){
							if( argClass != null ){
								argumentTypeList.add(TypeToken.get(argClass).getType());
							}
						}
					}
				}
				return argumentTypeList.toArray(new Type[0]);
			}
		};
	}

	/**
	 * 
	 * @param json
	 * @param typeOfT
	 * 	Type typeOfSrc = new TypeToken<ArrayList<Object>>(){}.getType();
	 * @return
	 */
	public static <T> T fromJson(String json,  Type typeOfT){
		if( !ST.isEmpty(json) ){
			try{
				return getGson().fromJson(json, typeOfT);
			}catch (JsonSyntaxException e) {
				Log.e("", "Parse json error: " + e.getMessage());
				throw new HttpException("Parse json error" + e.getMessage());
			}
		}
		return null ;
	}
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2012-5-6 17:44:09 by Cuckoo]<BR>
 * Create a util for parse and packaging JSON .
 * Copyright 2011 Cuckoo Systems All rights reserved.
 ******************************************************************************/