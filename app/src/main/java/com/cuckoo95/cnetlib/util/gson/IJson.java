/*******************************************************************************
 * @project: 
 * @file: IJson.java
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


public interface IJson {
	/**
	 * Packaging the current object to JSON.
	 * @return
	 */
	String toJson(Object bean);
	
	/**
	 * Parse the JSON by current object.
	 * @param json
	 * @return
	 */
	<T> Object parseJson(String json);
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2012-5-6 18:28:09 by Cuckoo]<BR>
 * Create an interface for parse and packaging JSON
 * Copyright 2011 Cuckoo Systems All rights reserved.
 ******************************************************************************/