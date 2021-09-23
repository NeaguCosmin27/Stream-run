package DataModels;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DismantledTreeDataModel extends Thread implements Serializable{
	
	private static final long serialVersionUID = 1492199129052020L;
	private TreeElementsList treeElementsList;
	private File localSelectedFile;
	
	
	public DismantledTreeDataModel(String selectedFilePath) {
		treeElementsList = new TreeElementsList();	
		localSelectedFile = new File(selectedFilePath);
	}
	
	public TreeElementsList getTreeElementsList(){
		return treeElementsList;
	}
	
	public void run() {
		
		if(localSelectedFile.isDirectory()) {
			
			TreeDataElement element = new TreeDataElement();
			element.setElementName(localSelectedFile.getAbsolutePath());
			element.setRoot(true);
			element.setNode(true);
			
			System.out.println(element.getElementName());
			
			treeElementsList.addElement(element);
			
			setTreeElements(localSelectedFile, element);
			
			
		}else if(!localSelectedFile.isDirectory()) {
			TreeDataElement element = new TreeDataElement();
			element.setElementName(localSelectedFile.getAbsolutePath());
			element.setRoot(true);
			element.setNode(true);
			
			treeElementsList.addElement(element);
		}
		
	}
	
	
	public void setTreeElements(File localSelectedFile, TreeDataElement parentElement) {
		
		
		for(File file:localSelectedFile.listFiles()) {
			
			try {
				
				if(file.isDirectory()) {
					
					TreeDataElement element = new TreeDataElement();
					element.setElementName(file.getName());
					element.setRoot(false);
					element.setNode(true);
					element.setParentElementName(parentElement.getElementName());
					
					treeElementsList.addElement(element);
					
					setTreeElements(file, element);
					
				}else if(!file.isDirectory()) {
					
					TreeDataElement element = new TreeDataElement();
					element.setElementName(file.getName());
					element.setRoot(false);
					element.setNode(false);
					element.setParentElementName(parentElement.getElementName());
					
					treeElementsList.addElement(element);
					
				}
				
			}catch(NullPointerException exception) {
				System.out.println(file.getName());
			}
			
			
		}	
		
		
	}

}
