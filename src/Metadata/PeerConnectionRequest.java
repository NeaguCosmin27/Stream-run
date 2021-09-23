package Metadata;

import java.io.Serializable;

public class PeerConnectionRequest implements Serializable{
	
	
	private static final long serialVersionUID = 1492149122323424L;
	String sessionID;
	String sessionPassword;
	int sessionPort;
	
	public PeerConnectionRequest() {
		
	}
	
	

	public int getSessionPort() {
		return sessionPort;
	}



	public void setSessionPort(int sessionPort) {
		this.sessionPort = sessionPort;
	}



	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionPassword() {
		return sessionPassword;
	}

	public void setSessionPassword(String sessionPassword) {
		this.sessionPassword = sessionPassword;
	}
	
	

}
