package Handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.tree.DefaultTreeModel;

import DataModels.DismantledTreeDataModel;
import DataModels.FilePathModel;
import DataModels.SourceTreeDataModel;

public class SourceHandler implements Runnable{
	
	private Socket socket = null;
	private Thread currentThread;
	private boolean sourceHandlerRunning = true;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private SourceTreeDataModel sourceTreeDataModel;
	private DismantledTreeDataModel dismantledTreeDataModel;
	
	public SourceHandler(Socket socket, SourceTreeDataModel sourceTreeDataModel, DismantledTreeDataModel dismantledTreeDataModel) {
		this.socket = socket;
		this.sourceTreeDataModel = sourceTreeDataModel;
		this.dismantledTreeDataModel = dismantledTreeDataModel;
	}

	@Override
	public void run() {
		
		synchronized(this) {
			this.currentThread = Thread.currentThread();
		}
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		while(sourceHandlerRunning) {
			//To be continued....
			try {
				
				
				//
				Object object = (Object)ois.readObject();
				
				if(object instanceof String) {
					String commandReceived = (String)object;
					
					if(commandReceived.equals("DataTree")) {
						
						
						//DefaultTreeModel temporaryTreeModel = sourceTreeDataModel.getTreeModel();
						//oos.writeObject(sourceTreeDataModel);
						oos.writeObject(dismantledTreeDataModel);
						oos.flush();
						//System.out.println("Source data tree requested");
					}
				}else if(object instanceof FilePathModel) {
					
					String filePathReceived = ((FilePathModel)object).getPath();
					System.out.println("Source path received: " + filePathReceived);
					
					new Thread(new SourceFileSender(filePathReceived, oos, ois)).start();
					
				}
				
				
			} catch (IOException e) {
				try {
					ois.close();
					this.sourceHandlerRunning = false;
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void closeSourceHandler() {
		try {
			sourceHandlerRunning = false;
			socket.close();
			currentThread.interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
