package com.epro.service;

import com.epro.domain.ServerInfo;

public interface ServerInfoService {
	
	ServerInfo getIdleAgent(long interval);
	
	ServerInfo addServerInfo(ServerInfo serverInfo);
	
}
