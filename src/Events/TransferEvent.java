package Events;

import java.util.EventObject;

public class TransferEvent extends EventObject{
	
	private byte[] buffer;

	public TransferEvent(Object source, byte[] buffer) {
		super(source);
		this.buffer = buffer;
	}
	
	public byte[] getBuffer() {
		return buffer;
	}

}
