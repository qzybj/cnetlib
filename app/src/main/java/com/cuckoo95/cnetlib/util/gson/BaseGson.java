/*******************************************************************************
 * @project: 
 * @file: BaseGosn.java
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
package com.cuckoo95.cnetlib.util.gson;

import java.io.Serializable;


public class BaseGson implements Serializable {
	
	/**
	 * Parse JSON
	 */
	public static <T> Object parseJson(String json, Class<T> objClass){
		return GsonUtil.parseJson(json, objClass);
	}
	
	/**
	 * Packaging java bean to JSON.
	 */
	public String toJson() {
		return GsonUtil.toJson(this);
	}
	
	/**
	 * According the response format, we must implement it.
	 * @return
	 */
	public Object getData(){
		return null ;
	}
	
	/**
	 * According the response format, we must implement it.
	 * @return
	 */
	public int getStatus() {
		return -1;
	}
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2012-5-6 18:23:09 by Cuckoo]<BR>
 * Create a base class for parse and packaging JSON.
 * Copyright 2011 Cuckoo Systems All rights reserved.
 ******************************************************************************/