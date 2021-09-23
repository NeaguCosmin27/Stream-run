package DataModels;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class SourcesDB {
	
	private LinkedList<Source> sourceList;
	
	public SourcesDB() {
		sourceList = new LinkedList<Source>();
	}
	
	public void addToList(Source source) {
		sourceList.add(source);
	}
	
	public void removeFromList(Source source) {
		sourceList.remove(source);
	}
	
	public LinkedList<Source> getList() {
		return sourceList;
	}
	
	public Source getByIndex(int index) {
		return sourceList.get(index);
	}

}
