package DataModels;

import javax.swing.tree.DefaultTreeModel;

import Networking.RemoteNetworking;

public class Connection {
	
	private RemoteNetworking remoteNetworking;
	private String hostname;
	private String IP;
	private int port;
	
	public Connection(RemoteNetworking remoteNetworking, String hostname, String IP, int port) {
		this.remoteNetworking = remoteNetworking;
		this.IP = IP;
		this.port = port;
		this.hostname = hostname;
	}

	public RemoteNetworking getRemoteNetworking() {
		return remoteNetworking;
	}

	public void setRemoteNetworking(RemoteNetworking remoteNetworking) {
		this.remoteNetworking = remoteNetworking;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
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
	
	public void disconnect() {
		remoteNetworking.disconnect();
	}
	
	public DefaultTreeModel getConnectiontreeModel() {
		return remoteNetworking.getClientTreeModel();
	}
	
}
