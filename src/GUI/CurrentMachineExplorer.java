package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

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
import Listeners.CurrentMachineExplorerListener;
import Metadata.CurrentMachineFileMetadata;


public class CurrentMachineExplorer extends JPanel implements MouseListener, ActionListener{
	
	private JTable explorerTable;
	private CurrentMachineTableModel currentMachineTableModel;
	private CurrentMachineFilelistModel currentMachineFilelistModel;
	
	private JLabel filePath;
	
	private JTextField filePathText;
	
	private JButton back;
	
	private CurrentMachineExplorerListener listener;
	
	public CurrentMachineExplorer() {
		setLayout(new BorderLayout());
		
		explorerTable = new JTable();
		explorerTable.setShowGrid(false);
		explorerTable.addMouseListener(this);
		
		filePath = new JLabel("Path");
		filePathText = new JTextField(10);
		
		back = new JButton("Back");
		back.addActionListener(this);
		//back.setEnabled(false);
		
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
		this.currentMachineFilelistModel = new CurrentMachineFilelistModel();
		this.currentMachineTableModel = new CurrentMachineTableModel();
		
		FileSystemView view = FileSystemView.getFileSystemView();
		
        File[] roots = File.listRoots();
        
        for (File root: roots)
        {
            System.out.println();
            CurrentMachineFileMetadata metadata = new CurrentMachineFileMetadata();
            metadata.setFilePath(root.getAbsolutePath());
            metadata.setFileName(root.getAbsolutePath());
            metadata.setFileTyle(view.getSystemTypeDescription(root));
            currentMachineFilelistModel.addToList(metadata);
            
        }
        currentMachineTableModel.setFileList(currentMachineFilelistModel.getFileList());
        explorerTable.setModel(currentMachineTableModel);
        
        explorerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		explorerTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		explorerTable.getColumnModel().getColumn(2).setPreferredWidth(80);
		explorerTable.getColumnModel().getColumn(0).setPreferredWidth(180);
	}
	
    public void exploreFolder(String folderPath) {
		
		this.currentMachineFilelistModel = new CurrentMachineFilelistModel();
		this.currentMachineTableModel = new CurrentMachineTableModel();
		
		File folderExplored = new File(folderPath);
		
		if(folderExplored.isDirectory()) {
			
			FileSystemView view = FileSystemView.getFileSystemView();
			
			for(File fileContained:folderExplored.listFiles()) {
				if(fileContained!= null && !fileContained.isHidden() && fileContained.canRead() && fileContained.canExecute()) {
					//System.out.println(fileContained.getAbsolutePath());
					
					CurrentMachineFileMetadata metadata = new CurrentMachineFileMetadata();
		            metadata.setFileName(fileContained.getName());
		            metadata.setFilePath(fileContained.getAbsolutePath());
		            metadata.setFileTyle(view.getSystemTypeDescription(fileContained));
		            currentMachineFilelistModel.addToList(metadata);
				}
				
			}
			currentMachineTableModel.setFileList(currentMachineFilelistModel.getFileList());
	        explorerTable.setModel(currentMachineTableModel);
	        
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

    public void setCurrentMachineExplorerListener(CurrentMachineExplorerListener listener) {
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
			
			// to be userd for remote file explorer update
			System.out.println(explorerTable.getValueAt(selectedRow, 0).toString());
			
			if(currentMachineTableModel.getFileList() == null) {
				//System.out.println("null");
				loadTableData();
			}else{
				exploreFolder(currentMachineTableModel.getFileList().get(selectedRow).getFilePath());	
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
