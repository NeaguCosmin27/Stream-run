package Metadata;

import java.io.Serializable;

public class SharedSessionConnectionData implements Serializable{
	
	
	private static final long serialVersionUID = 1357574353543645763L;
	private String IP;
	private int port;
	
	public SharedSessionConnectionData(String IP, int port) {
		this.IP = IP;
		this.port = port;
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
