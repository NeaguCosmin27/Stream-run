package Listeners;

public interface FileTransferListener {
	
	public void getConnectionDialog();
	public void getSourceDialog();
	public void closeSource(int index);
	public void closeConnection(int index);
	public void retrieveFile(int index, String originalFilePath, String localFilePath, String localFileName);

}
