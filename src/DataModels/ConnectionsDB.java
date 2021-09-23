package DataModels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConnectionsDB {
	
	private LinkedList<Connection> connectionsList;
	
	public ConnectionsDB() {
		connectionsList = new LinkedList<Connection>();
	}
	
	public void addConnection(Connection connection) {
		connectionsList.add(connection);
	}
	
	public void removeConnection(Connection connection) {
		connectionsList.remove(connection);
	}
	
	public LinkedList<Connection> getConnectionsList(){
		return connectionsList;
	}
	
	public Connection getByIndex(int index) {
		return connectionsList.get(index);
	}

}
