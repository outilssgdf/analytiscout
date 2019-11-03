package org.leplan73.outilssgdf.gui.analyseur;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ParamEntree;
import org.leplan73.outilssgdf.ParamSortie;
import org.leplan73.outilssgdf.Progress;
import org.leplan73.outilssgdf.engine.EngineAnalyseur;
import org.leplan73.outilssgdf.gui.GuiProgress;
import org.leplan73.outilssgdf.gui.utils.Appender;
import org.leplan73.outilssgdf.gui.utils.BoutonOuvrir;
import org.leplan73.outilssgdf.gui.utils.Dialogue;
import org.leplan73.outilssgdf.gui.utils.ExportFileFilter;
import org.leplan73.outilssgdf.gui.utils.GuiCommand;
import org.leplan73.outilssgdf.gui.utils.LoggedDialog;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.outils.ResetableFileInputStream;
import org.slf4j.Logger;

abstract public class Analyseur extends Dialogue implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	protected File fBatch = new File("conf/batch_responsables.txt");
	private JFileChooser fcEntree = new JFileChooser();
	protected File fEntree = new File(Preferences.lit(Consts.REPERTOIRE_ENTREE, "données", false));
	protected File fModele = new File("conf/modele_responsables.xlsx");
	private JFileChooser fcSortieFichier = new JFileChooser();
	private JFileChooser fcSortieRepertoire = new JFileChooser();
	protected File fSortieFichier = new File("données/analyse.xlsx");
	protected File fSortieRepertoire = new File("données");
	protected String nomFichier_;
	private JCheckBox chcAge;
	private JLabel lblSortie;
	private JLabel lblEntree;
	private JButton btnGo;
	private JButton btnFichier;
	private BoutonOuvrir btnOuvrir;
	private JCheckBox chkGenererParGroupe;

	/**
	 * Create the dialog.
	 */
	public Analyseur(String titre, Logger logger, File pfSortieFichier, File pfSortieRepertoire, String nomFichier, File pfBatch, File pfModele) {
		super();
		this.logger_ = logger;
		this.fSortieFichier = pfSortieFichier;
		this.fSortieRepertoire = pfSortieRepertoire;
		this.fBatch = pfBatch;
		this.fModele = pfModele;
		this.nomFichier_ = nomFichier;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Appender.setLoggedDialog(this);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle(titre);
		double x = Preferences.litd(Consts.FENETRE_ANALYSEUR_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEUR_Y, 100);
		setBounds((int)x, (int)y, 778, 608);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 211, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
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
				JButton btnDossier = new JButton("Répertoire...");
				btnDossier.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcEntree.setDialogTitle("Répertoire de données");
						fcEntree.setApproveButtonText("Go");
						fcEntree.setCurrentDirectory(fEntree);
						fcEntree.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
			panel.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				chcAge = new JCheckBox("Gestion de l'âge");
				panel.add(chcAge, BorderLayout.NORTH);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel(fSortieFichier.getAbsolutePath());
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.EAST);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					chkGenererParGroupe = new JCheckBox("Générer un fichier par groupe");
					chkGenererParGroupe.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							btnFichier.setText(chkGenererParGroupe.isSelected() ? "Répertoire..." : "Fichier...");
							lblSortie.setText(chkGenererParGroupe.isSelected() ? fSortieRepertoire.getPath() : fSortieFichier.getPath());
						}
					});
					panel_1.add(chkGenererParGroupe, BorderLayout.NORTH);
				}
				{
					btnFichier = new JButton("Fichier...");
					panel_1.add(btnFichier, BorderLayout.CENTER);
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
						panel_1.add(btnOuvrir, BorderLayout.EAST);
					}
					btnFichier.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (chkGenererParGroupe.isSelected())
							{
								fcSortieRepertoire = new JFileChooser();
								fcSortieRepertoire.setDialogTitle("Répertoire de sortie");
								fcSortieRepertoire.setApproveButtonText("Export");
								fcSortieRepertoire.setCurrentDirectory(fSortieRepertoire);
								fcSortieRepertoire.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								int resultRepertoire = fcSortieRepertoire.showDialog(panel, "OK");
								if (resultRepertoire == JFileChooser.APPROVE_OPTION) {
									fSortieRepertoire = fcSortieRepertoire.getSelectedFile();
									String path = fSortieRepertoire.getPath();
									lblSortie.setText(path);
									btnOuvrir.maj();
								}
							}
							else
							{
								fcSortieFichier = new JFileChooser();
								fcSortieFichier.setDialogTitle("Fichier de sortie");
								fcSortieFichier.setApproveButtonText("Export");
								fcSortieFichier.setCurrentDirectory(fSortieFichier);
								fcSortieFichier.setFileSelectionMode(JFileChooser.FILES_ONLY);
								fcSortieFichier.removeChoosableFileFilter(fcSortieFichier.getFileFilter());
								fcSortieFichier.removeChoosableFileFilter(fcSortieFichier.getAcceptAllFileFilter());
								fcSortieFichier.addChoosableFileFilter(new ExportFileFilter("xlsx"));
								int result = fcSortieFichier.showDialog(panel, "OK");
								if (result == JFileChooser.APPROVE_OPTION) {
									fSortieFichier = ajouteExtensionFichier(fcSortieFichier, lblSortie, "xlsx");
									btnOuvrir.maj();
								}
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
		if (fBatch == null) {
			logger_.error("Batch non-sélectionnée");
			return false;
		}
		if (fEntree == null) {
			logger_.error("Entrée non-sélectionnée");
			return false;
		}
		if (fModele == null) {
			logger_.error("Modèle non-sélectionnée");
			return false;
		}
		if (fSortieFichier == null) {
			logger_.error("Sortie non-sélectionnée");
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
		
		EngineAnalyseur en = new EngineAnalyseur(progress, logger_);

		new Thread(() -> {
			initLog();
			boolean ret = check();
			if (ret) {
				logger_.info("Lancement");
				try {
					ParamEntree pentree = new ParamEntree(fEntree);
					ParamSortie psortie = chkGenererParGroupe.isSelected() ? new ParamSortie(fSortieRepertoire, true, nomFichier_) : new ParamSortie(fSortieFichier);
					en.go(pentree, new ResetableFileInputStream(new FileInputStream(fBatch)), new ResetableFileInputStream(new FileInputStream(fModele)), null, getChcAge().isSelected(), "tout_responsables", psortie, false, chkGenererParGroupe.isSelected());
					btnOuvrir.maj();
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
		Preferences.sauve(Consts.REPERTOIRE_ENTREE, this.fEntree.getPath(), false);
		Preferences.sauve(Consts.REPERTOIRE_SORTIE, chkGenererParGroupe.isSelected() ? this.fSortieRepertoire.getPath() : this.fSortieFichier.getAbsoluteFile().getParent(), false);
		super.dispose();
	}

	public JCheckBox getChcAge() {
		return chcAge;
	}

	public JLabel getLblSortie() {
		return lblSortie;
	}

	public JLabel getLblEntree() {
		return lblEntree;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
	protected JButton getBtnFichier() {
		return btnFichier;
	}
}
