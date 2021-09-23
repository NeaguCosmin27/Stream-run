package Metadata;

import java.io.Serializable;

public class PeerMetadata implements Serializable{
	
	private static final long serialVersionUID = 345678534553334453L;
	private String username;
	private String passwod;
	
	public PeerMetadata() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswod() {
		return passwod;
	}

	public void setPasswod(String passwod) {
		this.passwod = passwod;
	}
	
	

}
