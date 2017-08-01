package com.epro.constant;

import com.epro.utils.UUIDGenerator;
/**
 * 默认配置参数
 * @author Grant.You
 *
 */
public enum DefaultConfigEnum {
	;
	public static String LOCAL_IP = "127.0.0.1";
	public static final String DEFAULT_PORT = "8082";
	public static final String DEFAULT_SERVER_TYPE = "slave";
	public static String WEIGHT = "2:1:0";
	public static long INTERVAL = 600;
	public static boolean IGNOREAUTH = true;
	public static final String SLAVEAGENTNO = UUIDGenerator.getUUID();
}
