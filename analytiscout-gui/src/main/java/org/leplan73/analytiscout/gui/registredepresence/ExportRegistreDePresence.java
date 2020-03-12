package org.leplan73.analytiscout.gui.registredepresence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Params;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.engine.EngineExportRegistreDePresence;
import org.leplan73.analytiscout.gui.GuiProgress;
import org.leplan73.analytiscout.gui.utils.Appender;
import org.leplan73.analytiscout.gui.utils.Dialogue;
import org.leplan73.analytiscout.gui.utils.ExportFileFilter;
import org.leplan73.analytiscout.gui.utils.GuiCommand;
import org.leplan73.analytiscout.gui.utils.LoggedDialog;
import org.leplan73.analytiscout.gui.utils.Logging;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ExportRegistreDePresence extends Dialogue implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JFileChooser fcEntree = new JFileChooser();
	private File fEntree = new File(Preferences.lit(Consts.REPERTOIRE_ENTREE, "données", false),"registrepresence.csv");
	private JLabel lblEntree;
	private JButton btnGo;
	private JTextField txfConnexion;
	private JTextField txfUtilisateur;
	private JPasswordField pwdMotdePasse;
	private JTextField txfDb;

	/**
	 * Create the dialog.
	 */
	public ExportRegistreDePresence() {
		super();
		logger_ = LoggerFactory.getLogger(ExportRegistreDePresence.class);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Export registre de présence");

		Appender.setLoggedDialog(this);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		double x = Preferences.litd(Consts.FENETRE_ANALYSEUR_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEUR_Y, 100);
		setBounds((int)x, (int)y, 778, 608);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 399, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 113, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Entr\u00E9e", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblEntree = new JLabel(fEntree.getAbsolutePath());
				panel.add(lblEntree, BorderLayout.WEST);
			}
			{
				JButton btnDossier = new JButton("Fichier...");
				btnDossier.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcEntree.setDialogTitle("Répertoire de données");
						fcEntree.setApproveButtonText("Go");
						fcEntree.setSelectedFile(fEntree);
						fcEntree.setCurrentDirectory(fEntree.getParentFile());
						fcEntree.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcEntree.addChoosableFileFilter(new ExportFileFilter("csv"));
						int result = fcEntree.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fEntree = fcEntree.getSelectedFile();
							lblEntree.setText(fEntree.getPath());
						}
					}
				});
				panel.add(btnDossier, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Serveur InfluxDB", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			{
			}
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{750, 0};
			gbl_panel.rowHeights = new int[]{52, 52, 0};
			gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JPanel panel_1 = new JPanel();
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.NORTH;
				gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new TitledBorder(null, "Connexion", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					panel_1.add(panel_2, BorderLayout.WEST);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						txfConnexion = new JTextField();
						panel_2.add(txfConnexion);
						txfConnexion.setColumns(30);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new TitledBorder(null, "Utilisateur", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					panel_1.add(panel_2, BorderLayout.CENTER);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						txfUtilisateur = new JTextField();
						panel_2.add(txfUtilisateur);
						txfUtilisateur.setColumns(20);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new TitledBorder(null, "Mot de passe", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					panel_1.add(panel_2, BorderLayout.EAST);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						pwdMotdePasse = new JPasswordField();
						pwdMotdePasse.setColumns(20);
						panel_2.add(pwdMotdePasse);
					}
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Base de donn\u00E9es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.NORTH;
				gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					txfDb = new JTextField();
					panel_1.add(txfDb);
					txfDb.setColumns(30);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane);
				{
					txtLog = new JTextArea();
					scrollPane.setViewportView(txtLog);
					txtLog.setEditable(false);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));
			{
				{
					JPanel panel = new JPanel();
					buttonPane.add(panel, BorderLayout.EAST);
					{
						btnGo = new JButton("Go");
						panel.add(btnGo);
						JButton btnFermer = new JButton("Fermer");
						panel.add(btnFermer);
						{
							JPanel panel_1 = new JPanel();
							buttonPane.add(panel_1, BorderLayout.WEST);
							{
								JButton btnAide = new JButton("Aide");
								btnAide.setEnabled(false);
								btnAide.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
									}
								});
								panel_1.add(btnAide);
							}
						}
						btnFermer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								dispose();
							}
						});
						btnGo.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								go();
							}
						});
					}
				}
			}
		}
	}

	@Override
	public boolean check() {
		logger_.info("Vérification des paramètres");
		if (fEntree == null) {
			logger_.error("Entrée non-sélectionnée");
			return false;
		}
		if (txfConnexion.getText().isEmpty()) {
			logger_.error("La connexion est vide");
			return false;
		}
		if (txfDb.getText().isEmpty()) {
			logger_.error("La base de données est vide");
			return false;
		}
		return true;
	}

	@Override
	public void go() {
		ProgressMonitor guiprogress = new ProgressMonitor(this, "Analyseur", "", 0, 100);
		
		Progress progress = new GuiProgress(guiprogress, this.getTitle());
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);
		
		EngineExportRegistreDePresence en = new EngineExportRegistreDePresence(progress, logger_);

		new Thread(() -> {
			progress.start();
			initLog();
			boolean ret = check();
			progress.setProgress(20, null);
			if (ret) {
				addLog("Lancement");
				try {
					en.go(fEntree, txfConnexion.getText(), txfUtilisateur.getText(), new String(pwdMotdePasse.getPassword()), txfDb.getText(), Params.getb(Consts.PARAMS_ANONYMISER, false));
				} catch (Exception e) {
					logger_.error(Logging.dumpStack(null, e));
				}
			}
		}).start();
	}

	@Override
	public void dispose() {
		Appender.setLoggedDialog(null);
		Preferences.sauved(Consts.FENETRE_ANALYSEUR_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_ANALYSEUR_Y, this.getLocation().getY());
		super.dispose();
	}

	public JLabel getLblEntree() {
		return lblEntree;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
}
