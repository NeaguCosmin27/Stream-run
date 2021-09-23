package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import DataModels.Connection;
import DataModels.ConnectionsTableModel;
import Listeners.ClientConnectionsTableListener;

public class ClientConnectionsTable extends JPanel{
	
	private JTable clientConnectionTable;
	private ConnectionsTableModel connectionsTableModel;
	
	private ClientConnectionsTableListener listener;
	
	private JPopupMenu popupMenu;
	
	private JMenuItem disconnectItem;
	
	public ClientConnectionsTable() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Active connections"));
		
		connectionsTableModel = new ConnectionsTableModel();
		clientConnectionTable = new JTable(connectionsTableModel);
		popupMenu = new JPopupMenu();
		
		disconnectItem = new JMenuItem("Disconnect");
		popupMenu.add(disconnectItem);
		
		
		add(new JScrollPane(clientConnectionTable), BorderLayout.CENTER);
		
		clientConnectionTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent event) {
				
				int row = clientConnectionTable.rowAtPoint(event.getPoint());
				
				listener.getIndex(row);
				listener.getTreeModel((connectionsTableModel.getConnectionsDB().get(row)).getConnectiontreeModel());
				
                clientConnectionTable.getSelectionModel().setSelectionInterval(row, row);
				
				if(event.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(clientConnectionTable, event.getX(), event.getY());
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		disconnectItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = clientConnectionTable.getSelectedRow();
				
				//Hook up with old closure method
				listener.disconnect(row);
				
				if(clientConnectionTable.getSelectedRowCount()==0) {
					listener.getTreeModel(new DefaultTreeModel(new DefaultMutableTreeNode("Disconnected")));
				}
				
			}
			
		});
	}
	
	public void setData(LinkedList<Connection> connectionsDB) {
		connectionsTableModel.setData(connectionsDB);
	}
	
	public void refreshData() {
		connectionsTableModel.fireTableDataChanged();
	}
	
	public void setClientConnectionsTableListener(ClientConnectionsTableListener listener) {
		this.listener = listener;
	}

}
