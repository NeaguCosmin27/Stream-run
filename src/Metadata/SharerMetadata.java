package Metadata;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SharerMetadata implements Serializable{
	

	private static final long serialVersionUID = 1245664354646463535L;
	private ObjectOutputStream oos;
	private PeerIdentity identity;
	private String peerIP;
	private int peerPort;
	
	public SharerMetadata() {
		
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public PeerIdentity getIdentity() {
		return identity;
	}

	public void setIdentity(PeerIdentity identity) {
		this.identity = identity;
	}

	public String getPeerIP() {
		return peerIP;
	}

	public void setPeerIP(String peerIP) {
		this.peerIP = peerIP;
	}

	public int getPeerPort() {
		return peerPort;
	}

	public void setPeerPort(int peerPort) {
		this.peerPort = peerPort;
	}
	
	

}