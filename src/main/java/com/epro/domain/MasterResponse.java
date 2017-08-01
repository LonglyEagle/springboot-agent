package com.epro.domain;

/**
 * masterController请求返回对象
 * @author Grant.You
 *
 */
public class MasterResponse {
	private String ipaddr;
	
	private String port;
	
	private String encrypt;
	
	public MasterResponse(){}

	public MasterResponse(String ipaddr, String port) {
		super();
		this.ipaddr = ipaddr;
		this.port = port;
	}
	
	public MasterResponse(String ipaddr, String port, String encrypt) {
		super();
		this.ipaddr = ipaddr;
		this.port = port;
		this.encrypt = encrypt;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
	
}
