package DataModels;

import java.io.Serializable;

public class RemotePeerExplorerUpdate implements Serializable{
	
	private static final long serialVersionUID = 1402199128772131L;
	private String remoteFolderPath;

	public RemotePeerExplorerUpdate() {
		
	}
	
	public String getRemoteFolderPath() {
		return remoteFolderPath;
	}
	
	public void setRemoteFolderPath(String remoteFolderPath) {
		this.remoteFolderPath = remoteFolderPath;
	}

}
