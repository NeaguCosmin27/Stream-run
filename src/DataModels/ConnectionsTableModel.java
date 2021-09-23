package DataModels;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class ConnectionsTableModel extends AbstractTableModel{
	
	private LinkedList<Connection> connectionsDB;
	
	private String[] colname = {"Source Hostname", "Source IP", "Source port"};
	
	public ConnectionsTableModel() {
		
	}
	
	public void setData(LinkedList<Connection> connectionsDB) {
		this.connectionsDB = connectionsDB;
	}
	
	public LinkedList<Connection> getConnectionsDB(){
		return connectionsDB;
	}
	
	@Override
	public String getColumnName(int column) {
		return colname[column];
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getRowCount() {
		return connectionsDB.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		Connection source = connectionsDB.get(row);
		
		switch(col) {
		case 0:
			return source.getHostname();
		
		case 1:
			return source.getIP();
		
		case 2:
			return source.getPort();
		}
		return null;
	}

}
