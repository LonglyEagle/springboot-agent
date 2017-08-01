package com.epro.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备信息表
 * @author Grant.You
 *
 */
@Entity
@Table(name="agent_device_info")
public class DeviceInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String sn;
	
	private String accesscode;
	
	private String macaddr;
	
	private String ipaddr;
	
	public DeviceInfo(){}

	public DeviceInfo(String sn, String accesscode, String macaddr, String ipaddr) {
		super();
		this.sn = sn;
		this.accesscode = accesscode;
		this.macaddr = macaddr;
		this.ipaddr = ipaddr;
	}



	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getAccesscode() {
		return accesscode;
	}

	public void setAccesscode(String accesscode) {
		this.accesscode = accesscode;
	}

	public String getMacaddr() {
		return macaddr;
	}

	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
}
