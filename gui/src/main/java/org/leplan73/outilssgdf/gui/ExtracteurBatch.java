package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

import javax.swing.border.TitledBorder;

import org.apache.http.client.ClientProtocolException;
import org.leplan73.outilssgdf.intranet.ExtractionMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ExtracteurBatch extends JDialog implements LoggedDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextArea txtLog;
	private JFileChooser fcSortie;
	private JFileChooser fcBatch;
	
	private static Logger logger_ = LoggerFactory.getLogger(ExtracteurBatch.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ExtracteurBatch dialog = new ExtracteurBatch();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExtracteurBatch() {
		Appender.setLoggedDialog(this);
		
		setResizable(false);
		setTitle("ExtracteurBatch");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 566, 480);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{121, 0};
		gbl_contentPanel.rowHeights = new int[]{46, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Identifiant", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.WEST);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfIdentifiant = new JTextField();
					txfIdentifiant.setColumns(30);
					panel_1.add(txfIdentifiant);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Mot de passe", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfMotdepasse = new JPasswordField();
					txfMotdepasse.setColumns(10);
					panel_1.add(txfMotdepasse);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Batch", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblBatch = new JLabel("<rien>");
				panel.add(lblBatch, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcBatch = new JFileChooser();
						fcBatch.setDialogTitle("Fichier batch");
						fcBatch.setApproveButtonText("Go");
						fcBatch.setCurrentDirectory(new File("."));
						fcBatch.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcBatch.removeChoosableFileFilter(fcSortie.getFileFilter() );
						fcBatch.addChoosableFileFilter(new ExportFileFilter("txt"));
						int result = fcBatch.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {  
							File targetFile = fcBatch.getSelectedFile();
							lblBatch.setText(targetFile.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			{
				JCheckBox checkBox = new JCheckBox("Récursif");
				checkBox.setSelected(true);
				panel.add(checkBox);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel("<rien>");
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcSortie = new JFileChooser();
						fcSortie.setDialogTitle("Export Configuration");
						fcSortie.setApproveButtonText("Export");
						fcSortie.setCurrentDirectory(new File("."));
						fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter() );
						fcSortie.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcSortie.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {  
							File targetFile = fcSortie.getSelectedFile();
							lblSortie.setText(targetFile.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JProgressBar progressBar = new JProgressBar();
				progressBar.setStringPainted(true);
				panel.add(progressBar, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Go");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						export();
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 5;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			{
				txtLog = new JTextArea();
				txtLog.setEditable(false);
				panel.add(txtLog);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setEnabled(false);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected ExtractionMain connection_;
	private JCheckBox chkRecursif;
	private JLabel lblBatch;
	private JLabel lblSortie;
	
	private void login(ExtractionMain connection) throws ClientProtocolException, IOException
	{
		connection_ = connection;
		Logging.logger_.info("Connexion");
		
		connection_.init();
		if (connection_.login(txfIdentifiant.getText(),new String(txfMotdepasse.getPassword())) == false)
		{
			throw new IOException("erreur de connexion");
		}
	}
	
	public void logout() throws IOException
	{
		connection_.close();
	}
	
	private int extract(String key)
	{
		int value = -1;
		
		int i = key.lastIndexOf("(");
		int j = key.lastIndexOf(")");
		if (i > -1 && j > -1)
		{
			String v = key.substring(i+1, j-1);
			return Integer.valueOf(v);
		}
		return value;
	}
	
	private boolean check()
	{
		Logging.logger_.info("Vérification des paramètres");
		if (fcSortie==null)
		{
			logger_.error("Sortie non-sélectionnée");
			return false;
		}
		if (txfIdentifiant.getText().isEmpty())
		{
			logger_.error("Identifiant est vide");
			return false;
		}
		if (txfMotdepasse.getPassword().length == 0)
		{
			logger_.error("Mode de passe est vide");
			return false;
		}
		return true;
	}
	
	private void export()
	{
		txtLog.setText("");
		
		Instant now = Instant.now();
		
		boolean ret = check();
		if (ret)
		{
		}
		
		long d = Instant.now().getEpochSecond() - now.getEpochSecond();
		Logging.logger_.info("Terminé en "+d+" seconds");
	}
	
	@Override
	public void dispose()
	{
		Appender.setLoggedDialog(null);
		super.dispose();
	}
	
	@Override
	public void addLog(String message)
	{
		String texte = txtLog.getText();
		if (texte.length() > 0) texte+="\n";
		texte+=message;
		txtLog.setText(texte);
	}

	public JPasswordField getTxfMotdepasse() {
		return txfMotdepasse;
	}
	public JTextField getTxfIdentifiant() {
		return txfIdentifiant;
	}
	public JTextArea getTxtLog() {
		return txtLog;
	}
	public JLabel getLblBatch() {
		return lblBatch;
	}
	public JLabel getLblSortie() {
		return lblSortie;
	}
}
