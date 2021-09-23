package Events;

import java.util.EventObject;

public class ClientFileReceiverEvent extends EventObject{
	
	private long fileSize;
	private String fileName;
	private double percentage;

	public ClientFileReceiverEvent(Object source, long fileSize, double percentage, String fileName) {
		super(source);
		this.fileSize = fileSize;
		this.fileName = fileName;
		this.percentage = percentage;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public double getPercentage() {
		return percentage;
	}

}
