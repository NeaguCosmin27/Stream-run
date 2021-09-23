package DataModels;

import java.io.Serializable;

public class FilePathModel implements Serializable{
	
	private String originalFilePath;
	
	public FilePathModel(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}
	
	public String getPath() {
		return originalFilePath;
	}

}
