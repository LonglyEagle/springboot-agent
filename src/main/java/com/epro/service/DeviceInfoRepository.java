package com.epro.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.epro.domain.DeviceInfo;

/**
 * 设备信息表操作类
 * @author Grant.You
 *
 */
public interface DeviceInfoRepository extends Repository<DeviceInfo, Long>{

	List<DeviceInfo> findAll();
	
	@Query("from DeviceInfo d where d.sn = ?1")
	DeviceInfo findBySn(String sn);
	
	@Query("from DeviceInfo d where d.sn = ?1 and d.accesscode = ?2")
	DeviceInfo findBySnAndAccessCode(String sn,String accesscode);
	
	DeviceInfo save(DeviceInfo deviceInfo);
}
