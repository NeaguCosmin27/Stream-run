package DataModels;

import java.util.LinkedList;

import Metadata.CurrentMachineFileMetadata;


public class CurrentMachineFilelistModel {
	
	private LinkedList<CurrentMachineFileMetadata> fileList;
	
	public CurrentMachineFilelistModel() {
		fileList = new LinkedList<CurrentMachineFileMetadata>();
	}
	
	public void addToList(CurrentMachineFileMetadata fileMetadata) {
		fileList.add(fileMetadata);
	}
	
	public void removeFormList(CurrentMachineFileMetadata fileMetadata) {
		fileList.remove(fileMetadata);
	}
	
	public LinkedList<CurrentMachineFileMetadata> getFileList(){
		return fileList;
	}

}
