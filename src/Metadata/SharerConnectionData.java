package Metadata;

import java.io.Serializable;

public class SharerConnectionData implements Serializable{
	
	
	private static final long serialVersionUID = 1357574353543645763L;
	public String IP;
	private int port;
	
	public SharerConnectionData(String IP, int port) {
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
