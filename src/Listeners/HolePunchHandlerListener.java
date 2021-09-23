package Listeners;

public interface HolePunchHandlerListener {
      public void sessionConnectionStatus(String status);
      public void remoteConnectionStatus(String status, boolean isConnected);
}
