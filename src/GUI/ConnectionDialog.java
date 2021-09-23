package GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Listeners.ConnectionDialogListener;

public class ConnectionDialog extends JDialog implements ActionListener, ItemListener{
	
	private JLabel sourceHostname;
	private JLabel sourceIP;
	private JLabel sourcePort;
	private JLabel connectionStatus;
	private JLabel status;
	
	private JTextField sourceHostnameText;
	private JTextField sourceIPText;
	private JTextField sourcePortText;
	
	private JButton connect;
	private JButton disconnect;
	
	private JRadioButton hostnameOption;
	private JRadioButton IPOption;
	
	private ButtonGroup rButtonGroup;
	private JOptionPane optionPane;
	
	private ConnectionDialogListener listener;
	

	public ConnectionDialog(JFrame parent) {
		super(parent, "Connect to transfer source", false);
		setSize(400, 200);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());
		setResizable(false);
		
		sourceHostname = new JLabel("Hostname: ");
		sourceIP = new JLabel("Source IP: ");
		sourcePort = new JLabel("Source port: ");
		connectionStatus = new JLabel("Connection status: ");
		status = new JLabel("Not connected");
		
		sourceHostnameText = new JTextField(10);
		sourceIPText = new JTextField(10);
		sourceIPText.setEnabled(false);
		sourcePortText = new JTextField(10);
		
		connect = new JButton("Connect");
		disconnect = new JButton("Disconnect");
		
		hostnameOption = new JRadioButton();
		hostnameOption.setSelected(true);
		IPOption = new JRadioButton();
		
		rButtonGroup = new ButtonGroup();
		rButtonGroup.add(hostnameOption);
		rButtonGroup.add(IPOption);
		
		optionPane = new JOptionPane();
		
		setControls();
		
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				listener.enableMainframe();
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		hostnameOption.addItemListener(this);
		IPOption.addItemListener(this);

	}
	
	private void setControls() {
		
		//Connection data area
		JPanel connectionDataPanel = new JPanel();
		add(connectionDataPanel, BorderLayout.PAGE_START);
		connectionDataPanel.setBorder(BorderFactory.createTitledBorder("Connection data"));
		connectionDataPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints connectionDataConstraints = new GridBagConstraints();
		
		connectionDataConstraints.gridx = 0;
		connectionDataConstraints.gridy = 0;
		connectionDataPanel.add(sourceHostname, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 1;
		connectionDataConstraints.gridy = 0;
		connectionDataPanel.add(sourceHostnameText, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 2;
		connectionDataConstraints.gridy = 0;
		connectionDataPanel.add(hostnameOption, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 0;
		connectionDataConstraints.gridy = 1;
		connectionDataPanel.add(sourceIP, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 1;
		connectionDataConstraints.gridy = 1;
		connectionDataPanel.add(sourceIPText, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 2;
		connectionDataConstraints.gridy = 1;
		connectionDataPanel.add(IPOption, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 0;
		connectionDataConstraints.gridy = 2;
		connectionDataPanel.add(sourcePort, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 1;
		connectionDataConstraints.gridy = 2;
		connectionDataPanel.add(sourcePortText, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 0;
		connectionDataConstraints.gridy = 3;
		connectionDataPanel.add(connectionStatus, connectionDataConstraints);
		
		connectionDataConstraints.gridx = 1;
		connectionDataConstraints.gridy = 3;
		connectionDataPanel.add(status, connectionDataConstraints);
		
		//Buttons panel
		JPanel buttonsPanel = new JPanel();
		add(buttonsPanel, BorderLayout.PAGE_END);
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
		
		buttonsPanel.add(connect);
		buttonsPanel.add(disconnect);
		
	}
	
	public void setConnectionDialogListener(ConnectionDialogListener listener) {
		this.listener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton source = (JButton)event.getSource();
		
		if(source == connect) {
			if(sourceIPText.isEnabled()) {
				if(sourceIPText.getText().equals("")) {
					optionPane.showMessageDialog(ConnectionDialog.this, "Please enter the coresponding IP address!");
					return;
				}else if(sourcePortText.getText().equals("")) {
					optionPane.showMessageDialog(ConnectionDialog.this, "Please enter the coresponding port number!");
					return;
				}
				listener.connect(sourceIPText.getText(), sourceHostnameText.getText(), Integer.parseInt(sourcePortText.getText()));
			}else if(!sourceIPText.isEnabled()) {
				if(sourceHostnameText.getText().equals("")) {
					optionPane.showMessageDialog(ConnectionDialog.this, "Please enter the coresponding source Hostname!");
					return;
				}else if(sourcePortText.getText().equals("")) {
					optionPane.showMessageDialog(ConnectionDialog.this, "Please enter the coresponding port number!");
					return;
				}
				
				try {
					String ip = InetAddress.getByName(sourceHostnameText.getText()).getHostAddress().toString();
					listener.connect(ip, sourceHostnameText.getText(), Integer.parseInt(sourcePortText.getText()));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if(source == disconnect) {
			listener.disconnect();
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		int state = event.getStateChange();
		
		JRadioButton source = (JRadioButton)event.getSource();
		
		if(source == hostnameOption) {
			sourceHostnameText.setEnabled(true);
			sourceIPText.setEnabled(false);
		}else if(source == IPOption) {
			sourceIPText.setEnabled(true);
			sourceHostnameText.setEnabled(false);
		}
		
	}

}
