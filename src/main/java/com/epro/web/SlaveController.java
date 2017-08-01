package com.epro.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epro.domain.InitData;
import com.epro.exception.BusinessException;
import com.epro.redis.dao.RedisValuesDao;
import com.epro.utils.HttpClientUtil;

@Controller
@RequestMapping
public class SlaveController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SlaveController.class);

	@Autowired
	private RedisValuesDao redisValuesDao;

	@RequestMapping(value="/{mtype}/{mpath}")
	@ResponseBody
	public String dataDispatch(@PathVariable String mtype,
			@PathVariable String mpath,HttpServletRequest req,
			HttpServletResponse resp) throws BusinessException{
		String result = dispatch(mtype,mpath,req);
		return result;
	}

	/**
	 * 转发请求
	 * @param ip
	 * @param mtype
	 * @param mpath
	 * @param req
	 * @return
	 */
	public String dispatch(String mtype,String mpath,HttpServletRequest req) throws BusinessException{
		LOGGER.debug("dispatch request: mtype:{},mpath:{}",mtype,mpath);

		String url = rewriteURL(mtype, mpath, req);
		LOGGER.debug("targetUrl:{}", url);
		
		if (url == null || "".equals(url)) {
			throw new BusinessException("请求路径无效，请确认请求路径或检查路径映射关系表");
		}
		
		String resp = HttpClientUtil.send(req,url);

		return resp;
	}

	/**
	 * 重写url
	 * @param ip
	 * @param mtype
	 * @param mpath
	 * @param req
	 * @return
	 */
	private String rewriteURL(String mtype,String mpath,HttpServletRequest req){
		InitData initData = redisValuesDao.get(new String[]{"initData"}, InitData.class);
		Map<String,String> ipMap = initData.getIpMap();
		Map<String,String> pathMap = initData.getPathMap();
		
		if (ipMap.size() == 0 || pathMap.size() == 0) {
			LOGGER.error("ipMapSize:{},pathMapSize:{}",ipMap.size(),pathMap.size());
			return null;
		}
		
		String ip = ipMap.get(mtype);
		String key = mtype+"/"+mpath;

		if (!pathMap.containsKey(key)){
			return null;
		}

		String realpath = pathMap.get(key).toString();
		String url = "http://"+ip+"/"+realpath;
		
		String queryString = req.getQueryString();
		
		if (queryString != null && !"".equals(queryString)){
			if (url.contains("?")){
				url = url + "&" + queryString;
			} else{
				url = url + "?" + queryString;
			}
		}
		
		return url;
	}
}
