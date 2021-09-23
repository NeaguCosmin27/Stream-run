package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;

import DataModels.MachineTreeModel;
import Listeners.CurrentMachineExplorerListener;
import Listeners.RemoteFileTransferListener;
import Listeners.RemoteMachineExplorerListener;
import Metadata.PeerConnectionRequest;
import Metadata.PeerIdentity;

public class RemoteFileTransfer extends JPanel implements ActionListener{
	
	private JLabel sessionID;
	private JLabel sessionPassword;
	private JLabel peerSessionID;
	private JLabel peerSessionPassword;
	private JLabel serverConnectionStatus;
	private JLabel serverConnectionStatusText;
	private JLabel saveTo;
	
	private JTextField sessionIDText;
	private JTextField sessionPasswordText;
	private JTextField peerSessionIDText;
	private JTextField peerSessionPasswordText;
	
	private JButton setRemotePC;
	private JButton connectToRemotePC;
	private JButton closeSharingSession;
	private JButton closeRemoteSession;
	
	private JTree machineTree;
	
	private JToolBar toolbar;
	
	private JOptionPane optionPane;
	
	private CurrentMachineExplorer currentMachineExplorer;
	private RemoteMachineExplorer remoteMachineExplorer;
	
	private RemoteFileTransferListener listener;
	
	public RemoteFileTransfer() {
		setLayout(new BorderLayout());
		
		
		sessionID = new JLabel("Session ID: ");
		sessionPassword = new JLabel("Session password: ");
		peerSessionID = new JLabel("Remote peer ID: ");
	    peerSessionPassword = new JLabel("Remote peer password: ");
	    serverConnectionStatus = new JLabel("Connection status: ");
	    serverConnectionStatusText = new JLabel("server disconnected");
	    saveTo = new JLabel("Save to: ");
		
		sessionIDText = new JTextField(10);
		sessionPasswordText = new JTextField(10);
		peerSessionIDText = new JTextField(10);
		peerSessionPasswordText = new JTextField(10);
		
		toolbar = new JToolBar();
		
		optionPane = new JOptionPane();
		
		setRemotePC = new JButton("Start session");
		connectToRemotePC = new JButton("Connect to session");
		closeSharingSession = new JButton("Close session");
		closeRemoteSession = new JButton("Close session connection");
		setRemotePC.addActionListener(this);
		connectToRemotePC.addActionListener(this);
		closeSharingSession.addActionListener(this);
		closeRemoteSession.addActionListener(this);
		
		machineTree = new JTree();
		
		toolbar.add(setRemotePC);
		toolbar.addSeparator();
		toolbar.add(closeSharingSession);
		toolbar.addSeparator();
		toolbar.add(connectToRemotePC);
		toolbar.addSeparator();
		toolbar.add(closeRemoteSession);
		
		currentMachineExplorer = new CurrentMachineExplorer();
		remoteMachineExplorer = new RemoteMachineExplorer();
		
		//Listens for actions that are happening within the current machine file explorer
		currentMachineExplorer.setCurrentMachineExplorerListener(new CurrentMachineExplorerListener() {

			@Override
			public void getSelectedPath(String selectedPath) {
				currentMachineExplorer.setFileHerarchicalpath(selectedPath);
			}

			@Override
			public void getHierarchicalPath(String hierarchicalPath) {
				
                FileSystemView view = FileSystemView.getFileSystemView();
				
                File parentFile = new File(hierarchicalPath);
       
                System.out.println(parentFile.getName());
                
                if(parentFile == null) {
                	System.out.println("null");
                }else if(view.getSystemTypeDescription(parentFile).equals("Local Disk") || view.getSystemTypeDescription(parentFile).equals("CD Drive")){
                	currentMachineExplorer.loadTableData();
                	currentMachineExplorer.setFileHerarchicalpath("");
                }else if(parentFile != null && !parentFile.getName().equals("")){
                	currentMachineExplorer.exploreFolder(parentFile.getParent());
                }
			}
			
		});
		
		//Listens for actions that are happening within the remote machine file explorer
		remoteMachineExplorer.setRemoteMachineExplorerListener(new RemoteMachineExplorerListener() {

			@Override
			public void getSelectedPath(String selectedPath) {
				remoteMachineExplorer.setFileHerarchicalpath(selectedPath);	
			}

			@Override
			public void getHierarchicalPath(String hierarchicalPath) {
				
                FileSystemView view = FileSystemView.getFileSystemView();
				
                File parentFile = new File(hierarchicalPath);
       
                System.out.println(parentFile.getName());
                
                if(parentFile == null) {
                	System.out.println("null");
                }else if(view.getSystemTypeDescription(parentFile).equals("Local Disk") || view.getSystemTypeDescription(parentFile).equals("CD Drive")){
                	remoteMachineExplorer.loadTableData();
                	remoteMachineExplorer.setFileHerarchicalpath("");
                }else if(parentFile != null && !parentFile.getName().equals("")){
                	remoteMachineExplorer.exploreFolder(parentFile.getParent());
                }
			}

			//User to update the explorer remotely
			@Override
			public void expandSelectedFolder(String path) {
				listener.expandSelectedFolder(path);		
			}
			
		});
		
		
		setControls();
	}
	
	
	public void setControls() {
		
		// Machines display
		JPanel machinePanel = new JPanel();
		machinePanel.setLayout(new BorderLayout());
		machinePanel.setBorder(BorderFactory.createEtchedBorder());
		add(machinePanel, BorderLayout.CENTER);
		
		//Current machine file explorer
		JPanel currentMachinePanel = new JPanel();
		currentMachinePanel.setLayout(new BorderLayout());
		currentMachinePanel.setBorder(BorderFactory.createTitledBorder("Current machine"));
		machinePanel.add(currentMachinePanel, BorderLayout.WEST);
		currentMachinePanel.add(currentMachineExplorer, BorderLayout.CENTER);
		Dimension dimensionCurrent = currentMachinePanel.getPreferredSize();
		dimensionCurrent.width = 380;
		currentMachinePanel.setPreferredSize(dimensionCurrent);
		
		//Remote machine file explorer
		JPanel remoteMachinePanel = new JPanel();
		remoteMachinePanel.setLayout(new BorderLayout());
		remoteMachinePanel.setBorder(BorderFactory.createTitledBorder("Remote machine"));
		machinePanel.add(remoteMachinePanel, BorderLayout.EAST);
		remoteMachinePanel.add(remoteMachineExplorer, BorderLayout.CENTER);
		Dimension dimensionRemote = remoteMachinePanel.getPreferredSize();
		dimensionRemote.width = 380;
		remoteMachinePanel.setPreferredSize(dimensionRemote);
		
		//For holding control panels
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(BorderFactory.createEtchedBorder());
		add(controlPanel, BorderLayout.NORTH);
		controlPanel.add(toolbar, BorderLayout.PAGE_START);
		
		//Current session ID data
		JPanel sessionData = new JPanel();
		sessionData.setLayout(new GridBagLayout());
		sessionData.setBorder(BorderFactory.createTitledBorder("Session credentials"));
		controlPanel.add(sessionData, BorderLayout.WEST);
		GridBagConstraints gcSession = new GridBagConstraints();
		
		gcSession.gridx = 0;
		gcSession.gridy = 0;
		sessionData.add(sessionID, gcSession);
		
		gcSession.gridx = 1;
		gcSession.gridy = 0;
		sessionData.add(sessionIDText, gcSession);
		
		gcSession.gridx = 0;
		gcSession.gridy = 1;
		sessionData.add(sessionPassword, gcSession);
		
		gcSession.gridx = 1;
		gcSession.gridy = 1;
		sessionData.add(sessionPasswordText, gcSession);
		
		//For peer ID data
		JPanel peerSessionData = new JPanel();
		peerSessionData.setLayout(new GridBagLayout());
		peerSessionData.setBorder(BorderFactory.createTitledBorder("Remote peer session"));
		controlPanel.add(peerSessionData, BorderLayout.EAST);
		GridBagConstraints gcPeerSession = new GridBagConstraints();
		
		gcPeerSession.gridx = 0;
		gcPeerSession.gridy = 0;
		peerSessionData.add(peerSessionID, gcPeerSession);
		
		gcPeerSession.gridx = 1;
		gcPeerSession.gridy = 0;
		peerSessionData.add(peerSessionIDText, gcPeerSession);
		
		gcPeerSession.gridx = 0;
		gcPeerSession.gridy = 1;
		peerSessionData.add(peerSessionPassword, gcPeerSession);
		
		gcPeerSession.gridx = 1;
		gcPeerSession.gridy = 1;
		peerSessionData.add(peerSessionPasswordText, gcPeerSession);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new GridBagLayout());
		statusPanel.setBorder(BorderFactory.createEtchedBorder());
		controlPanel.add(statusPanel, BorderLayout.CENTER);
		GridBagConstraints gcStatus = new GridBagConstraints();
		
		gcStatus.anchor = GridBagConstraints.FIRST_LINE_START;
		gcStatus.gridx = 0;
		gcStatus.gridy = 0;
		statusPanel.add(serverConnectionStatus, gcStatus);
		
		gcStatus.gridx = 1;
		gcStatus.gridy = 0;
		statusPanel.add(serverConnectionStatusText, gcStatus);
		
		JPanel fileRoutingPanel = new JPanel();
		fileRoutingPanel.setLayout(new GridBagLayout());
		fileRoutingPanel.setBorder(BorderFactory.createTitledBorder("File routing"));
		controlPanel.add(fileRoutingPanel, BorderLayout.SOUTH);
		GridBagConstraints gcFile = new GridBagConstraints();
		
		gcFile.gridx = 0;
		gcFile.gridy = 0;
		fileRoutingPanel.add(saveTo, gcFile);
		
		
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		JButton source = (JButton)event.getSource();
		
		//Check if the ID and password are of the right lenght and after that connects to server
		if(source == setRemotePC) {
			if(sessionID.getText().equals("") || sessionPassword.getText().equals("")) {
				optionPane.showMessageDialog(RemoteFileTransfer.this, "You must enter a ID and password of at least 8 characters!");
			}else if(sessionID.getText().length()<8 || sessionPasswordText.getText().length()<8) {
				optionPane.showMessageDialog(RemoteFileTransfer.this, "The session ID or password must have at least 8 characters!");
			}else {
				PeerIdentity identity = new PeerIdentity();
				identity.setUsername(sessionIDText.getText());
				identity.setPasswod(sessionPasswordText.getText());
				listener.startSharingSession(identity);
			}
			
		}else if(source == connectToRemotePC) {
			
			PeerConnectionRequest connectionRequest = new PeerConnectionRequest();
			connectionRequest.setSessionID(peerSessionIDText.getText());
			connectionRequest.setSessionPassword(peerSessionPasswordText.getText());
			listener.connectToRemoteSession(connectionRequest);
			
		}else if(source == closeSharingSession) {
			if(setRemotePC.isEnabled()) {
				optionPane.showMessageDialog(RemoteFileTransfer.this, "No active session!");
			}else if(!setRemotePC.isEnabled()) {
				listener.stopSharingSession();
			}
			
		}else if(source == closeRemoteSession) {
			if(connectToRemotePC.isEnabled()) {
				optionPane.showMessageDialog(RemoteFileTransfer.this, "Not connected to any peer!");
			}else if(!connectToRemotePC.isEnabled()) {
				listener.disconnectFromRemoteSession();
			}
		}
		
	}
	
	public void setRemoteFileTransferListener(RemoteFileTransferListener listener) {
		this.listener = listener;
	}
	
	public void setServerConnectionStatus(String status) {
		serverConnectionStatusText.setText(status);
	}
	
	//Enable or disable the start session button
	public void lockUnlockSessionButton(boolean isSetup) {
		if(isSetup==true) {
			setRemotePC.setEnabled(false);
		}else if(isSetup==false) {
			setRemotePC.setEnabled(true);
		}
	}

}
