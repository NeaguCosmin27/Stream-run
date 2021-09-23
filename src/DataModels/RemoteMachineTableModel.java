package DataModels;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import Metadata.CurrentMachineFileMetadata;
import Metadata.RemoteMachineFileMetadata;

public class RemoteMachineTableModel extends AbstractTableModel{

	private String[] columnName = {"Name", "Size", "Type"};
	private LinkedList<RemoteMachineFileMetadata> fileList;
	
	public RemoteMachineTableModel() {
		
	}

	public void setFileList(LinkedList<RemoteMachineFileMetadata> fileList) {
		this.fileList = fileList;
	}
	
	public LinkedList<RemoteMachineFileMetadata> getFileList(){
		return fileList;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		
		RemoteMachineFileMetadata fileMetadata = fileList.get(row);
		switch(column) {
		case 0: 
			return fileMetadata.getFileName();
			
		case 1:
			return fileMetadata.getFileSize();
			
		case 2:
			return fileMetadata.getFileTyle();
		}
		return null;
	}
}
