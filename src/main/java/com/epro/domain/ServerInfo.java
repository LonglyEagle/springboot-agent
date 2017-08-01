package com.epro.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.epro.redis.cache.SerializableEntity;

/**
 * Slave服务器信息表
 * @author Grant.You
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="agent_server_info")
public class ServerInfo extends SerializableEntity{
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false)
	private String slaveAgentNo;
	
	@Column(nullable = false)
	private String ip;
	
	@Column(nullable = false)
	private String port;
	
	@Column(nullable = false)
	private double cpu;
	
	private double memory;
	
	private double io;
	
	private int onlineterminals;
	
	@Column(nullable = false)
	private Timestamp latestTime;
	
	public String getSlaveAgentNo() {
		return slaveAgentNo;
	}

	public void setSlaveAgentNo(String slaveAgentNo) {
		this.slaveAgentNo = slaveAgentNo;
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

	public double getCpu() {
		return cpu;
	}
	
	public void setCpu(double cpu) {
		this.cpu = cpu;
	}
	
	public double getMemory() {
		return memory;
	}
	
	public void setMemory(double memory) {
		this.memory = memory;
	}
	
	public double getIo() {
		return io;
	}

	public void setIo(double io) {
		this.io = io;
	}

	public int getOnlineterminals() {
		return onlineterminals;
	}
	
	public void setOnlineterminals(int onlineterminals) {
		this.onlineterminals = onlineterminals;
	}

	public Timestamp getLatestTime() {
		return latestTime;
	}

	public void setLatestTime(Timestamp latestTime) {
		this.latestTime = latestTime;
	}

}
