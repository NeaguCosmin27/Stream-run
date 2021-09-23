package Metadata;

import java.io.Serializable;

public class RequestorMetadata implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 192342423413124412L;
	private String IP;
	private int port;
	
	public void RequestorMetadata() {
		
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	

}
