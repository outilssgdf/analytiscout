package org.leplan73.outilssgdf.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.leplan73.outilssgdf.gui.utils.VersionCheck;

public class OutilsSGDF extends JFrame {
	public OutilsSGDF() {
	}
	protected JPanel contentPane;
	
	public void go()
	{
		setVisible(true);
	}
	
	protected void updateVersion(JLabel lblVersionStatus)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run() {
				VersionCheck.check(lblVersionStatus);
	            }
		});
		t.setDaemon(true);
		t.start();
	}

}
