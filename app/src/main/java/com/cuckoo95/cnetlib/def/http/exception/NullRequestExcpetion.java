/*******************************************************************************
 * @file: NullRequestExcpetion.java
 * @author: Cuckoo
 * @created: 2012-2-6
 * @purpose:
 *
 * @version: 1.0
 *
 * Revision History at the end of file.
 *
 * Copyright 2012 Cuckoo All rights reserved.
 ******************************************************************************/
package com.cuckoo95.cnetlib.def.http.exception;

public class NullRequestExcpetion extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String msg = "Null request.";
	public NullRequestExcpetion() {
		super(msg);
	}
	
	public String getMessage() {
		return msg;
	}
}
/*******************************************************************************
 * Revision History [type 'revision' & press Alt + '/' to insert revision block]
 * 
 * [Revision on 2012-2-6 13:38:09 by Cuckoo]<BR>
 *  Define an exception for null request.
 * Copyright 2012 Cuckoo Systems All rights reserved.
 ******************************************************************************/
