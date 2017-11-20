package com.dr.proxymocker.pojos;

public class Request {
	
	public String host;
	public int port;
	public String method;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return "Request [host=" + host + ", port=" + port + ", method=" + method + "]";
	}

	
	
}
