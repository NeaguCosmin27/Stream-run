package Metadata;

public class RemoteMachineFileMetadata {

	private String fileName;
	private double fileSize;
	private String fileTyle;
	private String filePath;

	public RemoteMachineFileMetadata() {
		
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileTyle() {
		return fileTyle;
	}

	public void setFileTyle(String fileTyle) {
		this.fileTyle = fileTyle;
	}
}
