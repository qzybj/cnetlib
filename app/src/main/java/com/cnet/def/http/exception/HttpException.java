/**
 * 
 * 文件描述：
 * May 15, 2012 10:50:34 AM
 *
 */
package com.cnet.def.http.exception;

import com.cnet.def.http.resp.IErrResp;

/**
 *
 * @author Cuckoo
 *
 *
 */
public class HttpException extends RuntimeException implements IErrResp{

	/**  */
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private Object tag;
	/**
	 *
	 * 适用场景：
	 *
	 * @param statusCode
	 * @param detailMessage
	 * @param throwable
	 *
	 */
	public HttpException(int statusCode, String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
		this.statusCode = statusCode;
	}
	
	/**
	 *
	 * 适用场景：
	 *
	 * @param statusCode
	 * @param detailMessage
	 *
	 */
	public HttpException(int statusCode, String detailMessage) {

		super(detailMessage);
		this.statusCode = statusCode;
	}
	

	/**
	 *
	 * 适用场景：
	 *
	 * @param detailMessage
	 * @param throwable
	 *
	 */
	public HttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 *
	 * 适用场景：
	 *
	 * @param detailMessage
	 *
	 */
	public HttpException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 *
	 * 适用场景：
	 *
	 * @param throwable
	 *
	 */
	public HttpException(Throwable throwable) {
		super(throwable);
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	@Override
	public Object getTag() {
		return tag;
	}

	@Override
	public int getStatus() {
		return statusCode;
	}

	@Override
	public String getErrMsg() {
		return getMessage();
	}
}
