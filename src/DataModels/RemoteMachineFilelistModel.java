package DataModels;

import java.util.LinkedList;

import Metadata.RemoteMachineFileMetadata;

public class RemoteMachineFilelistModel {

private LinkedList<RemoteMachineFileMetadata> fileList;
	
	public RemoteMachineFilelistModel() {
		fileList = new LinkedList<RemoteMachineFileMetadata>();
	}
	
	public void addToList(RemoteMachineFileMetadata fileMetadata) {
		fileList.add(fileMetadata);
	}
	
	public void removeFormList(RemoteMachineFileMetadata fileMetadata) {
		fileList.remove(fileMetadata);
	}
	
	public LinkedList<RemoteMachineFileMetadata> getFileList(){
		return fileList;
	}
}
