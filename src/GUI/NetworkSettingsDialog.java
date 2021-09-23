package GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DataModels.NetworkSettingsPreferences;

public class NetworkSettingsDialog extends JDialog implements ActionListener{
	
	private JLabel serverPublicIP;
	private JLabel serverPublicPort;
	private JLabel serverInternalIP;
	private JLabel serverInternalPort;
	
	private JTextField serverPublicIPText;
	private JTextField serverPublicPortText;
	private JTextField serverInternalIPText;
	private JTextField serverInternalPortText;
	
	private JButton apply;
	private JButton close;
	
	private NetworkSettingsPreferences networkPreferences;

	public NetworkSettingsDialog(JFrame parent) {
		super(parent, "Networking settings", false);
		setSize(400, 400);
		setResizable(false);
		setLayout(new BorderLayout());
		
		serverPublicIP = new JLabel("Server public IP: ");
		serverPublicPort = new JLabel("Server public port: ");
		serverInternalIP = new JLabel("Server internal IP: ");
		serverInternalPort = new JLabel("Server internal port: ");
		
		serverPublicIPText = new JTextField(10);
		serverPublicPortText = new JTextField(10);
		serverInternalIPText = new JTextField(10);
		serverInternalPortText = new JTextField(10);
		
		networkPreferences = new NetworkSettingsPreferences();
		serverPublicIPText.setText(networkPreferences.getDefaultPublicIP());
		serverPublicPortText.setText(networkPreferences.getDefaultPublicPort());
		serverInternalIPText.setText(networkPreferences.getDefaultInternalIP());
		serverInternalPortText.setText(networkPreferences.getDefaultInternalPort());
		
		apply = new JButton("Apply");
		close = new JButton("Close");
		apply.addActionListener(this);
		close.addActionListener(this);
		
		setControls();
	}
	
	public String getServerExternalIP() {
		return serverPublicIPText.getText();
	}
	
	public String getServerExternalPort() {
		return serverPublicPortText.getText();
	}
	
	public String getServerInternalIP() {
		return serverInternalIPText.getText();
	}
	
	public String getServerInternalPort() {
		return serverInternalPortText.getText();
	}
	
	private void setControls() {
		JPanel serverDataPanel = new JPanel();
		serverDataPanel.setBorder(BorderFactory.createTitledBorder("Server connection data"));
		add(serverDataPanel, BorderLayout.PAGE_START);
		serverDataPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.gridx = 0;
		gc.gridy = 0;
		serverDataPanel.add(serverPublicIP, gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		serverDataPanel.add(serverPublicIPText, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		serverDataPanel.add(serverPublicPort, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;
		serverDataPanel.add(serverPublicPortText, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		serverDataPanel.add(serverInternalIP, gc);
		
		gc.gridx = 1;
		gc.gridy = 2;
		serverDataPanel.add(serverInternalIPText, gc);
		
		gc.gridx = 0;
		gc.gridy = 3;
		serverDataPanel.add(serverInternalPort, gc);
		
		gc.gridx = 1;
		gc.gridy = 3;
		serverDataPanel.add(serverInternalPortText, gc);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		buttonPanel.setLayout(new FlowLayout());
		add(buttonPanel, BorderLayout.PAGE_END);
		
		buttonPanel.add(apply);
		buttonPanel.add(close);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton source = (JButton)event.getSource();
		
		if(source == apply) {
			networkPreferences.setExternalConnectionData(serverPublicIPText.getText(), serverPublicPortText.getText());
		}else if(source == close) {
			NetworkSettingsDialog.this.dispose();
		}
		
	}

}
