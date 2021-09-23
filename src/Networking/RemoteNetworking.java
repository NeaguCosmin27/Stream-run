package Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import DataModels.ClientTreeDataModel;
import DataModels.DismantledTreeDataModel;
import DataModels.SourceTreeDataModel;
import DataModels.TreeElementsList;
import Events.ClientFileReceiverEvent;
import Handlers.ClientFileReceiver;
import Listeners.ClientFileReceiverListener;
import Listeners.RemoteNetworkingListener;

public class RemoteNetworking extends Thread{
	
	private Socket remoteSocket;
	private volatile boolean isRunning = true;
	private Thread currentThread;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private volatile DefaultTreeModel treeModel = null;
	private volatile TreeElementsList clientTreeElementsList;
	
	private ClientFileReceiver clientFileReceiver;
	private RemoteNetworkingListener listener;
	private ClientTreeDataModel clientTreeDataModel;
	private DismantledTreeDataModel clientDismantledTree;
	
	public RemoteNetworking() {
	     
	}
	
	
	public void connect(String IP, int port) {
		try {
			SocketAddress socketAddress = new InetSocketAddress(IP, port);
			remoteSocket = new Socket();
			remoteSocket.connect(socketAddress);
			
			try {
				
			    oos = new ObjectOutputStream(remoteSocket.getOutputStream());
			    oos.writeObject("DataTree");
				oos.flush();
				
			    ois = new ObjectInputStream(remoteSocket.getInputStream());
			    
			    Object object = (Object)ois.readObject();
				
				if(object instanceof DismantledTreeDataModel) {
					//this received object is not null
				    //treeModel = ((SourceTreeDataModel)object).getTreeModel();
					
					clientDismantledTree = (DismantledTreeDataModel)object;
					clientTreeElementsList = clientDismantledTree.getTreeElementsList();
					
					clientTreeDataModel = new ClientTreeDataModel(clientTreeElementsList);
					clientTreeDataModel.start();
					try {
						clientTreeDataModel.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					treeModel = clientTreeDataModel.getAssembledTreeModel();
	
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void disconnect() {
		try {
			
			
			oos.flush();
			oos.close();
			isRunning = false;
			remoteSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DefaultTreeModel getClientTreeModel() {
		return treeModel;
	}
	
	//Retrieve the selected file
	public void retrieveFile(String originalFilePath, String localFilePath, String localFileName) {
		
		clientFileReceiver = new ClientFileReceiver(originalFilePath, localFilePath, localFileName, oos, ois);
		clientFileReceiver.setClientFileReceiverListener(new ClientFileReceiverListener() {

			@Override
			public void filePartReceived(ClientFileReceiverEvent event) {
			   System.out.println("Retrieving: " + (int)event.getPercentage() + "%");
			   listener.getFileSize(event.getFileSize());
			   listener.getFileName(event.getFileName());
			   listener.getTransferPercentage((int)event.getPercentage());  
			}
			
		});
		clientFileReceiver.start();		
	}
	
	public void setRemoteNetworkingListener(RemoteNetworkingListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		
		synchronized(this) {
			this.currentThread = Thread.currentThread();
		}
		
		
		
		
	}

}
