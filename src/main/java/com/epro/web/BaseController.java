package com.epro.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epro.domain.ResponseResult;
import com.epro.utils.WebUtil;

@ControllerAdvice
public class BaseController{

	private static final Logger logger = LoggerFactory
			.getLogger(BaseController.class);


	/**
	 * 统一异常处理
	 * 
	 * @param request
	 * @param t
	 * @return
	 */
	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	public ResponseResult<String> exp(HttpServletRequest request, Throwable t) {
		logger.error(String.format("发生未知错误/异常：访问IP地址=%s", WebUtil.getClientIp(request)), t);
		ResponseResult<String> result = new ResponseResult<String>();
		result.setResultCode("-1");
		result.setMessage(t.getMessage());
		return result;
	}

}
