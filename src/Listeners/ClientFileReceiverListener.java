package Listeners;

import java.util.EventListener;

import Events.ClientFileReceiverEvent;

public interface ClientFileReceiverListener extends EventListener{
     public void filePartReceived(ClientFileReceiverEvent event);
}
