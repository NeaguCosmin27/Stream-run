package Controlers;

import Listeners.HolePunchNetworkingListener;
import Listeners.RemoteFileTransferControlerListener;
import Metadata.PeerConnectionRequest;
import Metadata.PeerIdentity;
import Networking.HolePunchNetworking;

public class RemoteFileTransferControler {
	
	private HolePunchNetworking holePunchNetworking;
	
	private RemoteFileTransferControlerListener listener;
	
	public RemoteFileTransferControler() {
		
	}
	
	public void setRemoteFileTransferControlerListener(RemoteFileTransferControlerListener listener) {
		this.listener = listener;
	}
	
	public void connectToServer() {
		
	}
	
	//Connects to server
	public void startSharingSession(String externalIP, int externalPort, String internalIP, int internalPort, PeerIdentity identity) {
		holePunchNetworking = new HolePunchNetworking();
		holePunchNetworking.setHolePunchNetworkingListener(new HolePunchNetworkingListener() {

			//sets up the conenction status listener
			@Override
			public void getServerConnectionStatus(String status, boolean isSetup) {
				listener.getServerConnectionStatus(status, isSetup);
			}
			
		});
		
		holePunchNetworking.startSharingSession(externalIP, externalPort, internalIP, internalPort, identity);
	}
	
	//Disconnects from server
	public void stopSharingSession() {
		holePunchNetworking.stopSharingSession();
	}
	
	
	public void disconnectFromSession() {
		holePunchNetworking.disconnectFromSession();
	}
	
	//Connects to remote session
	public void connectRemoteSession(String externalIP, int externalPort, String internalIP, int internalPort, PeerConnectionRequest connectionRequest) {
		holePunchNetworking = new HolePunchNetworking();
		holePunchNetworking.setHolePunchNetworkingListener(new HolePunchNetworkingListener() {

			//sets up the conenction status listener
			@Override
			public void getServerConnectionStatus(String status, boolean isSetup) {
				listener.getServerConnectionStatus(status, isSetup);
			}
			
		});
		
		holePunchNetworking.connectRemoteSession(externalIP, externalPort, internalIP, internalPort, connectionRequest);
	}
	
	public void expandSelectedFolder(String remoteFolderPath) {
		holePunchNetworking.expandSelectedFolder(remoteFolderPath);
	}
	

}
