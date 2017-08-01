package com.epro.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epro.exception.BusinessException;

import net.sf.json.JSONObject;  


/**
 * @author Grant.You
 * HttpClient工具类
 */
public class HttpClientUtil {  
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpClientUtil.class);

	private static PoolingHttpClientConnectionManager cm;  
	private static String EMPTY_STR = "";  
	private static String CHARSET = "UTF-8";  


	/**
	 * 初始化连接池信息
	 */
	private static void init() {  
		if (cm == null) {  
			cm = new PoolingHttpClientConnectionManager(); 
			cm.setMaxTotal(50);// 整个连接池最大连接数  
			cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2  
		}  
	}  

	/** 
	 * 通过连接池获取HttpClient 
	 *  
	 * @return 
	 */  
	private static CloseableHttpClient getHttpClient() {  
		init();  
		return HttpClients.custom().setConnectionManager(cm).build();  
	}  

	/** 
	 * 转发请求到目标服务器。 
	 *  
	 * @param request 
	 * @param targetUrl 
	 * @throws IOException 
	 */  
	public static String send(HttpServletRequest request, 
			String targetUrl) throws BusinessException{
		Map<String,Object> headers = getRequestHeader(request);
		
		switch(request.getMethod()){
		case "GET":
			try {
				return httpGetRequest(targetUrl, headers);
			} catch (URISyntaxException e) {
				LOGGER.error("请联系管理员！", e);
				throw new BusinessException("未知错误，请联系管理员！");
			}

		case "POST":
			try {
				String param = getRequestParam(request);
				return httpPostJsonRequest(targetUrl,headers,param);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("请联系管理员！", e);
				throw new BusinessException("未知错误");
			}
		default:
			return null;
		}
	}
	
	/**
	 * 获取请求头信息
	 * @param req
	 * @return
	 */
	private static Map<String,Object> getRequestHeader(HttpServletRequest req){
		
		Map<String,Object> headers = new HashMap<String,Object>();
		Enumeration<String> headerNames = req.getHeaderNames();
		
		while(headerNames.hasMoreElements())
		{
			String headerName = headerNames.nextElement();
			//取出token以及Axb-开头的请求头信息
			if ("token".equals(headerName) || headerName.startsWith("axb-")){
				String headerValue = req.getHeader(headerName);
				headers.put(headerName, headerValue);
			}
		}
		
		//在请求头添加客户端ip信息
		headers.put("axb-client", WebUtil.getClientIp(req));
		LOGGER.debug("getRequestHeader response:{}",headers);
		return headers;
	}
	
	/**
	 * 发送get方式请求，带参数
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> headers) 
			throws URISyntaxException, BusinessException {  
		LOGGER.debug("httpGetRequest request: url:{},headers:{}",url,DataUtil.ObjectToJson(headers));

		if (StringUtils.isBlank(url))
		{
			return null;
		}

		HttpGet httpGet = new HttpGet(url);  

		if(!headers.isEmpty()){
			for (Map.Entry<String, Object> header : headers.entrySet()) {  
				httpGet.addHeader(header.getKey(), String.valueOf(header.getValue()));  
			} 
		}

		String result = getResult(httpGet);
		LOGGER.info("httpGetRequest response:{}", result);

		return result;  
	}  


	/**
	 * 发送post请求，带请求头和json请求内容
	 * @param url
	 * @param headers
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostJsonRequest(String url, Map<String, Object> headers, String param)  
			throws UnsupportedEncodingException, BusinessException {  
		LOGGER.debug("httpPostJsonRequest request param: url:{},headers:{},reqbody:{}",
				url, DataUtil.ObjectToJson(headers), param);
		
		System.err.println(param);
		if (StringUtils.isBlank(url)){
			throw new BusinessException("请求路径无效，请确认请求路径或检查路径映射关系表");
		}
		
		HttpPost httpPost = new HttpPost(url);

		if (!headers.isEmpty())
		{
			for (Map.Entry<String, Object> header : headers.entrySet()) {  
				httpPost.addHeader(header.getKey(), String.valueOf(header.getValue()));  
			}  
		}

		StringEntity requestEntity = new StringEntity(param,CHARSET);  
		requestEntity.setContentEncoding(CHARSET);                
		httpPost.setHeader("Content-type", "application/json");  
		httpPost.setEntity(requestEntity);  
		
		String result = getResult(httpPost);
		LOGGER.info("httpPostJsonRequest response:{}",result);

		return result; 
	}  

	/** 
	 * 处理Http请求 
	 *  
	 * @param request 
	 * @return 
	 */  
	private static String getResult(HttpRequestBase request) throws BusinessException{  

		CloseableHttpClient httpClient = getHttpClient();  
		try {  
			CloseableHttpResponse response = httpClient.execute(request);

			HttpEntity entity = response.getEntity(); 

			if (entity != null) {  

				String result = EntityUtils.toString(entity);  
				response.close();   

				LOGGER.debug("getResult response:{}", result);

				return result;  
			}  

		} catch (ClientProtocolException e) {  
			LOGGER.error("请联系管理员！", e);
			throw new BusinessException("未知错误，请联系管理员！");
		} catch (IOException e) {  
			LOGGER.error("请联系管理员！", e);
			throw new BusinessException("未知错误，请联系管理员！");
		} 

		return EMPTY_STR;  
	}  
	
	/**
	 * 获取post请求参数，并返回json格式字符串
	 * @param req
	 * @return
	 */
	public static String getRequestParam(HttpServletRequest req) throws BusinessException {
		String contentType = GetContentTypeOfHeaders(req);
		String json = "";

		if (contentType == null)
			LOGGER.debug("content-type is null::::", contentType);
		else if ((contentType.equalsIgnoreCase("application/json;charset=utf-8"))
				|| (contentType.equalsIgnoreCase("text/plain;charset=utf-8"))) {
			json = readRequestStream(req);
		} else if ((contentType.contains("application/x-www-form-urlencoded"))
				|| (contentType.contains("multipart/form-data"))) {
			json = readRequestParameterMap(req);
		} else
			LOGGER.debug("can't support this content-type::::" + contentType);

		LOGGER.debug("getRequestParam response:{}", json);
		return json;
	}
	/*public static String getRequestParam(HttpServletRequest req) throws BusinessException{
		String contentType = req.getHeader("Content-Type");
		String json = "";
		if (contentType != null && contentType.contains("text")) {
			json = readRequestStream(req);
		} else {
			json = readRequestParameterMap(req);
		}
		
		LOGGER.debug("getRequestParam response:{}",json);
		return json;
	}*/
	
	/**
	 * 获取表单及json格式请求参数获取方式
	 * @param request
	 * @param targetUrl
	 * @return
	 */
	public static String readRequestParameterMap(HttpServletRequest request){
		JSONObject json = new JSONObject();
		Enumeration<String> keys = request.getParameterNames();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = request.getParameter(key);
			json.put(key, value);
		}
		
		return json.toString();
	}
	
	/**
	 * 通过request的字符流获取请求参数
	 * 适用场景：text类型参数
	 * @param request
	 * @return
	 */
	public static final String readRequestStream(HttpServletRequest request) throws BusinessException{
		InputStream is = null;
		try {
			StringBuffer sb = new StringBuffer(3000);
			is = request.getInputStream();
			byte[] bytes = new byte[4096];
			int size = 0;
			while ((size = is.read(bytes)) > 0) {
				sb.append(new String(bytes, 0, size, "UTF-8"));
			}
			return sb.toString();
		} catch (IOException e) {
			LOGGER.error("请联系管理员！", e);
			throw new BusinessException("未知错误，请联系管理员！");
		} finally {
			if(is != null)
			{
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("请联系管理员！", e);
					throw new BusinessException("未知错误，请联系管理员！");
				}
			}
		}
	}
	
	public static String GetContentTypeOfHeaders(HttpServletRequest request) {
		Enumeration names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getHeader(name);
			LOGGER.debug(name + "=====00000===" + value);
			if ("content-type".equalsIgnoreCase(name)) {
				LOGGER.debug(name + "==11111===" + value);
				return value;
			}
		}
		return null;
	}
	
}  