package DataModels;

import java.io.File;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import Metadata.ClientTreeMetadata;

public class ClientTreeDataModel extends Thread{
	
	private volatile DefaultMutableTreeNode root;
	private volatile DefaultTreeModel treeModel;
	private volatile TreeElementsList clientTreeElementsList;
	private File clientRootFile;
	
	public ClientTreeDataModel(TreeElementsList clientTreeElementsList) {
		this.clientTreeElementsList = clientTreeElementsList;
	}
	
	public DefaultTreeModel getAssembledTreeModel() {
		return treeModel;
	}
	
	public void run() {
		
		LinkedList<ClientTreeMetadata> parentNodes = new LinkedList<ClientTreeMetadata>();
		
		for(TreeDataElement element:clientTreeElementsList.getElementsList()) {
			
			//System.out.println(element.getParentElementName());
			
			if(element.isRoot()== true && element.isNode() == true) {
				
				root = new DefaultMutableTreeNode(element.getElementName());
				treeModel = new DefaultTreeModel(root);
				
				ClientTreeMetadata treeMetadata = new ClientTreeMetadata(root, root.toString());
				parentNodes.add(treeMetadata);
				
				
			}else if(element.isRoot() == false && element.isNode() == false && element.getParentElementName() !=null) {
				
				DefaultMutableTreeNode clientChildNode = new DefaultMutableTreeNode(element.getElementName());
				
				for(ClientTreeMetadata metadata:parentNodes) {
					
					if(metadata.getNodeName().equals(element.getParentElementName())) {
						metadata.getNode().add(clientChildNode);
					}
				}
				
				/*if((parentNode.toString()).equals(parentNode.toString())) {
					parentNode.add(clientChildNode);
	
					//System.out.println("Something");
				}*/
				
				//treeModel.in
					
			}else if(element.isRoot() == false && element.isNode() == true && element.getParentElementName() != null) {
				
				DefaultMutableTreeNode clientChildNode = new DefaultMutableTreeNode(element.getElementName());
				
						ClientTreeMetadata metadataParent = new ClientTreeMetadata(clientChildNode, clientChildNode.toString());
						
						for(ClientTreeMetadata metadata:parentNodes) {
							
							if(metadata.getNodeName().equals(element.getParentElementName())) {
								metadata.getNode().add(clientChildNode);
							}
						}
						parentNodes.add(metadataParent);
	   
				}
				
				
			
		}
		
	}


}
