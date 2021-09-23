package GUI;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import DataModels.Source;
import DataModels.SourceTableModel;
import Listeners.SourcesTableListener;

public class SourcesTable extends JPanel{
	
	private JTable table;
	private JPopupMenu  popupMenu;
	
	private SourceTableModel sourceTableModel;
	
	private SourcesTableListener listener;
	
	public SourcesTable() {
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("Active sources"));
		
		sourceTableModel = new SourceTableModel();
		table = new JTable(sourceTableModel);
		
		popupMenu = new JPopupMenu();
		JMenuItem removeSource = new JMenuItem("Remove source");
		popupMenu.add(removeSource);
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		//pops up the menu
		table.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {
				
				int row = table.rowAtPoint(e.getPoint());
				
				//To be continued in order to get the three model
				//System.out.println(row);
				
				listener.getTreeModel((sourceTableModel.getSourcesList().get(row)).getTreeDataModel());
				
				table.getSelectionModel().setSelectionInterval(row, row);
				
				if(e.getButton() == MouseEvent.BUTTON3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
				
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			
		});
		
		//Removes the selected source row
		removeSource.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				int row = table.getSelectedRow();
				System.out.println(row);
				//Hook up with old closure method
				listener.closeSource(row);
				
				if(table.getSelectedRowCount()==0) {
					listener.getTreeModel(new DefaultTreeModel(new DefaultMutableTreeNode("Source closed")));
				}
				
			}
			
		});
		
	}
	
	public void setData(LinkedList<Source> sourcesList) {
		sourceTableModel.setData(sourcesList);
	}
	
	public void refresh() {
		sourceTableModel.fireTableDataChanged();
	}
	
	public void setSourcesTableListener(SourcesTableListener listener) {
		this.listener = listener;
	}

}
