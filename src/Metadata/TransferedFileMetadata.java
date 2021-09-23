package Metadata;

import java.io.Serializable;

public class TransferedFileMetadata implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filePath;
	private long fileSize;
	private boolean isDirectory;
	private boolean isFolderItem;
	private boolean isLastOne;
	
	public TransferedFileMetadata() {
		
	}
	
	

	public boolean isLastOne() {
		return isLastOne;
	}



	public void setLastOne(boolean isLastOne) {
		this.isLastOne = isLastOne;
	}



	public boolean isFolderItem() {
		return isFolderItem;
	}



	public void setFolderItem(boolean isFolderItem) {
		this.isFolderItem = isFolderItem;
	}



	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}	

}
