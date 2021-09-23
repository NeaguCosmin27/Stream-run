package Listeners;

public interface RemoteNetworkingListener {
	public void getFileSize(long filesize);
    public void getTransferPercentage(int percentage);
    public void getFileName(String filename);
}
