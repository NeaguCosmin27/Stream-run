package DataModels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class SourceTableModel extends AbstractTableModel {
	
	private List<Source> sourcesDB;
	
	private String[] colName = {"Source name", "Machine hostname", "Source path", "Source IP", "Source port"};
	
	public SourceTableModel() {
		
	}
	
	public List<Source> getSourcesList(){
		return sourcesDB;
	}
	
	public void setData(List<Source> sourcesDB) {
		this.sourcesDB = sourcesDB;
	}
	
	

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return colName[column];
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int getRowCount() {
		return sourcesDB.size();
	}

	@Override
	public Object getValueAt(int row, int col) {

        Source source = sourcesDB.get(row);
        
        switch(col) {
        case 0: 
        	return source.getName();
        	
        case 1:
        	return source.getHostname();
        	
        case 2:
        	return source.getSelectedFilePath();
      
        case 3:
        	return source.getIP();
        
        case 4:
        	return source.getPort();
        	 
        }
        
		return null;
	}

}
