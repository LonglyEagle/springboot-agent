package com.epro.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epro.domain.DeviceInfo;

@Component("deviceInfoService")
@Transactional
public class DeviceInfoServiceImpl implements DeviceInfoService {

	private final DeviceInfoRepository deviceInfoRepository;
	
	public DeviceInfoServiceImpl(DeviceInfoRepository deviceInfoRepository){
		this.deviceInfoRepository = deviceInfoRepository;
	}
	
	public boolean existDevice(String sn, String accesscode) {
		DeviceInfo device;
		
		if (accesscode == null){
			device = deviceInfoRepository.findBySn(sn);
		}else{
			device = deviceInfoRepository.findBySnAndAccessCode(sn, accesscode);
		}
			
		return device != null;
	}

	@Override
	public DeviceInfo addDeviceInfo(DeviceInfo deviceInfo) {
		return this.deviceInfoRepository.save(deviceInfo);
	}

}
