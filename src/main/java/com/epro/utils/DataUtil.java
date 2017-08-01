package com.epro.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epro.web.DeviceAuthController;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 数据操作类
 * @author Grant.You
 *
 */
public class DataUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeviceAuthController.class);
	
	/**
	 * 将对象转为json
	 * @param obj
	 * @return
	 */
	public static String ObjectToJson(Object obj){  
		String json = null;
		try {
			if (null == obj) {
				return null;
			}
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(obj);
		} catch (Throwable t) {
			LOGGER.error("将对象转换为JSON字符串出错", t);
		}
		return json;
    }
	
}
