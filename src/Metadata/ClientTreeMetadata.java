package Metadata;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClientTreeMetadata {
	
	private DefaultMutableTreeNode node;
	private String nodeName;
	
	public ClientTreeMetadata(DefaultMutableTreeNode node, String nodeName) {
		this.node = node;
		this.nodeName = nodeName;	
	}

	public DefaultMutableTreeNode getNode() {
		return node;
	}

	public void setNode(DefaultMutableTreeNode node) {
		this.node = node;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	

}
