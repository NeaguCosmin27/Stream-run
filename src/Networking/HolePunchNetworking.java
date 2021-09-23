package Networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import Handlers.HolePunchHandler;
import Listeners.HolePunchHandlerListener;
import Listeners.HolePunchNetworkingListener;
import Metadata.PeerConnectionRequest;
import Metadata.PeerIdentity;
import Metadata.RequestorMetadata;
import Metadata.SharedSessionConnectionData;
import Metadata.SharerMetadata;

public class HolePunchNetworking extends Thread{
	
	private Socket mainSocket;
	private ObjectInputStream serverOIS;
	private ObjectOutputStream serverOOS;
	private volatile boolean connected = true;
	private volatile boolean running = true;
	private volatile Socket peerSocket;
	private String peerSocketIP;
	private volatile int peerSocketPort;
	
	private SessionHandler sessionHandler;
	
	private HolePunchHandler holePunchHandler;
	
	private volatile Thread currentThread;
	
	private HolePunchNetworkingListener listener;
	
	public HolePunchNetworking() {
		
	}
	
	
	public void run() {
		
		synchronized(this) {
			this.currentThread = Thread.currentThread();
		}
		
		while(running) {
			
			try {
				Object object = serverOIS.readObject();
				
				//if this peer shares the session
				if(object instanceof RequestorMetadata) {
					System.out.println("Received requestor metadata from server!");
					
					System.out.println("Requestor Connection data");
					System.out.println(((RequestorMetadata)object).getIP());
					System.out.println(((RequestorMetadata)object).getPort());
					
					//If the peerSocket was previously disconnected and a new connection is attempted
				    if(peerSocket != null && peerSocket.isClosed()) {
				    	System.out.println("Socket reused");
					    this.peerSocket = new Socket();
					    SocketAddress randomAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress().toString(), peerSocketPort);
					    peerSocket.setReuseAddress(true);
					    peerSocket.bind(randomAddress);
				    }
					
					holePunchHandler = new HolePunchHandler(peerSocket, (RequestorMetadata)object);
					
					holePunchHandler.setHolePunchHandlerListener(new HolePunchHandlerListener() {

						@Override
						public void sessionConnectionStatus(String status) {
							// This is the right one	
						}

						@Override
						public void remoteConnectionStatus(String status, boolean isConnected) {
							// TODO Auto-generated method stub	
						}
						
					});
					holePunchHandler.start();
					
				//if this peer request the session
				}else if(object instanceof SharedSessionConnectionData) {
					
					System.out.println("Sharing Connection data");
					System.out.println(((SharedSessionConnectionData)object).getIP());
					System.out.println(((SharedSessionConnectionData)object).getPort());
					
					holePunchHandler = new HolePunchHandler(peerSocket, (SharedSessionConnectionData)object);
					
					holePunchHandler.setHolePunchHandlerListener(new HolePunchHandlerListener() {

						@Override
						public void sessionConnectionStatus(String status) {
							// TODO Auto-generated method stub	
						}

						@Override
						public void remoteConnectionStatus(String status, boolean isConnected) {
							// This is the right one
						}
						
					});
					holePunchHandler.start();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(e.getMessage().equals("socket closed")) {
					running = false;
					break;
				}
				running = false;
				break;
			}
			
		}
		
	}
	
	//If a second connection is attempted
	public void reconnect() {
		
	}
	
	// Connects to the server and request the sharing session of the peer
	public void connectRemoteSession(String externalIP, int externalPort, String internalIP, int internalPort, PeerConnectionRequest connectionRequest) {
		
		    PeerConnectionRequest connectionRequestLocal = connectionRequest;
			SocketAddress externalSocketAddress = new InetSocketAddress(externalIP, externalPort);
			SocketAddress internalSocketAddress = new InetSocketAddress(internalIP, internalPort);
			mainSocket = new Socket();
			
			//Try to connect to internal server address
			if(!mainSocket.isConnected() && !mainSocket.isClosed()) {
				try {
					mainSocket.connect(externalSocketAddress);
					//listener.getServerConnectionStatus("server connected");
					
					this.serverOOS = new ObjectOutputStream(mainSocket.getOutputStream());
					this.serverOIS = new ObjectInputStream(mainSocket.getInputStream());
					
					//Bind peer socket to random port to avoid port collision and sent it to server with the connection request
					if(peerSocket == null) {
						peerSocket = new Socket();
						
						//Sets up a random port between 2000 and 65000
						Random random = new Random();
						int randomPort = random.nextInt((65000 - 2000) + 1) + 2000;
						SocketAddress randomAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress().toString(), randomPort);
						peerSocket.setReuseAddress(true);
						peerSocket.bind(randomAddress);
						connectionRequestLocal.setSessionPort(randomPort);
					
					}
					
					if(mainSocket.isConnected()) {
						this.start();
						//listener.getServerConnectionStatus("server connected");
						serverOOS.writeObject(connectionRequestLocal);
						serverOOS.flush();
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					if(e.getMessage().equals("Connection refused: connect")) {
						listener.getServerConnectionStatus("server unreachable", false);
					}
					
					//System.out.println(e.getMessage());
				}
				
			}
			
			//Try to connect to external server address
			if(!mainSocket.isConnected() && !mainSocket.isClosed()) {
				try {
					mainSocket = new Socket();
					mainSocket.connect(internalSocketAddress);
					listener.getServerConnectionStatus("server connected", true);
					
					this.serverOOS = new ObjectOutputStream(mainSocket.getOutputStream());
					this.serverOIS = new ObjectInputStream(mainSocket.getInputStream());
					
					//Bind peer socket to random port to avoid port collision and sent it to server with the connection request
					if(peerSocket == null) {
						peerSocket = new Socket();
						
						//Sets up a random port between 2000 and 65000
						Random random = new Random();
						int randomPort = random.nextInt((65000 - 2000) + 1) + 2000;
						SocketAddress randomAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress().toString(), randomPort);
						peerSocket.setReuseAddress(true);
						peerSocket.bind(randomAddress);
						connectionRequestLocal.setSessionPort(randomPort);
						//this.peerSocketPort = randomPort;
					}
					
					if(mainSocket.isConnected()) {
						this.start();
						listener.getServerConnectionStatus("server connected", true);
						serverOOS.writeObject(connectionRequest);
						serverOOS.flush();
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					if(e.getMessage().equals("Connection refused: connect")) {
						listener.getServerConnectionStatus("server unreachable", false);
					}
					
					
				}
				
			}
	}
	
	//Stops the shared session
	public void stopSharingSession() {
		System.out.println("Session stopped");
		try {
			running = false;
			serverOIS.close();
			serverOOS.close();
			mainSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(peerSocket.isConnected()) {
			holePunchHandler.stopHolePunchHandler();
		}
		
		this.currentThread.interrupt();
		listener.getServerConnectionStatus("Session stopped", false);
	}
	
	//Disconnects from shared session
	public void disconnectFromSession() {
			System.out.println("Disconnected from session");
	}
	
	//Connects to server and send out the User ID and password
	public void startSharingSession(String externalIP, int externalPort, String internalIP, int internalPort, PeerIdentity identity) {
		
		PeerIdentity identityLocal = identity;
		SocketAddress externalSocketAddress = new InetSocketAddress(externalIP, externalPort);
		SocketAddress internalSocketAddress = new InetSocketAddress(internalIP, internalPort);
		mainSocket = new Socket();
		
		// Try and connect to external server address
		if(!mainSocket.isConnected() && !mainSocket.isClosed()) {
			try {
				
				mainSocket.connect(externalSocketAddress);
				
				this.serverOOS = new ObjectOutputStream(mainSocket.getOutputStream());
				this.serverOIS = new ObjectInputStream(mainSocket.getInputStream());
				
				//Bind peer socket to random port to avoid port collision and sent it to server with the session identity
				if(peerSocket == null) {
					peerSocket = new Socket();
					
					//Sets up a random port between 2000 and 65000
					Random random = new Random();
					int randomPort = random.nextInt((65000 - 2000) + 1) + 2000;
					SocketAddress randomAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress().toString(), randomPort);
					peerSocket.setReuseAddress(true);
					peerSocket.bind(randomAddress);
					identityLocal.setSessionPort(randomPort);
					this.peerSocketPort = randomPort;
				}
				
				if(mainSocket.isConnected()) {
					this.start();
					listener.getServerConnectionStatus("server connected", true);
					
					//Sends the peer identity to the server
					serverOOS.writeObject(identityLocal);
					serverOOS.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				if(e.getMessage().equals("Connection refused: connect")) {
					listener.getServerConnectionStatus("server unreachable", false);
				}
				
				//System.out.println(e.getMessage());
			}
			
		}
		
		// Try to connect to internal server address
		if(!mainSocket.isConnected() && !mainSocket.isClosed()) {
			try {
				mainSocket = new Socket();
				mainSocket.connect(internalSocketAddress);
				
				this.serverOOS = new ObjectOutputStream(mainSocket.getOutputStream());
				this.serverOIS = new ObjectInputStream(mainSocket.getInputStream());
				
				if(mainSocket.isConnected()) {
					this.start();
					listener.getServerConnectionStatus("server connected", true);
					
					//Sends the peer identity to the server
					serverOOS.writeObject(identity);
					serverOOS.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				if(e.getMessage().equals("Connection refused: connect")) {
					listener.getServerConnectionStatus("server unreachable", false);
				}
				
				
			}
				
		}
		//System.out.println(mainSocket.getRemoteSocketAddress().toString().substring(1, (mainSocket.getRemoteSocketAddress().toString().indexOf(':'))));
		//System.out.println(mainSocket.getPort());
		//System.out.println(mainSocket.getLocalSocketAddress());
		
	}
	
	public void setHolePunchNetworkingListener(HolePunchNetworkingListener listener) {
		this.listener = listener;
	}
	
	public void expandSelectedFolder(String remoteFolderPath) {
		holePunchHandler.expandSelectedFolder(remoteFolderPath);
	}


}
