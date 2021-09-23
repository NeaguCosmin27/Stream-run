package DataModels;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TreeElementsList implements Serializable{
	
	private static final long serialVersionUID = 12342323432424L;
	private LinkedList<TreeDataElement> elementsList;

	public TreeElementsList() {
		elementsList = new LinkedList<TreeDataElement>();
	}
	
	public void addElement(TreeDataElement treeElement) {
		elementsList.add(treeElement);
	}
	
	public LinkedList<TreeDataElement> getElementsList() {
		return elementsList;
	}

}
