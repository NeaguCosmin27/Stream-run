package GUI;

import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class SystemSettingsPanel extends JDialog{

	public SystemSettingsPanel(JFrame parent) {
		super(parent, "Settings", false);
		setSize(400, 400);
		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	

}
