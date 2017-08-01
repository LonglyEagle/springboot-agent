package com.epro.service;

import com.epro.domain.DeviceInfo;

public interface DeviceInfoService {
	
	boolean existDevice(String sn,String accesscode);
	
	DeviceInfo addDeviceInfo(DeviceInfo deviceInfo);
}
