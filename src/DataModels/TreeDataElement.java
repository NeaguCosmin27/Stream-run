package DataModels;

import java.io.Serializable;

public class TreeDataElement implements Serializable{
	
	private static final long serialVersionUID = 149102L;
	private String elementName;
	private boolean isRoot = false;
	private boolean isNode = false;
	private String parentElementName;
	private int index;

	public TreeDataElement() {
		
	}
	
	
	
	public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}



	public boolean isRoot() {
		return isRoot;
	}



	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}



	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public boolean isNode() {
		return isNode;
	}

	public void setNode(boolean isNode) {
		this.isNode = isNode;
	}

	public String getParentElementName() {
		return parentElementName;
	}

	public void setParentElementName(String parentElementName) {
		this.parentElementName = parentElementName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
