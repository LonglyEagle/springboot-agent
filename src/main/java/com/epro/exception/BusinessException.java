package com.epro.exception;

/**
 * 自定义异常
 * @author Grant.You
 *
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -1075526928642754824L;

	public BusinessException(String message) {
		super(message);
	}
}
