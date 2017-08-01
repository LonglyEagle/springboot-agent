package com.epro.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epro.constant.TimeEnum;
import com.epro.domain.DeviceInfo;
import com.epro.domain.DeviceInfoJson;
import com.epro.domain.ResponseResult;
import com.epro.exception.BusinessException;
import com.epro.redis.dao.RedisValuesDao;
import com.epro.service.DeviceInfoService;
import com.epro.utils.HttpClientUtil;
import com.epro.utils.UUIDGenerator;

import net.sf.json.JSONObject;

/**
 * 设备认证
 * @author Grant.You
 *
 */
@Controller
@RequestMapping
public class DeviceAuthController extends BaseController{
	
	private static final Logger logger = LoggerFactory
			.getLogger(DeviceAuthController.class);
	
	@Autowired
	private RedisValuesDao redisValuesDao;
	
	@Autowired
	private DeviceInfoService deviceInfoService;

	@RequestMapping("/route/deviceAuth")
	@ResponseBody
	public ResponseResult<Object> auth(HttpServletRequest req, 
			HttpServletResponse response) throws BusinessException{
		ResponseResult<Object> result = new ResponseResult<Object>();
		result.setResultCode("1");
		result.setMessage("设备认证失败");

		//获取post方式传过来的参数
		String reqJson = HttpClientUtil.getRequestParam(req);
		logger.debug("request param:{}", reqJson);

		DeviceInfoJson deviceJson = jsonToDeviceInfo(reqJson);
		String sn = deviceJson.getDeviceInfo().getSn();
		String accessCode = deviceJson.getDeviceInfo().getAccesscode();

		if(deviceInfoService.existDevice(sn,accessCode))
		{
			//String sessionId = req.getSession().getId();
			//将认证成功的信息保存到cookie
			String cookieValue = UUIDGenerator.getUUID();
			redisValuesDao.set(new String[]{"deviceCookie",cookieValue}, String.class, cookieValue, TimeEnum.MILLIS_ONE_WEEK);
			Cookie cookie = new Cookie("deviceCookie", cookieValue);
			cookie.setMaxAge(TimeEnum.SECOND_ONE_WEEK);//保存一周
			cookie.setPath("/");
			response.addCookie(cookie);
			result.setResultCode("0");
			result.setMessage("成功");
		}
		
		return result;
	}

	@RequestMapping("/route/send_deviceinfo")
	@ResponseBody
	public ResponseResult<Object> addDeviceInfo(HttpServletRequest req, 
			HttpServletResponse response) throws BusinessException {
		ResponseResult<Object> result = new ResponseResult<Object>();
		//获取post方式传过来的参数
		String reqJson =  HttpClientUtil.getRequestParam(req);
		logger.debug("request param:{}", reqJson);

		DeviceInfoJson deviceJson = jsonToDeviceInfo(reqJson);
		DeviceInfo device = deviceInfoService.addDeviceInfo(deviceJson.getDeviceInfo());

		result.setResultCode("0");
		result.setMessage("成功");

		if (device == null){
			result.setResultCode("1");
			result.setMessage("添加设备信息失败");
		}

		return result;
	}

	/**
	 * 将json文本转换为DeviceInfo对象
	 * @param json
	 * @return
	 */
	private DeviceInfoJson jsonToDeviceInfo(String json){
		DeviceInfoJson deviceJson = new DeviceInfoJson();
		DeviceInfo deviceInfo = new DeviceInfo();
		JSONObject jsonObj = JSONObject.fromObject(json);

		if(jsonObj.containsKey("SN")){
			deviceInfo.setSn(jsonObj.getString("SN"));
		}

		if(jsonObj.containsKey("AccessCode")){
			deviceInfo.setAccesscode(jsonObj.getString("AccessCode"));
		}

		if(jsonObj.containsKey("MacAddr")){
			deviceInfo.setMacaddr(jsonObj.getString("MacAddr"));
		}

		if(jsonObj.containsKey("IPAddr")){
			deviceInfo.setIpaddr(jsonObj.getString("IPAddr"));
		}

		deviceJson.setDeviceInfo(deviceInfo);

		if(jsonObj.containsKey("encrypt")){
			deviceJson.setEncrypt(jsonObj.getString("encrypt"));
		}

		logger.debug("response:", deviceJson);
		
		return deviceJson;
	}
}
