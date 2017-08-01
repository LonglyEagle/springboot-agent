package com.epro.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.epro.redis.cache.SerializableEntity;

/**
 * ip和方法映射关系
 * @author Grant.You
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="agent_ip_map")
public class IpMap extends SerializableEntity{
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String methodType;
	
	@Column(nullable = false)
	private String ip;
	
	@Column(nullable = false)
	private String port;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

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
	
}
