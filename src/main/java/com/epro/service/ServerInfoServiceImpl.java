package com.epro.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epro.domain.ServerInfo;
import com.epro.redis.cache.CacheQueryExecutor;
import com.epro.redis.cache.CacheTemplate;
import com.epro.redis.dao.RedisValuesDao;
import com.epro.utils.ServerInfoComparator;

@Component("serverInfoService")
@Transactional
@CacheConfig
public class ServerInfoServiceImpl implements ServerInfoService {

	@Autowired
	private RedisValuesDao redisValuesDao;
	
	private final ServerInfoRepository serverInfoRepository;
	
	public ServerInfoServiceImpl(ServerInfoRepository serverInfoRepository){
		this.serverInfoRepository = serverInfoRepository;
	}
	
	/**
	 * 获取空闲服务器
	 * 入参为缓存时间
	 */
	@Override
	public ServerInfo getIdleAgent(final long interval) {
		long millis = interval * 1000;
		return CacheTemplate.executeQuery(
				redisValuesDao, millis, ServerInfo.class,
				new Object[]{"ServerInfoServiceImpl","getIdleAgent",millis},
				new CacheQueryExecutor<ServerInfo>() {

					@Override
					public boolean backCacheEntity(ServerInfo entity) {
						return entity!=null;
					}

					@Override
					public ServerInfo execute() {
						List<ServerInfo> serverInfos = serverInfoRepository.findNewServerInfo(interval);
						if (serverInfos == null || serverInfos.isEmpty()) {
							return null;
						}
						
						Collections.sort(serverInfos,new ServerInfoComparator());
						return serverInfos.get(0);
					}

					@Override
					public boolean putCacheEntity(ServerInfo entity) {
						return entity!=null;
					}
					
				});
	}

	@Override
	public ServerInfo addServerInfo(ServerInfo serverInfo) {
		return serverInfoRepository.save(serverInfo);
	}

}
