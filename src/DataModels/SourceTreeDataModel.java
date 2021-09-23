package DataModels;

import java.io.File;
import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class SourceTreeDataModel extends Thread implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234567845323342234L;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private File fileRoot;
	
	public SourceTreeDataModel(String selectedFilePath) {
		fileRoot = new File(selectedFilePath);
		root = new DefaultMutableTreeNode(fileRoot);
		treeModel = new DefaultTreeModel(root);
	}

	@Override
	public void run() {
		CreateChildNodes createChildNodes = new CreateChildNodes(root, fileRoot);
		createChildNodes.start();
		try {
			createChildNodes.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}
	
	public void updateTreeModel() {
		
	}

}

class CreateChildNodes extends Thread{
	
	private DefaultMutableTreeNode root;
	private File fileRoot;
	
	public CreateChildNodes(DefaultMutableTreeNode root, File fileRoot) {
		this.root = root;
		this.fileRoot = fileRoot;
	}
	
	public void createChild(DefaultMutableTreeNode newRoot, File newfileRoot) {
		File[] files = newfileRoot.listFiles();
        if (files == null) return;
        

        for (File file : files) {
            DefaultMutableTreeNode fileNode = 
                    new DefaultMutableTreeNode(file.getName());
            newRoot.add(fileNode);
            if (file.isDirectory()) {
            	createChild(fileNode, file);
            }
        }
	}
	
	public void run() {
		createChild(root, fileRoot);
	}
	
}
