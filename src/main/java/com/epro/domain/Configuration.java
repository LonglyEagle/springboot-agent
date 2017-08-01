package com.epro.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="agent_configuration")
public class Configuration implements Serializable{
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String ip;
	
	@Column(nullable = false)
	private String port;
	
	@Column(nullable = false)
	private String serverType;
	
	@Column(nullable = false)
	private long scheduleInterval;
	
	private String weight;
	
	private boolean ignoreAuth;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public long getScheduleInterval() {
		return scheduleInterval;
	}

	public void setScheduleInterval(long scheduleInterval) {
		this.scheduleInterval = scheduleInterval;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public boolean isIgnoreAuth() {
		return ignoreAuth;
	}

	public void setIgnoreAuth(boolean ignoreAuth) {
		this.ignoreAuth = ignoreAuth;
	}
	
}
