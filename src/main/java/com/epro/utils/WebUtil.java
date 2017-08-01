package com.epro.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web工具类
 *
 */
public class WebUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WebUtil.class);

	/**
	 * 获取项目根路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request) {
		String basePath = null;
		try {
			basePath = getBasePath(request.getSession());
		} catch (Throwable t) {
			LOGGER.error("获取项目根路径出错", t);
		}
		return basePath;
	}

	/**
	 * 根据session获取项目根路径
	 * 
	 * @param session
	 * @return
	 */
	public static String getBasePath(HttpSession session) {
		String basePath = null;
		try {
			basePath = session.getServletContext().getRealPath("");
		} catch (Throwable t) {
			LOGGER.error("根据session获取项目根路径出错", t);
		}
		return basePath;
	}

	/**
	 * 获取项目访问基URL
	 * 
	 * @param request
	 * @return
	 */
	public static String getBaseUrl(HttpServletRequest request) {
		String baseUrl = null;
		try {
			String schemem = request.getScheme();
			String serverName = request.getServerName();
			int serverPort = request.getServerPort();
			String contentPath = request.getContextPath();
			if (serverPort == 80) {
				baseUrl = schemem + "://" + serverName + contentPath;
			} else {
				baseUrl = schemem + "://" + serverName + ":" + serverPort + contentPath;
			}
		} catch (Throwable t) {
			LOGGER.error("获取项目访问基URL出错", t);
		}

		return baseUrl;
	}

	/**
	 * 获取访问相对URL
	 * 
	 * @param request
	 * @return
	 */
	public static String getRelativePath(HttpServletRequest request) {
		try {
			String requestPath = request.getRequestURI();
			if (StringUtils.isBlank(requestPath)) {
				requestPath = "";
			}
			String contentPath = request.getContextPath();
			if (StringUtils.isNotBlank(contentPath) && StringUtils.isNotBlank(requestPath)) {
				requestPath = requestPath.replaceFirst(contentPath, "");
			}
			String queryStr = request.getQueryString();
			if (StringUtils.isBlank(queryStr)) {
				return requestPath;
			}
			return requestPath + "?" + queryStr;
		} catch (Throwable t) {
			LOGGER.error("获取访问相对URL出错", t);
			return null;
		}
	}

	/**
	 * 获取访问URL
	 * 
	 * @param request
	 * @return
	 */
	public static String getTargetUrl(HttpServletRequest request) {
		String targetUrl = null;

		try {
			targetUrl = request.getRequestURL().toString();
			String queryStr = request.getQueryString();
			if (StringUtils.isNotBlank(queryStr)) {
				targetUrl = targetUrl + "?" + queryStr;
			}
		} catch (Throwable t) {
			LOGGER.error("获取访问URL出错", t);
		}

		return targetUrl;
	}

	/**
	 * 获取客户端请求IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		try {
			if (null == request) {
				LOGGER.error("请求对象为空，无法获取客户端IP请求地址");
				return null;
			}
			String ip = "";
			String[] headers = {"x-forwarded-for","Proxy-Client-IP","WL-Proxy-Client-IP"};
			
			for (int i = 0; i <= headers.length;i++)
			{
				 ip = i < headers.length?request.getHeader(headers[i]):request.getRemoteAddr();
				 
				 if(ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)){
						break;
				 }
			}

			ip = ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;

			if(ip.equals("127.0.0.1")){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					LOGGER.error("根据网卡取本机配置的IP出错", e);
				}
				ip= inet.getHostAddress();
			}

			//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
			if(ip!=null && ip.indexOf(",")>0){
				ip = ip.substring(0,ip.indexOf(","));
			}

			return ip;
			
		} catch (Throwable t) {
			LOGGER.error("获取客户端请求IP出错", t);
			return null;
		}
	}

}
