package org.leplan73.analytiscout.gui.dialogues;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.leplan73.analytiscout.gui.utils.Images;
import org.leplan73.analytiscout.gui.utils.Version;
import org.leplan73.analytiscout.intranet.ExtractionIntranet;

import com.jcabi.manifests.Manifests;

public class Infos extends JDialog {

	/**
	 * Create the dialog.
	 */
	public Infos() {
		setTitle("Informations");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 528, 363);
		setIconImage(Images.getIcon());
		setFont(new Font("Dialog", Font.PLAIN, 12));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{54, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(10, 10, 5, 10);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		
		JTextArea textAreaVersion = new JTextArea();
		scrollPane_1.setViewportView(textAreaVersion);
		textAreaVersion.setEditable(false);
		
		try {
			Version v = Version.parse(Manifests.read("version"));
			textAreaVersion.setText("Version " + v.toString() + ExtractionIntranet.getIntranetServeur());
		} catch (java.lang.IllegalArgumentException e) {
		}
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 10, 5, 10);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		JTextArea textAreaInfos = new JTextArea();
		textAreaInfos.setLineWrap(true);
		textAreaInfos.setEditable(false);
		scrollPane.setViewportView(textAreaInfos);
		
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		StringBuffer sb = new StringBuffer();
		sb.append("OS : ").append(os.getName() + " - " + os.getVersion()).append("\n");
		sb.append("OS arch : ").append(os.getArch()).append("\n").append("\n");
		
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		sb.append("Java : ").append(rt.getVmVendor() + " " + rt.getVmName() + " " + rt.getVmVersion()).append("\n");
		textAreaInfos.setText(sb.toString());
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.SOUTHEAST;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		getContentPane().add(panel, gbc_panel);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(btnOK);
		setLocationRelativeTo(null);

	}

}
