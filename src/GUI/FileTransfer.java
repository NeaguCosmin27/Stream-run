package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import DataModels.Connection;
import DataModels.Source;
import Listeners.ClientConnectionsTableListener;
import Listeners.FileTransferListener;
import Listeners.SourcesTableListener;

public class FileTransfer extends JPanel implements ActionListener, TreeSelectionListener{
	
	private JLabel fileLocation;
	private JLabel fileSize;
	private JLabel transfered;
	private JLabel selectedFile;
	private JLabel transferedFileName;
	private JLabel openFile;
	
	private JTree tree;	
	private JTree clientTree;
	
	private JScrollPane treePane;
	private JScrollPane clientTreePane;
	
	private JTabbedPane sourceConnectionTabs;
	
	private JTextField fileLocationText;
	private JTextField selectedFileText;
	private JTextField fileSizeText;
	private JTextField transferedFileNameText;
	private JTextField openFileText;
	
	private JToolBar toolbar;
	private JButton setSource;
	private JButton sourceConnection;
	private JButton transferFile;
	private JButton setFileLocation;
	private JButton startTransfer;
	private JButton chooseForSending;
	private JButton send;
	
	private JProgressBar transferProgress;
	
	private FileTransferListener listener;
	
	private SourcesTable sourcesTable;
	private ClientConnectionsTable clientConnectionsTable;
	
	private JFileChooser chooser;
	
	private int clientConnectionIndex;
	private String selectedFilePath;
	
	public FileTransfer() {
		
		setLayout(new BorderLayout());
		
		fileLocation = new JLabel("Save Location: ");
		fileSize = new JLabel("File size: ");
		transfered = new JLabel("Transfered: ");
		selectedFile = new JLabel("Selected file: ");
		transferedFileName = new JLabel("File name: ");
		openFile = new JLabel("Open file: ");
		
		fileLocationText = new JTextField(15);
		selectedFileText = new JTextField(15);
		selectedFileText.setEditable(false);
		fileSizeText = new JTextField(13);
		fileSizeText.setEditable(false);
		transferedFileNameText = new JTextField(13);
		transferedFileNameText.setEditable(false);
		openFileText = new JTextField(15);
		
		toolbar = new JToolBar();
	    
		//Sources trees initialization
		tree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("No active source...")));
		treePane = new JScrollPane(tree);
		
		//client trees for connection
		clientTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("No active connection...")));
		clientTreePane = new JScrollPane(clientTree);
		clientTree.addTreeSelectionListener(this);
		
		sourceConnectionTabs = new JTabbedPane();
	    
	    setSource = new JButton(" Add source");
	    setSource.addActionListener(this);
	    toolbar.add(setSource);
	    
	    toolbar.addSeparator();
	    
	    sourceConnection = new JButton("Connect to source");
	    setFileLocation = new JButton("...");
	    startTransfer = new JButton("Retrieve");
	    chooseForSending = new JButton("...");
	    send = new JButton("Send");
	    
	    chooser = new JFileChooser();
	    
	    sourceConnection.addActionListener(this);
	    setFileLocation.addActionListener(this);
	    startTransfer.addActionListener(this);
	    chooseForSending.addActionListener(this);
	    send.addActionListener(this);
	    
	    toolbar.add(sourceConnection);
	    
	    transferProgress = new JProgressBar(0, 100);
	    transferProgress.setValue(0);
	    transferProgress.setStringPainted(true);
	    
	    sourcesTable= new SourcesTable();
	    clientConnectionsTable = new ClientConnectionsTable();
	    
	    //SourcesTable listener
	    sourcesTable.setSourcesTableListener(new SourcesTableListener() {

			@Override
			public void closeSource(int index) {
				listener.closeSource(index);	
			}

			@Override
			public void getTreeModel(DefaultTreeModel treeModel) {
				tree.setModel(treeModel);	
			}
	    	
	    });
	    
	    clientConnectionsTable.setClientConnectionsTableListener(new ClientConnectionsTableListener() {

			@Override
			public void disconnect(int index) {
				listener.closeConnection(index);	
			}

			@Override
			public void getTreeModel(DefaultTreeModel treeModel) {
				clientTree.setModel(treeModel);	
			}

			@Override
			public void getIndex(int index) {
				clientConnectionIndex = index;
			}
	    	
	    });
	    
	    addTransferControls();
	    addSourcesConnectionsTabs();		
	}
    
	//Button listeners
	@Override
	public void actionPerformed(ActionEvent event) {
		JButton source = (JButton)event.getSource();
		
		if(source == setSource) {
			listener.getSourceDialog();	
		}else if(source == sourceConnection) {
			listener.getConnectionDialog();	
		}else if(source == setFileLocation) {
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(chooser.showSaveDialog(FileTransfer.this)== JFileChooser.APPROVE_OPTION) {
				fileLocationText.setText(chooser.getSelectedFile().getPath());
			}
		}else if(source == startTransfer) {
			listener.retrieveFile(clientConnectionIndex, selectedFilePath, fileLocationText.getText(), selectedFileText.getText());
		}else if(source == chooseForSending) {
			
		}else if(source == send) {
			
		}
		
	}
		
	public void setFileTransferListener(FileTransferListener listener) {
		this.listener = listener;
	}
	
	//Sets data for the sourcesTable
	public void setSourceTableData(LinkedList<Source> sourcesList) {
		sourcesTable.setData(sourcesList);
	}
	
	//Sets data for connectionsTable
	public void setConnectionsTableData(LinkedList<Connection> connectionsList) {
		clientConnectionsTable.setData(connectionsList);
	}
	
	//update the sources table
	public void refreshTable() {
		sourcesTable.refresh();
	}
	
	//update the connections table
	public void refreshConnectionsTable() {
		clientConnectionsTable.refreshData();
	}
	
	//--------------------------------------------------- Controls ------------------------------------------------------------
	
	//Sets the transfer controls
	public void addTransferControls() {
		//Main control panel
		JPanel mainPanel = new JPanel();
		add(mainPanel, BorderLayout.PAGE_START);
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(toolbar, BorderLayout.PAGE_START);
		
		JPanel transferControls = new JPanel();
		transferControls.setLayout(new GridBagLayout());
		transferControls.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.add(transferControls, BorderLayout.WEST);
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.gridx = 0;
		gc.gridy = 0;
		transferControls.add(fileLocation, gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		transferControls.add(fileLocationText, gc);
		
		gc.gridx = 2;
		gc.gridy = 0;
		transferControls.add(setFileLocation, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		transferControls.add(selectedFile, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		transferControls.add(selectedFileText, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		transferControls.add(openFile, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		transferControls.add(openFileText, gc);
		
		gc.gridx = 2;
		gc.gridy = 2;
		transferControls.add(chooseForSending, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		transferControls.add(startTransfer, gc);
		
		gc.gridx = 1;
		gc.gridy = 4;
		transferControls.add(send, gc);
		
		JPanel transferPanel = new JPanel();
		transferPanel.setLayout(new GridBagLayout());
		transferPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.add(transferPanel, BorderLayout.CENTER);
		GridBagConstraints gcMain = new GridBagConstraints();
		
		gcMain.gridx = 0;
		gcMain.gridy = 0;
		transferPanel.add(transfered, gcMain);
		
		gcMain.gridx = 1;
		gcMain.gridy = 0;
		transferPanel.add(transferProgress, gcMain);
		
		gcMain.gridx = 0;
		gcMain.gridy = 1;
		transferPanel.add(fileSize, gcMain);
		
		gcMain.gridx = 1;
		gcMain.gridy = 1;
		transferPanel.add(fileSizeText, gcMain);
		
		gcMain.gridx = 0;
		gcMain.gridy = 2;
		transferPanel.add(transferedFileName, gcMain);
		
		gcMain.gridx = 1;
		gcMain.gridy = 2;
		transferPanel.add(transferedFileNameText, gcMain);
				
	}
	
	//Separate Tabs for connections and sources
	public void addSourcesConnectionsTabs(){
		
		JPanel sourcesPanel = new JPanel();
		sourcesPanel.setLayout(new BorderLayout());
		
		//For source tree display
		Dimension dim = getPreferredSize();
		dim.width = 200;
		treePane.setPreferredSize(dim);
		sourcesPanel.add(treePane, BorderLayout.WEST);
		
		//sources table
		sourcesPanel.add(sourcesTable, BorderLayout.CENTER);
		
		JPanel connectionPanel = new JPanel();
		connectionPanel.setLayout(new BorderLayout());
		
		sourceConnectionTabs.addTab("Active sources", sourcesPanel);
		sourceConnectionTabs.addTab("Active connections", connectionPanel);
		
		//For client tree display
		Dimension dimClientTree = getPreferredSize();
		dimClientTree.width = 200;
		clientTreePane.setPreferredSize(dimClientTree);
		connectionPanel.add(clientTreePane, BorderLayout.WEST);
		connectionPanel.add(clientConnectionsTable, BorderLayout.CENTER);
		
		add(sourceConnectionTabs, BorderLayout.CENTER);
	}

	//Gets partial path for the selected file in client tree and displays selection
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		selectedFilePath = e.getPath().toString().replaceAll("[\\[\\]]", "").replace(", ", "\\");
		selectedFileText.setText(new File(selectedFilePath).getName());
	}
	
	public void setReceivedFileSize(long fileSize) {
		
		if(fileSize <= 1024) {
			
			fileSizeText.setText(fileSize + " Byte");
			
		}else if((fileSize >= 1024) && (fileSize < 1024*1024)) {
			
			double sizeKB = fileSize/1024;
			fileSizeText.setText(sizeKB + " Kb");
			
		}else if((fileSize >=1024*1024) && (fileSize < 1024*1024*1024)) {
			
			double receivedValue = (double)fileSize/(1024*1024);
			
			DecimalFormat df = new DecimalFormat("#.##");

		    df.setRoundingMode(RoundingMode.FLOOR);

		    double result = new Double(df.format(receivedValue));
						
			fileSizeText.setText(result + " MB");
			
		}else if(fileSize >= 1024*1024*1024) {
			
            double receivedValue = (double)fileSize/(1024*1024*1024);
			
			DecimalFormat df = new DecimalFormat("#.##");

		    df.setRoundingMode(RoundingMode.FLOOR);

		    double result = new Double(df.format(receivedValue));
						
			fileSizeText.setText(result + " GB");
		}
	}
	
	public void setReceivedFileName(String fileName) {
		transferedFileNameText.setText(fileName);
	}
	
	public void setTransferPercentage(int percentage) {
		transferProgress.setValue(percentage);
		//transferProgress.setString(percentage + " %");
		
	}

}
