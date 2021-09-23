package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import DataModels.CurrentMachineFilelistModel;
import DataModels.CurrentMachineTableModel;
import DataModels.RemoteMachineFilelistModel;
import DataModels.RemoteMachineTableModel;
import Listeners.CurrentMachineExplorerListener;
import Listeners.RemoteMachineExplorerListener;
import Metadata.CurrentMachineFileMetadata;
import Metadata.RemoteMachineFileMetadata;

public class RemoteMachineExplorer extends JPanel implements MouseListener, ActionListener{
	
	private JTable explorerTable;
	
	private JLabel filePath;
	
	private JTextField filePathText;
	
	private JButton back;
	
	private RemoteMachineFilelistModel remoteMachineFilelistModel;
	private RemoteMachineTableModel remoteMachineTableModel;
	
	private RemoteMachineExplorerListener listener;
	
	public RemoteMachineExplorer() {
		setLayout(new BorderLayout());
		
		filePath = new JLabel("Path: ");
		
		filePathText = new JTextField(10);
		
		explorerTable = new JTable();
		explorerTable.setShowGrid(false);
		explorerTable.addMouseListener(this);
		
		back = new JButton("Back");
		back.addActionListener(this);
		
		setComponents();
		
		loadTableData();
	}
	
	public void setComponents() {
		
		//Only for controls of this explorer
	     JPanel controlsPanel = new JPanel();
	     controlsPanel.setLayout(new FlowLayout());
	     controlsPanel.setBorder(BorderFactory.createEtchedBorder());
	     add(controlsPanel, BorderLayout.PAGE_START);
	     controlsPanel.add(filePath);
	     controlsPanel.add(filePathText);
	     controlsPanel.add(back);
	     
	     JPanel explorerTablePanel = new JPanel();
	     explorerTablePanel.setLayout(new BorderLayout());
	     explorerTablePanel.setBorder(BorderFactory.createEtchedBorder());
	     add(explorerTablePanel, BorderLayout.CENTER);
	     explorerTablePanel.add(new JScrollPane(explorerTable), BorderLayout.CENTER);
	}
	
	public void loadTableData() {
		this.remoteMachineFilelistModel = new RemoteMachineFilelistModel();
		this.remoteMachineTableModel = new RemoteMachineTableModel();
		
		FileSystemView view = FileSystemView.getFileSystemView();
		
        File[] roots = File.listRoots();
        
        for (File root: roots)
        {
            RemoteMachineFileMetadata metadata = new RemoteMachineFileMetadata();
            metadata.setFilePath(root.getAbsolutePath());
            metadata.setFileName(root.getAbsolutePath());
            metadata.setFileTyle(view.getSystemTypeDescription(root));
            remoteMachineFilelistModel.addToList(metadata);
            
        }
        remoteMachineTableModel.setFileList(remoteMachineFilelistModel.getFileList());
        explorerTable.setModel(remoteMachineTableModel);
        
        explorerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		explorerTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		explorerTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		explorerTable.getColumnModel().getColumn(0).setPreferredWidth(180);
	}
	
public void exploreFolder(String folderPath) {
		
		this.remoteMachineFilelistModel = new RemoteMachineFilelistModel();
		this.remoteMachineTableModel = new RemoteMachineTableModel();
		
		File folderExplored = new File(folderPath);
		
		if(folderExplored.isDirectory()) {
			
			FileSystemView view = FileSystemView.getFileSystemView();
			
			for(File fileContained:folderExplored.listFiles()) {
				if(fileContained!= null && !fileContained.isHidden() && fileContained.canRead() && fileContained.canExecute()) {
					//System.out.println(fileContained.getName());
					
					RemoteMachineFileMetadata metadata = new RemoteMachineFileMetadata();
		            metadata.setFileName(fileContained.getName());
		            metadata.setFilePath(fileContained.getAbsolutePath());
		            metadata.setFileTyle(view.getSystemTypeDescription(fileContained));
		            remoteMachineFilelistModel.addToList(metadata);
				}
				
			}
			remoteMachineTableModel.setFileList(remoteMachineFilelistModel.getFileList());
	        explorerTable.setModel(remoteMachineTableModel);
	        
	        explorerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
			explorerTable.getColumnModel().getColumn(1).setPreferredWidth(100);
			explorerTable.getColumnModel().getColumn(2).setPreferredWidth(80);
			explorerTable.getColumnModel().getColumn(0).setPreferredWidth(180);
	        
	        listener.getSelectedPath(folderPath);
			
		}else {
			
		}
	}

    //Sets the hierarchical path
    public void setFileHerarchicalpath(String fileHierarchicalPath) {
	filePathText.setText(fileHierarchicalPath);
	
	if(fileHierarchicalPath.equals("")) {
		back.setEnabled(false);
	}else {
		back.setEnabled(true);
	}
   }
    
    public void setRemoteMachineExplorerListener(RemoteMachineExplorerListener listener) {
    	this.listener = listener;
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
	public void mousePressed(MouseEvent event) {
        int selectedRow = explorerTable.rowAtPoint(event.getPoint());
		
		if(event.getClickCount() == 2) {
			
			if(explorerTable.getValueAt(selectedRow, 2).equals("File folder") || explorerTable.getValueAt(selectedRow, 2).equals("Local Disk")) {
				System.out.println(explorerTable.getValueAt(selectedRow, 0).toString());
				
				LinkedList<RemoteMachineFileMetadata> tempList = this.remoteMachineFilelistModel.getFileList();
				
				for(RemoteMachineFileMetadata metadata:tempList) {
					if(metadata.getFileName().equals(explorerTable.getValueAt(selectedRow, 0).toString())) {
						System.out.println("Object catched: " + metadata.getFileName());
						listener.expandSelectedFolder(metadata.getFilePath());
					}
				}
				
				
			}
			
			if(remoteMachineTableModel.getFileList() == null) {
				//System.out.println("null");
				loadTableData();
			}else{
				exploreFolder(remoteMachineTableModel.getFileList().get(selectedRow).getFilePath());
			}		
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
        JButton source = (JButton)event.getSource();
		
		if(source == back) {
			listener.getHierarchicalPath(filePathText.getText());
		}
		
	}

}
