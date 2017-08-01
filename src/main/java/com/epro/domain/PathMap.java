package com.epro.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.epro.redis.cache.SerializableEntity;

/**
 * 路径映射关系
 * @author Grant.You
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="agent_path_map")
public class PathMap extends SerializableEntity{
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String appPath;
	
	@Column(nullable = false)
	private String servicePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	
}
