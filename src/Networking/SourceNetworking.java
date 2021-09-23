package Networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.tree.DefaultTreeModel;

import DataModels.DismantledTreeDataModel;
import DataModels.SourceTreeDataModel;
import Handlers.SourceHandler;

public class SourceNetworking extends Thread {
	
	private ServerSocket serverSocket;
	private volatile boolean isRunning = true;
	private Thread currentThread;
	private ArrayList<SourceHandler> sourceHandlerList;
	
	private SourceTreeDataModel sourceTreeDataModel;
	private DismantledTreeDataModel dismantledTreeDataModel;
	
	public SourceNetworking(SourceTreeDataModel sourceTreeDataModel, DismantledTreeDataModel dismantledTreeDataModel) {
		sourceHandlerList = new ArrayList<SourceHandler>();
		this.sourceTreeDataModel = sourceTreeDataModel;
		this.dismantledTreeDataModel = dismantledTreeDataModel;
	}
	
	public synchronized void startSource(String IP, int port) {
		try {
			serverSocket = new ServerSocket(port, 0, InetAddress.getByName(IP));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void stopSource() {
		isRunning = false;
		try {
			serverSocket.close();
			for(SourceHandler handler:sourceHandlerList) {
				handler.closeSourceHandler();
			}
			currentThread.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DefaultTreeModel getTreeDataModel() {
		return sourceTreeDataModel.getTreeModel();
	}

	@Override
	public void run() {
		
		synchronized(this) {
			this.currentThread = Thread.currentThread();
		}
		
		while(isRunning) {
			Socket socket = null;
			SourceHandler sourceHandler = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				if(!isRunning) {
					System.out.println("Source stopped");
					return;
				}
				e.printStackTrace();
			}
			//Places the new connection to a handler in a separate thread
			new Thread(sourceHandler = new SourceHandler(socket, sourceTreeDataModel, dismantledTreeDataModel)).start();
			sourceHandlerList.add(sourceHandler);
		}
			
		
	}

}
