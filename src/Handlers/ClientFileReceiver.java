package Handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import DataModels.FilePathModel;
import Events.ClientFileReceiverEvent;
import Listeners.ClientFileReceiverListener;
import Metadata.TransferedFileMetadata;

public class ClientFileReceiver extends Thread{
	
	private String originalFilePath;
	private String localFilePath;
	private String localFileName;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private volatile boolean running = true;
	
	private ClientFileReceiverListener listener;
	
	public ClientFileReceiver(String originalFilePath, String localFilePath, String localFileName, ObjectOutputStream oos, ObjectInputStream ois) {
		this.originalFilePath = originalFilePath;
		this.localFilePath = localFilePath;
		this.localFileName = localFileName;
		this.oos = oos;
		this.ois = ois;
	}

	@Override
	public void run() {
		
		
		try {
			
			oos.writeObject(new FilePathModel(originalFilePath));
			oos.flush();
			
				//Receives the main metadata
				TransferedFileMetadata metadata = (TransferedFileMetadata)ois.readObject();
				
				//determines if it is a folder
				if(metadata.isDirectory() == true) {
					
					String folderPath = metadata.getFilePath();
					File receivedFolder = new File(localFilePath + "\\" + folderPath);
					
					if(!receivedFolder.exists()) {
					    receivedFolder.mkdir();
					}
					
					while(running){
						
						//Receives the folder content metadata
						TransferedFileMetadata folderMetadata = (TransferedFileMetadata)ois.readObject();
						
						if(folderMetadata.isFolderItem() == true && folderMetadata.isLastOne() == false) {
							System.out.println(folderMetadata.getFilePath());
							
							//creates the folder if it is confirmed to be a folder and belongs to the main folder
							if(folderMetadata.isDirectory() == true) {
								
								String subfolderPath = folderMetadata.getFilePath();
								File receivedSubfolder = new File(localFilePath + "\\" + subfolderPath);
								
								if(!receivedSubfolder.exists()) {
								    receivedSubfolder.mkdirs();   
								}
										
							//receives the file that is content of the transfered folder or subfolder	
							}else if(folderMetadata.isDirectory() == false) {
								receiveFile(folderMetadata);
							}
							
							
						}else if(folderMetadata.isLastOne() == true){
							this.running = false;
							return;
						}
						
					}
					oos.flush();
				
			    //determines it is only a file
				}else if(metadata.isDirectory() ==  false) {
					
					receiveFile(metadata);
			
				}
				oos.flush();	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
							
	}
	
	//used for file transfer
	public void receiveFile(TransferedFileMetadata metadata) {
		
		long fileSize = metadata.getFileSize();
		String fileNamePath = metadata.getFilePath();
		
		try {
			File receivedFile = new File(localFilePath + "\\" + fileNamePath);
			FileOutputStream fos = new FileOutputStream(receivedFile);
			
			String dirUnpack = (String)ois.readObject();
			
			long bytesSum = 0; //sums up the receivedBytes
			
			double percentage = 0; //stores the calculated transfer percentage
			
			while(!dirUnpack.equalsIgnoreCase("UPLOAD_COMPLETE")) {
				int bytes = Integer.parseInt(dirUnpack.split(" ")[1]);
				byte[] buffer = new byte[bytes];
				
				bytesSum = bytesSum + bytes;
				percentage = (bytesSum*100)/fileSize;
				
				buffer = (byte[])ois.readObject();//get bytes sent from client
				
				fos.write(buffer,0,bytes);// Reads the bytes to file
				
			    dirUnpack = (String)ois.readObject();
			    
			    ClientFileReceiverEvent event = new ClientFileReceiverEvent(this, fileSize, percentage, receivedFile.getName());
			    listener.filePartReceived(event);
			}
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
		
	
	public void setClientFileReceiverListener(ClientFileReceiverListener listener) {
		this.listener = listener;
	}

}
