package com.epro.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.epro.constant.DefaultConfigEnum;
import com.epro.domain.ResponseResult;
import com.epro.redis.dao.RedisValuesDao;
import com.epro.utils.DataUtil;

/**
 * 设备认证请求拦截
 * @author Grant.You
 *
 */
@Configuration
public class AuthInterceptor extends WebMvcConfigurerAdapter {

	@Autowired
	private RedisValuesDao redisValuesDao;
	
	private final static String ERROR_RESP = DataUtil.ObjectToJson(
			new ResponseResult<String>("1","未通过设备认证",null));
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration ir = registry.addInterceptor(new HandlerInterceptor() {
			
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {
				//解决js跨域问题
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
				response.setContentType("application/json;charset=utf-8");
				
				if (DefaultConfigEnum.IGNOREAUTH) {
					return true;
				}
				
				Cookie[] cookies = request.getCookies();
				
				if (cookies == null || cookies.length == 0) {
					response.getWriter().write(ERROR_RESP);
					return false;
				}
				
				for (Cookie cookie : cookies) {
					if ("deviceCookie".equals(cookie.getName())) {
						String value = cookie.getValue();

						if (redisValuesDao.get(new String[]{"deviceCookie",value}, String.class) != null) {
							return true;
						}
					}
				}
				
				response.getWriter().write(ERROR_RESP);
				return false;
			}

			@Override
			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {
			}

			@Override
			public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
					Exception ex) throws Exception {
			}
			
		});
		ir .addPathPatterns("/**");
		ir.excludePathPatterns("/**/route/**");
	}
	
}
