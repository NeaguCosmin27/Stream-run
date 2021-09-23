package Handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import DataModels.RemotePeerExplorerUpdate;
import Listeners.HolePunchHandlerListener;
import Metadata.RequestorMetadata;
import Metadata.SharedSessionConnectionData;

public class HolePunchHandler extends Thread{
	
	private volatile Socket peerSocket;
	private RequestorMetadata requestorMetadata;
	private SharedSessionConnectionData sharedConnectionData;
	
	private volatile ObjectOutputStream OOS = null;
	private volatile ObjectInputStream OIS = null;
	
	private volatile boolean isRunning = true;
	
	private volatile Thread currentThread;
	
	private HolePunchHandlerListener listener;
	
	public HolePunchHandler(Socket peerSocket, SharedSessionConnectionData sharedConnectionData) {
		this.peerSocket = peerSocket;
		this.sharedConnectionData = sharedConnectionData;
	}
	
	public HolePunchHandler(Socket peerSocket, RequestorMetadata requestorMetadata) {
		this.peerSocket = peerSocket;
		this.requestorMetadata = requestorMetadata;
	}
	
	//Updates the remote file explorer
	public void expandSelectedFolder(String remoteFolderPath) {
		RemotePeerExplorerUpdate crossPeerFilePath = new RemotePeerExplorerUpdate();
        crossPeerFilePath.setRemoteFolderPath(remoteFolderPath);
		
		try {
			OOS.writeObject(crossPeerFilePath);
			OOS.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This is where the main action happens
	public void run() {
		
		synchronized(this) {
			this.currentThread = Thread.currentThread();
		}
		
		//Connect to peer if this  peer is sharing the session
		if(peerSocket!=null && !peerSocket.isConnected() && sharedConnectionData!=null) {
			
			SocketAddress peerAddress = new InetSocketAddress(sharedConnectionData.getIP(), sharedConnectionData.getPort());
			try {
				peerSocket.connect(peerAddress);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			if(peerSocket.isConnected()) {
				System.out.println("Shared session connected");
				listener.sessionConnectionStatus("Peer connection successfull!");
			}else if(!peerSocket.isConnected()) {
				listener.sessionConnectionStatus("Peer connection failed!");
			}
				
	   }
	   
	   //Connect to peer if this peer is the requestor (It should also send the file explorer data)
       if(peerSocket!=null && !peerSocket.isConnected() && requestorMetadata!=null) {
			
			SocketAddress peerAddress = new InetSocketAddress(requestorMetadata.getIP(), requestorMetadata.getPort());
			try {
				peerSocket.connect(peerAddress);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(peerSocket.isConnected()) {
				System.out.println("Requestor connected");
				listener.remoteConnectionStatus("Peer connection successfull!", true);
			}else if(!peerSocket.isConnected()){
				listener.remoteConnectionStatus("PeerConnection failed!", false);
			}
	    }
       
       //Create the communication chanels only if the socket is connected and the communication channels are null
       
       if(peerSocket.isConnected() && OOS == null && OIS == null) {
    	   try {
			this.OOS = new ObjectOutputStream(peerSocket.getOutputStream());
			this.OIS = new ObjectInputStream(peerSocket.getInputStream());
		   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
       }
       
       //Processes packets received from peer
       if(OOS!= null && OIS!= null) {
    	   while(isRunning) {
    		   try {
				Object object = (Object)OIS.readObject();
				
				if(object instanceof RemotePeerExplorerUpdate) {
					
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				//Closes this socket and his resources if peer disconnects
				if(e.getLocalizedMessage().equals("Connection reset")) {
					this.isRunning = false;
					try {
						OIS.close();
						OOS.close();
						peerSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
    	   }
       }

    }
	
	public synchronized void stopHolePunchHandler() {
		this.isRunning = false;
		try {
			OIS.close();
			OOS.flush();
			OOS.close();
			peerSocket.close();
			this.currentThread.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setHolePunchHandlerListener(HolePunchHandlerListener listener) {
		this.listener = listener;
	}
	
	
}
