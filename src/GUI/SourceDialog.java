package GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Listeners.SourceDialogListener;

public class SourceDialog extends JDialog implements ActionListener{
	
	private String selectedFilePath = null;
	
	private SourceDialogListener listener;
	
	private JLabel IP;
	private JLabel port;
	private JLabel physicalPath;
	private JLabel sourceName;
	private JLabel hostname;
	
	private JTextField sourceNameText;
	private JTextField hostnameText;
	private JTextField IPText;
	private JTextField portText;
	private JTextField physicalPathText;
	
	private JButton startSource;
	private JButton getLocalIP;
	private JButton setPhysicalSourcePath;
	
	private JFileChooser chooser;
	private JOptionPane errorPane;

	public SourceDialog(JFrame parent) {
		super(parent, "Set transfer source on current machine", false);
		setSize(400, 300);
		setLocationRelativeTo(parent);
		setResizable(false);
		
		setLayout(new BorderLayout());
		
		hostname = new JLabel("Hostname: ");
		sourceName = new JLabel("Name: ");
		IP = new JLabel("IP: ");
		port = new JLabel("Port: ");
		physicalPath = new JLabel("Source path: ");
		
		sourceNameText = new JTextField(11);
		IPText = new JTextField(11);
		IPText.setEditable(false);
		portText = new JTextField(11);
		physicalPathText = new JTextField(15);
		hostnameText = new JTextField(11);
		hostnameText.setEditable(false);
		
		startSource = new JButton("Start source");
		getLocalIP = new JButton("Get machine IP");
		setPhysicalSourcePath = new JButton("...");
		
		chooser = new JFileChooser();
		errorPane = new JOptionPane();
		
		startSource.addActionListener(this);
		getLocalIP.addActionListener(this);
		setPhysicalSourcePath.addActionListener(this);
		
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				listener.enableMainframe();
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		setControls();
			
}
	
	public void setSourceDialogListener(SourceDialogListener listener) {
		this.listener = listener;
	}
	
	public void setControls() {
		
		//Connection data area
		JPanel connectionDataPanel = new JPanel();
		connectionDataPanel.setLayout(new GridBagLayout());
		connectionDataPanel.setBorder(BorderFactory.createTitledBorder("Connection data"));
		add(connectionDataPanel, BorderLayout.PAGE_START);
		GridBagConstraints connectionConstraints = new GridBagConstraints();
		
		//Connection data rows
		connectionConstraints.gridx = 0;
		connectionConstraints.gridy = 0;
		connectionDataPanel.add(sourceName, connectionConstraints);
		
		connectionConstraints.gridx = 1;
		connectionConstraints.gridy = 0;
		connectionDataPanel.add(sourceNameText, connectionConstraints);
		
		connectionConstraints.gridx = 0;
		connectionConstraints.gridy = 1;
		connectionDataPanel.add(hostname, connectionConstraints);
		
		connectionConstraints.gridx = 1;
		connectionConstraints.gridy = 1;
		connectionDataPanel.add(hostnameText, connectionConstraints);
		
		connectionConstraints.gridx = 0;
		connectionConstraints.gridy = 2;
		connectionDataPanel.add(IP, connectionConstraints);
		
		connectionConstraints.gridx = 1;
		connectionConstraints.gridy = 2;
		connectionDataPanel.add(IPText, connectionConstraints);
		
		connectionConstraints.gridx = 0;
		connectionConstraints.gridy = 3;
		connectionDataPanel.add(port, connectionConstraints);
		
		connectionConstraints.gridx = 1;
		connectionConstraints.gridy = 3;
		connectionDataPanel.add(portText, connectionConstraints);
		
		connectionConstraints.gridx = 1;
		connectionConstraints.gridy = 4;
		connectionDataPanel.add(getLocalIP, connectionConstraints);
		
		//Physical source area
		JPanel physicalSource = new JPanel();
		physicalSource.setLayout(new GridBagLayout());
		physicalSource.setBorder(BorderFactory.createTitledBorder("Physical source path"));
		add(physicalSource, BorderLayout.CENTER);
		GridBagConstraints physicalSourceConstraints = new GridBagConstraints();
		
		//Physical source rows
		physicalSourceConstraints.gridx = 0;
		physicalSourceConstraints.gridy = 0;
		physicalSource.add(physicalPath, physicalSourceConstraints);
		
		physicalSourceConstraints.gridx = 1;
		physicalSourceConstraints.gridy = 0;
		physicalSource.add(physicalPathText, physicalSourceConstraints);
		
		physicalSourceConstraints.gridx = 2;
		physicalSourceConstraints.gridy = 0;
		physicalSource.add(setPhysicalSourcePath, physicalSourceConstraints);
		
		//Buttons area
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
		add(buttonsPanel, BorderLayout.PAGE_END);
		
		buttonsPanel.add(startSource);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton source = (JButton)event.getSource();
		
		if(source == getLocalIP) {
			
			try {
				String IP = InetAddress.getLocalHost().getHostAddress().toString();
				IPText.setText(IP);
				hostnameText.setText(InetAddress.getLocalHost().getCanonicalHostName());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}else if(source == startSource) {
			
			if(IPText.getText().equals("")) {
				errorPane.showMessageDialog(SourceDialog.this, "Please set up the source IP!");
				return;
			}else if(portText.getText().equals("")) {
				errorPane.showMessageDialog(SourceDialog.this, "Please enter a valid port for this source");
				return;
			}else if(sourceName.getText().equals("")) {
				errorPane.showMessageDialog(SourceDialog.this, "Please name your source!");
				return;
			}else if(physicalPathText.getText().equals("")) {
				errorPane.showMessageDialog(SourceDialog.this, "Please set up source path!");
				return;
			}
				
			listener.startSource(IPText.getText(), Integer.parseInt(portText.getText()), sourceNameText.getText(), selectedFilePath, hostnameText.getText());
				
		}else if(source == setPhysicalSourcePath) {
			
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			if(chooser.showOpenDialog(SourceDialog.this)==JFileChooser.APPROVE_OPTION) {
				System.out.println("Selected");
				this.selectedFilePath = chooser.getSelectedFile().getAbsolutePath();
				physicalPathText.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
		
	}
}
