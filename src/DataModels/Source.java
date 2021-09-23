package DataModels;

import javax.swing.tree.DefaultTreeModel;

import Networking.SourceNetworking;

public class Source {
	
	private String name;
	private SourceNetworking sourceNetworking;
	private String IP;
	private int port;
	private String hostname;
	private String selectedFilePath;
	
	public Source(String name, String IP, int port, String hostname, String selectedFilePath, SourceNetworking sourceNetworking) {
		this.name = name;
		this.sourceNetworking = sourceNetworking;
		this.IP = IP;
		this.port = port;
		this.hostname = hostname;
		this.selectedFilePath = selectedFilePath;
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



	public String getHostname() {
		return hostname;
	}



	public void setHostname(String hostname) {
		this.hostname = hostname;
	}



	public String getSelectedFilePath() {
		return selectedFilePath;
	}



	public void setSelectedFilePath(String selectedFilePath) {
		this.selectedFilePath = selectedFilePath;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SourceNetworking getSourceNetworking() {
		return sourceNetworking;
	}

	public void setSourceNetworking(SourceNetworking sourceNetworking) {
		this.sourceNetworking = sourceNetworking;
	}
	
	public void stopSource() {
		sourceNetworking.stopSource();
	}
	
	public DefaultTreeModel getTreeDataModel() {
		return sourceNetworking.getTreeDataModel();
	}

}
