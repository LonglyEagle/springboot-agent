package com.epro.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.epro.DataInitializer;
import com.epro.domain.ResponseResult;
/**
 * 用于新增接口映射时将数据库数据加载到内存
 * @author Grant.You
 *
 */
@Controller
@RequestMapping
public class DBDataLoadController extends BaseController{

	@Autowired
	private DataInitializer dataInitializer;
	
	@RequestMapping(value = "/route/db2cache")
	@ResponseBody
	public ResponseResult<String> loadData(HttpServletResponse response){
		dataInitializer.updateMap();
		dataInitializer.updateConfig();
		ResponseResult<String> result = new ResponseResult<String>();
		result.setResultCode("0");
		result.setMessage("成功");
		return result;
	}
	
}
