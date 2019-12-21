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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.engine.EngineExtractionRegistreDePresence;
import org.leplan73.analytiscout.gui.GuiProgress;
import org.leplan73.analytiscout.gui.utils.Appender;
import org.leplan73.analytiscout.gui.utils.BoutonOuvrir;
import org.leplan73.analytiscout.gui.utils.Dialogue;
import org.leplan73.analytiscout.gui.utils.ExportFileFilter;
import org.leplan73.analytiscout.gui.utils.GuiCommand;
import org.leplan73.analytiscout.gui.utils.Logging;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.slf4j.LoggerFactory;

public class ExtractionRegistreDePresence extends Dialogue implements GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfAnnee;
	private JTextField txfCodeStructure;
	private JLabel lblSortie;
	private JFileChooser fcSortie;
	private File fSortie = new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"registrepresence.csv");
	private JCheckBox chkRecursif;
	private BoutonOuvrir btnOuvrir;

	/**
	 * Create the dialog.
	 */
	public ExtractionRegistreDePresence() {
		logger_ = LoggerFactory.getLogger(ExtractionRegistreDePresence.class);
		
		setResizable(false);
		setTitle("Extraction registre de présence");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Appender.setLoggedDialog(this);

		double x = Preferences.litd(Consts.FENETRE_ANALYSEUR_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEUR_Y, 100);
		setBounds((int)x, (int)y, 800, 553);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{60, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 191};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel, BorderLayout.EAST);
				{
					JButton okButton = new JButton("Go");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							go();
						}
					});
					panel.add(okButton);
					okButton.setActionCommand("OK");
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Fermer");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					panel.add(cancelButton);
					cancelButton.setActionCommand("Cancel");
				}
			}
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel, BorderLayout.WEST);
				{
					JButton button = new JButton("Aide");
					button.setEnabled(false);
					panel.add(button);
				}
			}
		}
		
		{
			{
				JPanel panelannee = new JPanel();
				panelannee.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Ann\u00E9e (au 1er septembre)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				GridBagConstraints gbc_panelannee = new GridBagConstraints();
				gbc_panelannee.insets = new Insets(0, 0, 5, 0);
				gbc_panelannee.anchor = GridBagConstraints.NORTH;
				gbc_panelannee.fill = GridBagConstraints.HORIZONTAL;
				gbc_panelannee.gridx = 0;
				gbc_panelannee.gridy = 1;
				contentPanel.add(panelannee, gbc_panelannee);
				panelannee.setLayout(new BorderLayout(0, 0));
				{
					txfAnnee = new JTextField();
					txfAnnee.setColumns(30);
					txfAnnee.setText("2019");
					panelannee.add(txfAnnee, BorderLayout.NORTH);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Code structure", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				txfCodeStructure = new JTextField();
				txfCodeStructure.setColumns(30);
				txfCodeStructure.setText(Preferences.lit(Consts.INTRANET_STRUCTURE, "", true));
				panel.add(txfCodeStructure, BorderLayout.CENTER);
			}
			{
				chkRecursif = new JCheckBox("Ajouter les structures enfants");
				chkRecursif.setSelected(true);
				panel.add(chkRecursif, BorderLayout.EAST);
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
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel(fSortie.getAbsolutePath());
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.EAST);
				{
					JButton btnFichier = new JButton("Fichier...");
					panel_1.add(btnFichier);
					{
						btnOuvrir = new BoutonOuvrir("Ouvrir...", lblSortie);
						btnOuvrir.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								try {
									btnOuvrir.ouvrir();
								} catch (Exception ex) {
									logger_.error(Logging.dumpStack(null, ex));
								}
							}
						});
						panel_1.add(btnOuvrir);
					}
					btnFichier.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							fcSortie = new JFileChooser();
							fcSortie.setDialogTitle("Fichier de sortie");
							fcSortie.setApproveButtonText("Export");
							fcSortie.setCurrentDirectory(fSortie.getParentFile());
							fcSortie.setSelectedFile(fSortie);
							fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
							fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter());
							fcSortie.removeChoosableFileFilter(fcSortie.getAcceptAllFileFilter());
							fcSortie.addChoosableFileFilter(new ExportFileFilter("csv"));
							int result = fcSortie.showDialog(panel, "OK");
							if (result == JFileChooser.APPROVE_OPTION) {
								fSortie = ajouteExtensionFichier(fcSortie, lblSortie, "csv");
								btnOuvrir.maj();
							}
						}
					});
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
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
	}

	@Override
	public void dispose() {
		Appender.setLoggedDialog(null);
		Preferences.sauved(Consts.FENETRE_ANALYSEURENLIGNE_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_ANALYSEURENLIGNE_Y, this.getLocation().getY());
		Preferences.sauve(Consts.REPERTOIRE_SORTIE, this.fSortie.getParent(), false);
		super.dispose();
	}

	@Override
	public void go() {
		ProgressMonitor guiprogress = new ProgressMonitor(this, "ExtractionRegistreDePresence", "", 0, 100);
		
		Progress progress = new GuiProgress(guiprogress, this.getTitle());
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);
		
		new Thread(() -> {
			initLog();
			boolean ret = check(txfCodeStructure);
			if (ret) {
				try {
					EngineExtractionRegistreDePresence en = new EngineExtractionRegistreDePresence(progress, logger_);
					int structures[] = construitStructures();
					en.go(identifiant_, motdepasse_, fSortie, structures, chkRecursif.isSelected(), Integer.parseInt(txfAnnee.getText()));
					btnOuvrir.maj();
				} catch (Exception e) {
					logger_.error(Logging.dumpStack(null, e));
				}
			}
		}).start();
	}
	
	public boolean check(JTextField txfStructure) {
		if (check() == false) return false;
		structure_ = txfCodeStructure.getText();
		return checkIntranet();
	}

	@Override
	public boolean check() {
		logger_.info("Vérification des paramètres");
		if (fSortie == null) {
			logger_.error("Le répertoire de sortie est non-sélectionnée");
			return false;
		}
		if (txfAnnee.getText().isEmpty()) {
			logger_.error("L'année est vide");
			return false;
		}
		try
		{
			Integer.parseInt(txfAnnee.getText());
		}
		catch (NumberFormatException e)
		{
			logger_.error("Erreur lors de la validation de l'année",e);
			return false;
		}
		return true;
	}
	protected JCheckBox getChkRecursif() {
		return chkRecursif;
	}
	public BoutonOuvrir getBtnOuvrir() {
		return btnOuvrir;
	}
}
