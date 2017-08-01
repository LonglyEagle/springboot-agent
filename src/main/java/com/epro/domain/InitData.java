package com.epro.domain;

import java.util.Map;

import com.epro.redis.cache.SerializableEntity;

public class InitData extends SerializableEntity{

	private static final long serialVersionUID = 1L;
	
	private Map<String,String> ipMap;
	
	private Map<String,String> pathMap;
	
	public InitData() {}
	
	public InitData(Map<String, String> ipMap, Map<String, String> pathMap) {
		super();
		this.ipMap = ipMap;
		this.pathMap = pathMap;
	}

	public Map<String, String> getIpMap() {
		return ipMap;
	}

	public void setIpMap(Map<String, String> ipMap) {
		this.ipMap = ipMap;
	}

	public Map<String, String> getPathMap() {
		return pathMap;
	}

	public void setPathMap(Map<String, String> pathMap) {
		this.pathMap = pathMap;
	}
	
}
