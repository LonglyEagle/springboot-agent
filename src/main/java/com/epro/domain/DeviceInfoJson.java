package com.epro.domain;

/**
 * http请求参数Json对象
 * @author Grant.You
 *
 */
public class DeviceInfoJson {
	
	private DeviceInfo deviceInfo;
	
	private String encrypt;

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	
}
