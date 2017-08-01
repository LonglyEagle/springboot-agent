package com.epro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epro.domain.PathMap;

@Component("pathMapService")
@Transactional
public class PathMapServiceImpl implements PathMapService {
	
	private PathMapRepository pathMapRepository;
	
	public PathMapServiceImpl(PathMapRepository pathMapRepository) {
		this.pathMapRepository = pathMapRepository;
	}
	
	/**
	 * 获取路径映射
	 */
	@Override
	public Map<String, String> getPathMap() {
		Map<String,String> result = new HashMap<String,String>();
		List<PathMap> pathList = pathMapRepository.findAll();
		for (PathMap path : pathList){
			result.put(path.getAppPath(), path.getServicePath());
		}
		return result;
	}

}
