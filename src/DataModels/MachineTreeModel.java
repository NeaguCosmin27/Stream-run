package DataModels;

import java.io.File;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MachineTreeModel {

	private File root;
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	
	public MachineTreeModel(File root) {
		this.root = root;
		this.rootNode = new DefaultMutableTreeNode(root);
		this.treeModel = new DefaultTreeModel(rootNode);
		
		createChildNodes(root);
	}
	
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}
	
	public void createChildNodes(File root) {
		
		for(File rootChild:root.listFiles()) {
			
			try {
				if(rootChild == null) {
					return;
				}else {
					if(rootChild.isDirectory()) {
						
						System.out.println("Directory: " + rootChild.getAbsolutePath());
						createChildNodes(rootChild);
						
					}else if(!rootChild.isDirectory()) {
						
						System.out.println("File: " + rootChild.getAbsolutePath());
					}
				}
				
			}catch(NullPointerException exception) {
				
			}
			
			
		}
				
		}

}
