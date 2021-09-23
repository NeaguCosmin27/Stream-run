package Listeners;

public interface ConnectionDialogListener {
	 public void enableMainframe();
     public void connect(String IP, String hostname, int port);
     public void disconnect();
}
