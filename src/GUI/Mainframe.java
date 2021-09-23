package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import Controlers.NetworkingControler;
import Controlers.RemoteFileTransferControler;
import Events.NetworkControlerEvent;
import Listeners.ConnectionDialogListener;
import Listeners.FileTransferListener;
import Listeners.NetworkingControlerListener;
import Listeners.RemoteFileTransferControlerListener;
import Listeners.RemoteFileTransferListener;
import Listeners.SourceDialogListener;
import Metadata.PeerConnectionRequest;
import Metadata.PeerIdentity;

public class Mainframe extends JFrame{
	
	private JTabbedPane tabs;
	private FileTransfer fileTransfer;
	private FileSynchronization fileSynchronization;
	private SourceDialog sourceDialog;
	private ConnectionDialog connectionDialog;
	private SystemSettingsPanel systemSettingsPanel;
	private RemoteFileTransfer remoteFileTransfer;
	private NetworkSettingsDialog networkSettingsDialog;
	private RemoteFileTransferControler remoteFileTransferControler;
	
	//Networking
	private NetworkingControler networkingControler;
	
	public Mainframe() {
		super("StreamRun");
		setVisible(true);
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		setJMenuBar(setMenuBar());
		
		
		sourceDialog = new SourceDialog(Mainframe.this);
		connectionDialog = new ConnectionDialog(Mainframe.this);
		fileTransfer = new FileTransfer();
		fileSynchronization = new FileSynchronization();
		systemSettingsPanel = new SystemSettingsPanel(Mainframe.this);
		networkSettingsDialog = new NetworkSettingsDialog(Mainframe.this);
		remoteFileTransfer = new RemoteFileTransfer();
		
		//Controls the remote file transfer
		remoteFileTransferControler = new RemoteFileTransferControler();
		remoteFileTransferControler.setRemoteFileTransferControlerListener(new RemoteFileTransferControlerListener() {

			//Works only for shared sessions
			@Override
			public void getServerConnectionStatus(String status, boolean isSetup) {
				remoteFileTransfer.setServerConnectionStatus(status);
				remoteFileTransfer.lockUnlockSessionButton(isSetup);
			}	
		});
		
		//GUI for file transfer and listener
		remoteFileTransfer.setRemoteFileTransferListener(new RemoteFileTransferListener() {

			@Override
			public void startSharingSession(PeerIdentity identity) {
				remoteFileTransferControler.startSharingSession(networkSettingsDialog.getServerExternalIP(), Integer.parseInt(networkSettingsDialog.getServerExternalPort()), networkSettingsDialog.getServerInternalIP(), Integer.parseInt(networkSettingsDialog.getServerInternalPort()), identity);	
			}

			@Override
			public void stopSharingSession() {
				remoteFileTransferControler.stopSharingSession();	
			}

			@Override
			public void connectToRemoteSession(PeerConnectionRequest connectionRequest) {
				remoteFileTransferControler.connectRemoteSession(networkSettingsDialog.getServerExternalIP(), Integer.parseInt(networkSettingsDialog.getServerExternalPort()), networkSettingsDialog.getServerInternalIP(), Integer.parseInt(networkSettingsDialog.getServerInternalPort()), connectionRequest);
			}

			@Override
			public void disconnectFromRemoteSession() {
				remoteFileTransferControler.disconnectFromSession();	
			}

			@Override
			public void expandSelectedFolder(String remoteFolderPath) {
				remoteFileTransferControler.expandSelectedFolder(remoteFolderPath);
			}	
		});
		
		//Networking instantiations
		networkingControler = new NetworkingControler();
		
		
		//Sets up the tab menu
		tabs = new JTabbedPane();
		tabs.addTab("Local file transfer", fileTransfer);
		tabs.addTab("Remote file transfer", remoteFileTransfer);
		tabs.addTab("File Synchronization", fileSynchronization);
		add(tabs, BorderLayout.CENTER);
		
		//Sets up the listener for file transfer section
		fileTransfer.setFileTransferListener(new FileTransferListener() {

			//Also disables the main window when the dialogs are active
			@Override
			public void getConnectionDialog() {
				connectionDialog.setVisible(true);
				setEnabled(false);	
			}

			@Override
			public void getSourceDialog() {
				sourceDialog.setVisible(true);
				setEnabled(false);		
			}

			//Close the source by index
			@Override
			public void closeSource(int index) {
				networkingControler.stopSourceFinal(index);	
			}

			@Override
			public void closeConnection(int index) {
				networkingControler.disconnectFinal(index);	
			}

			@Override
			public void retrieveFile(int index, String originalFilePath, String localFilePath, String localFileName) {
				networkingControler.retrieveFile(index, originalFilePath, localFilePath, localFileName);	
			}

			
		});
		
		//Sets sourceDB to the table model
	    fileTransfer.setSourceTableData(networkingControler.getSources());
	    
	    //Sets connectionsDB to the connections table model
	    fileTransfer.setConnectionsTableData(networkingControler.getConnections());
	    	
	    //Refresh the table on the basis of an controler generated event
	    networkingControler.setNetworkingControlerListener(new NetworkingControlerListener() {
			@Override
			public void actionHappened(NetworkControlerEvent event) {
			fileTransfer.refreshTable();	
			fileTransfer.refreshConnectionsTable();
			}

			@Override
			public void getReceivedFileSize(long fileSize) {
				fileTransfer.setReceivedFileSize(fileSize);
			}

			@Override
			public void getFileName(String fileName) {
				fileTransfer.setReceivedFileName(fileName);	
			}

			@Override
			public void getTransferPercentage(int percentage) {
				fileTransfer.setTransferPercentage(percentage);	
			}
		});
		
		//Sets up the listener for source dialog
		sourceDialog.setSourceDialogListener(new SourceDialogListener() {

			//sets this dialog as the only active window
			@Override
			public void enableMainframe() {
				setEnabled(true);
				Mainframe.this.toFront();
			}
            //starts the source
			@Override
			public void startSource(String IP, int port, String name, String selectedFilePath, String hostname) {
				networkingControler.startSource(IP, port, name, selectedFilePath, hostname);	
			}
			
		});
		
		//Sets up the listener for client connection
		connectionDialog.setConnectionDialogListener(new ConnectionDialogListener() {

			//sets this dialog as the only active window
			@Override
			public void enableMainframe() {
				setEnabled(true);
				Mainframe.this.toFront();
				
			}

			@Override
			public void connect(String IP, String hostname, int port) {
				networkingControler.connect(IP, hostname, port);	
			}

			@Override
			public void disconnect() {
				networkingControler.disconnect();		
			}
			
		});
			
	}
	
	private JMenuBar setMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		//File menu
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		
		exit.addActionListener((ActionEvent event)-> System.exit(0));
		
		//Settings menu
		JMenu settingsMenu = new JMenu("Settings");
		menuBar.add(settingsMenu);
		
		JMenuItem systemSettings = new JMenuItem("System settings");
		settingsMenu.add(systemSettings);
		systemSettings.addActionListener((ActionEvent event)-> systemSettingsPanel.setVisible(true));
		
		JMenuItem networkingSettings = new JMenuItem("Networking settings");
		settingsMenu.add(networkingSettings);
		networkingSettings.addActionListener((ActionEvent event)-> networkSettingsDialog.setVisible(true));
		
		JMenu about = new JMenu("About");
		menuBar.add(about);
		
		return menuBar;
		
	}

}
