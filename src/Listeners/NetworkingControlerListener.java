package Listeners;

import java.util.EventListener;
import Events.NetworkControlerEvent;

public interface NetworkingControlerListener extends EventListener{
	public void actionHappened(NetworkControlerEvent event);
	public void getReceivedFileSize(long fileSize);
	public void getFileName(String fileName);
	public void getTransferPercentage(int percentage);
}
