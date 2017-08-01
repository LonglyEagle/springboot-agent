package com.epro.domain;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable{
	
	private static final long serialVersionUID = -280417396558372641L;

	/**
	 * "响应码：0表示成功，其他表示失败"
	 */
	private String resultCode;
	
	/**
	 * 响应描述
	 */
	private String message;
	
	/**
	 * 返回数据
	 */
	private T data;
	
	public ResponseResult() {
		super();
	}

	public ResponseResult(String resultCode, String message, T data) {
		super();
		this.resultCode = resultCode;
		this.message = message;
		this.data = data;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	
}
