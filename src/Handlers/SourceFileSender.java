package Handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

import Metadata.TransferedFileMetadata;

public class SourceFileSender implements Runnable{
	
	private String filePathReceived;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public SourceFileSender(String filePathReceived, ObjectOutputStream oos, ObjectInputStream ois) {
		this.filePathReceived = filePathReceived;
		this.oos = oos;
		this.ois = ois;
	}

	@Override
	public void run() {
		
		File fileToSend = new File(filePathReceived);
		
		TransferedFileMetadata metadata = new TransferedFileMetadata();
		
		if(fileToSend.isDirectory()) {
			
			Path fileToSendPath = fileToSend.toPath();
			
			for(int i = 0; i<fileToSendPath.getNameCount(); i++) {
				if((fileToSendPath.getName(i).toString()).equals(fileToSend.getName())) {
					
					int rootPathIndex = i;
	   			    int fileIndex = i+1;
	   			    
	   			    metadata.setDirectory(true);
	   			    metadata.setFilePath(fileToSendPath.subpath(rootPathIndex, fileIndex).toString());
	   			    try {
						oos.writeObject(metadata);
						oos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			      
	   			    setDirItems(fileToSend, rootPathIndex, fileIndex); 
	   			
				}
			//-----------------Experimental block---------------------	
				
		   //-----------------Experimental block---------------------	
			}
			
			try {
				TransferedFileMetadata metadataEnd = new TransferedFileMetadata();
				metadataEnd.setLastOne(true);
				oos.writeObject(metadataEnd);
				oos.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else if(!fileToSend.isDirectory()) {
			
			byte[] buffer = new byte[2048 * 2048];
			
			Path fileToSendPath = fileToSend.toPath();
			
			try {
				FileInputStream fis = new FileInputStream(fileToSend);
				
				metadata.setFilePath(fileToSend.getName());
				metadata.setFileSize(fileToSend.length());
				metadata.setDirectory(false);
				
				oos.writeObject(metadata);
				oos.flush();
				
				int retVal = fis.read(buffer); // reads pieces from buffer
				
				int counter = 0; // counts sent pieces
				
				while(retVal != -1) {
                    counter += retVal;
			        
			        oos.writeObject("UPACK "+retVal);
			        oos.flush();
			       
			        oos.writeObject(buffer);//send bytes
			        oos.reset();
			        oos.flush();
			        
			        retVal = fis.read(buffer);
				}
				oos.writeObject("UPLOAD_COMPLETE");
				oos.flush();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}

			
	}
	
	public void setDirItems(File folder, int rootPathIndex, int fileIndex) {
		
		
		for(File file:folder.listFiles()) {
			
			if(file.isDirectory()) {
				
				Path filePath = file.toPath();
				
				TransferedFileMetadata metadataSubfolder = new TransferedFileMetadata();
				metadataSubfolder.setFolderItem(true);
				metadataSubfolder.setDirectory(true);
				metadataSubfolder.setFilePath(filePath.subpath(rootPathIndex, fileIndex+1).toString());
				
				try {
					oos.writeObject(metadataSubfolder);
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				setDirItems(file, rootPathIndex, fileIndex+1);
				
			}else if(!file.isDirectory()) {
				
				TransferedFileMetadata metadataFile = new TransferedFileMetadata();
				
				byte[] buffer = new byte[2048 * 2048];
				
				Path filePath = file.toPath();
				
				try {
					FileInputStream fis = new FileInputStream(file);
					
					metadataFile.setFilePath(filePath.subpath(rootPathIndex, fileIndex+1).toString());
					metadataFile.setFileSize(file.length());
					metadataFile.setDirectory(false);
					metadataFile.setFolderItem(true);
					
					//System.out.println("Set path: " + filePath.subpath(rootPathIndex, fileIndex+1).toString());
					
					oos.writeObject(metadataFile);
					oos.flush();
					
					
					int retVal = fis.read(buffer); // reads pieces from buffer
					
					int counter = 0; // counts sent pieces
					
					while(retVal != -1) {
	                    counter += retVal;
				        
				        oos.writeObject("UPACK "+retVal);
				       
				        oos.writeObject(buffer);//send bytes
				        oos.reset();
				        oos.flush();
				        
				        retVal = fis.read(buffer);
					}
					oos.writeObject("UPLOAD_COMPLETE");
					oos.flush();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
		

}
