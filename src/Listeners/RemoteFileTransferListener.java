package Listeners;

import Metadata.PeerConnectionRequest;
import Metadata.PeerIdentity;

public interface RemoteFileTransferListener {
     public void startSharingSession(PeerIdentity identity);
     public void stopSharingSession();
     public void connectToRemoteSession(PeerConnectionRequest connectionRequest);
     public void disconnectFromRemoteSession();
     public void expandSelectedFolder(String remoteFolderPath);
}
