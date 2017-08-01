package com.epro.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.epro.domain.ServerInfo;

/**
 * 服务器信息表操作类
 * @author Grant.You
 *
 */
public interface ServerInfoRepository extends Repository<ServerInfo, Long>{
	
	List<ServerInfo> findAll();
	
	/*@Query("from ServerInfo s where (s.ip,s.port,s.latestTime) in "
			+ "(select s1.ip,s1.port,max(s1.latestTime) from ServerInfo s1 "
			+ "group by s1.ip,s1.port))")
	List<ServerInfo> findNewServerInfo();*/
	
	@Query("from ServerInfo s where timestampdiff(second,s.latestTime,current_timestamp()) < ?1")
	List<ServerInfo> findNewServerInfo(long interval);
	
	ServerInfo save(ServerInfo serverInfo);
	
}