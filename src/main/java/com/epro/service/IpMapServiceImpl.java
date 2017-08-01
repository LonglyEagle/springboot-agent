package com.epro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epro.domain.IpMap;

@Component("ipMapService")
@Transactional
public class IpMapServiceImpl implements IpMapService {

	private final IpMapRepository ipMapRepository;
	
	public IpMapServiceImpl(IpMapRepository ipMapRepository){
		this.ipMapRepository = ipMapRepository;
	}
	
	/**
	 * 获取ip映射表数据
	 */
	@Override
	public Map<String, String> getIpMap() {
		Map<String,String> result = new HashMap<String,String>();
		List<IpMap> ipList = ipMapRepository.findAll();
		for (IpMap ip : ipList){
			result.put(ip.getMethodType(), ip.getIp()+":"+ip.getPort());
		}
		return result;
	}

}
