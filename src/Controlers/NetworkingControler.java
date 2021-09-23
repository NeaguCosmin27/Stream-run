package Controlers;

import java.util.ArrayList;
import java.util.LinkedList;

import DataModels.Connection;
import DataModels.ConnectionsDB;
import DataModels.DismantledTreeDataModel;
import DataModels.Source;
import DataModels.SourceTreeDataModel;
import DataModels.SourcesDB;
import Events.NetworkControlerEvent;
import Listeners.NetworkingControlerListener;
import Listeners.RemoteNetworkingListener;
import Networking.RemoteNetworking;
import Networking.SourceNetworking;

public class NetworkingControler{
	
	private SourceNetworking sourceNetworking;
	private SourcesDB sourcesDB;
	private ConnectionsDB connectionsDB;
	private SourceTreeDataModel sourceTreeDataModel;
	private RemoteNetworking remoteNetworking;
	private DismantledTreeDataModel dismantledTreeDataModel;
	
	private NetworkingControlerListener controlerListener;

	public NetworkingControler() {
		sourcesDB = new SourcesDB();
		connectionsDB = new ConnectionsDB();
	}
	
	//Starts the source
	public void startSource(String IP, int port, String name, String selectedFilePath, String hostname) {
		
		sourceTreeDataModel = new SourceTreeDataModel(selectedFilePath);
		dismantledTreeDataModel = new DismantledTreeDataModel(selectedFilePath);
		dismantledTreeDataModel.start();
		sourceTreeDataModel.start();
		
		try {
			sourceTreeDataModel.join();
			dismantledTreeDataModel.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sourceNetworking = new SourceNetworking(sourceTreeDataModel, dismantledTreeDataModel);
		sourceNetworking.startSource(IP, port);
		sourceNetworking.start();
		
		sourcesDB.addToList(new Source(name, IP, port, hostname, selectedFilePath, sourceNetworking));		
		
		NetworkControlerEvent event = new NetworkControlerEvent(this);
		controlerListener.actionHappened(event);
		
	}
	
	//Finds the source from the list and stops it
	public void stopSourceFinal(int index) {
		
		Source sourceFinal = null;
		for(Source source:sourcesDB.getList()) {
			if(source.equals(sourcesDB.getByIndex(index))) {
				source.stopSource();
				sourceFinal = source;	
			}
		}
		sourcesDB.removeFromList(sourceFinal);
		
		NetworkControlerEvent event = new NetworkControlerEvent(this);
		controlerListener.actionHappened(event);	
	}
	
	//-------------------------------------Connection area--------------------------------
	//Connect to source
	public void connect(String IP, String hostname, int port){
		remoteNetworking = new RemoteNetworking();
		remoteNetworking.connect(IP, port);
		remoteNetworking.start();
		remoteNetworking.setRemoteNetworkingListener(new RemoteNetworkingListener() {

			@Override
			public void getFileSize(long filesize) {
				controlerListener.getReceivedFileSize(filesize);
			}

			@Override
			public void getTransferPercentage(int percentage) {
				controlerListener.getTransferPercentage(percentage);	
			}

			@Override
			public void getFileName(String filename) {
				controlerListener.getFileName(filename);
			}
			
		});
	    
		connectionsDB.addConnection(new Connection(remoteNetworking, hostname, IP, port));
		
		NetworkControlerEvent event = new NetworkControlerEvent(this);
		controlerListener.actionHappened(event);
	}
	
	//Disconnects from source
	public void disconnect() {
		remoteNetworking.disconnect();
	}
	
	public void disconnectFinal(int index) {
		
		Connection connectionFinal = null;
		for(Connection connection:connectionsDB.getConnectionsList()) {
			if(connection.equals(connectionsDB.getByIndex(index))) {
				connection.disconnect();
				connectionFinal = connection;
			}
		}
		connectionsDB.removeConnection(connectionFinal);
		
		NetworkControlerEvent event = new NetworkControlerEvent(this);
		controlerListener.actionHappened(event);
	}
	
	//Gets the sources list object
	public LinkedList<Source> getSources(){
		return sourcesDB.getList();
	}
	
	//Gets the connections list object
	public LinkedList<Connection> getConnections(){
		return connectionsDB.getConnectionsList();
	}
	
	public void setNetworkingControlerListener(NetworkingControlerListener controlerListener) {
		this.controlerListener = controlerListener;
	}
	
	//Retrieve file selected by the client
	public void retrieveFile(int index, String originalFilePath, String localFilePath, String localFileName) {
	     
		((connectionsDB.getByIndex(index)).getRemoteNetworking()).retrieveFile(originalFilePath, localFilePath, localFileName);
		System.out.println("Trasnfer startet at: " + index);
	}
}
