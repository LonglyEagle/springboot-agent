package com.epro;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epro.constant.DefaultConfigEnum;
import com.epro.domain.Configuration;
import com.epro.domain.InitData;
import com.epro.redis.dao.RedisValuesDao;
import com.epro.schedule.DynamicScheduledTask;
import com.epro.service.ConfigurationService;
import com.epro.service.IpMapService;
import com.epro.service.PathMapService;
import com.epro.utils.ServerInfoUtil;

/**
 * 物理库同步到缓存的操作类
 * @author Grant.You
 *
 */
@Component
public class DataInitializer {

	@Autowired
	private DynamicScheduledTask dynamicScheduledTask;

	@Autowired
	private IpMapService ipMapService;

	@Autowired
	private PathMapService pathMapService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private RedisValuesDao redisValuesDao;

	@PostConstruct
	public void init() {
		updateMap();
		updateConfig();
	}

	/**
	 * 从物理库更新接口url映射关系到缓存
	 */
	public void updateMap() {
		Map<String,String> ipMap = ipMapService.getIpMap();
		Map<String,String> methodMap = pathMapService.getPathMap();
		InitData initData = new InitData(ipMap, methodMap);
		redisValuesDao.set(new String[]{"initData"}, InitData.class, initData);
	}

	/**
	 * 更新定时任务的周期
	 * @param serverType
	 * @param interval
	 */
	public void updateScheduleTask(String serverType,long interval,String serverPort) {
		dynamicScheduledTask.setServerType(serverType);
		dynamicScheduledTask.setInterval(interval);
		dynamicScheduledTask.setServerPort(serverPort);
	}

	/**
	 * 更新配置
	 * 如果数据库中存在该代理的配置则加载
	 * 否则向数据库中插入默认配置
	 */
	public void updateConfig() {
		DefaultConfigEnum.LOCAL_IP = ServerInfoUtil.getDefaultIpAddress();
		//查询配置表并修改程序中的配置
		Configuration config = configurationService.findByIP(DefaultConfigEnum.LOCAL_IP);
		if (config == null) {
			config = new Configuration();
			config.setIp(DefaultConfigEnum.LOCAL_IP);
			config.setPort(DefaultConfigEnum.DEFAULT_PORT);
			config.setServerType(DefaultConfigEnum.DEFAULT_SERVER_TYPE);
			config.setScheduleInterval(DefaultConfigEnum.INTERVAL);
			config.setWeight(DefaultConfigEnum.WEIGHT);
			config.setIgnoreAuth(DefaultConfigEnum.IGNOREAUTH);
			configurationService.addConfig(config);
			
			updateScheduleTask(DefaultConfigEnum.DEFAULT_SERVER_TYPE,
					DefaultConfigEnum.INTERVAL,
					DefaultConfigEnum.DEFAULT_PORT);
		} else {
			updateScheduleTask(
					config.getServerType(), 
					config.getScheduleInterval(),
					config.getPort());
			DefaultConfigEnum.WEIGHT = config.getWeight();
			DefaultConfigEnum.IGNOREAUTH = config.isIgnoreAuth();
			DefaultConfigEnum.INTERVAL = config.getScheduleInterval();
		}
		
	}

}