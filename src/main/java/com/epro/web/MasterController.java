package com.epro.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epro.constant.DefaultConfigEnum;
import com.epro.domain.MasterResponse;
import com.epro.domain.ResponseResult;
import com.epro.domain.ServerInfo;
import com.epro.service.ServerInfoService;

/**
 * 主代理，用于返回空闲服务器ip信息
 * @author Grant.You
 *
 */
@Controller
@RequestMapping
public class MasterController extends BaseController {
	
	@Autowired
	private ServerInfoService serverInfoService;
	
	@RequestMapping(value = "/route/getIdleAgent")
	@ResponseBody
	public ResponseResult<MasterResponse> getIdleAgent(HttpServletResponse response){
		ResponseResult<MasterResponse> result = new ResponseResult<MasterResponse>();
		
		long interval = DefaultConfigEnum.INTERVAL;
		ServerInfo idleServer = serverInfoService.getIdleAgent(interval);
		
		if (idleServer == null || idleServer.getIp() == null
				||"".equals(idleServer.getIp()))
		{
			result.setResultCode("1");
			result.setMessage("获取空闲服务器失败");
		}
		else{
			result.setResultCode("0");
			result.setMessage("成功");
			MasterResponse mr = new MasterResponse(idleServer.getIp(),idleServer.getPort());
			result.setData(mr);
		}
		return result;
	}
}
