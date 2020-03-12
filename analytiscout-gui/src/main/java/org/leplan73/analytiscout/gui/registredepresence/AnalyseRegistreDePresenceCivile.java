package org.leplan73.analytiscout.gui.registredepresence;

import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Params;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.engine.EngineAnalyseurRegistreDePresence;
import org.leplan73.analytiscout.gui.GuiProgress;
import org.leplan73.analytiscout.gui.utils.Appender;
import org.leplan73.analytiscout.gui.utils.BoutonOuvrir;
import org.leplan73.analytiscout.gui.utils.Dialogue;
import org.leplan73.analytiscout.gui.utils.ExportFileFilter;
import org.leplan73.analytiscout.gui.utils.GuiCommand;
import org.leplan73.analytiscout.gui.utils.LoggedDialog;
import org.leplan73.analytiscout.gui.utils.Logging;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.slf4j.LoggerFactory;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

@SuppressWarnings("serial")
public class AnalyseRegistreDePresenceCivile extends Dialogue implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JFileChooser fcEntree = new JFileChooser();
	private JFileChooser fcEntreeP = new JFileChooser();
	private File fEntree = new File(Preferences.lit(Consts.REPERTOIRE_ENTREE, "données", false),"registrepresence.csv");
	private File fEntreeP = new File(Preferences.lit(Consts.REPERTOIRE_ENTREE, "données", false),"registrepresence.csv");
	protected File fModele = new File("conf/modele_registrepresence_civile.xlsx");
	private JFileChooser fcSortie = new JFileChooser();
	protected File fSortie = new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"analyse_registrepresence.xlsx");
	private JLabel lblSortie;
	private JLabel lblEntree;
	private JButton btnGo;
	private BoutonOuvrir btnOuvrir;
	private JLabel lblEntreeP;

	/**
	 * Create the dialog.
	 */
	public AnalyseRegistreDePresenceCivile() {
		super();
		logger_ = LoggerFactory.getLogger(AnalyseRegistreDePresenceCivile.class);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Analyse registre de présence (année civile)");

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
		gbl_contentPanel.columnWidths = new int[] { 211, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Entr\u00E9e Ann\u00E9e N-1", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblEntreeP = new JLabel(fEntreeP.getAbsolutePath());
				panel.add(lblEntreeP, BorderLayout.WEST);
			}
			{
				JButton btnFichierP = new JButton("Fichier...");
				btnFichierP.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcEntreeP.setDialogTitle("Répertoire de données");
						fcEntreeP.setApproveButtonText("Go");
						fcEntreeP.setCurrentDirectory(fEntreeP.getParentFile());
						fcEntreeP.setSelectedFile(fEntreeP);
						fcEntreeP.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcEntreeP.addChoosableFileFilter(new ExportFileFilter("csv"));
						int result = fcEntreeP.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fEntreeP = fcEntreeP.getSelectedFile();
							lblEntreeP.setText(fEntreeP.getPath());
						}
					}
				});
				panel.add(btnFichierP, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Entr\u00E9e Ann\u00E9e N", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblEntree = new JLabel(fEntree.getAbsolutePath());
				panel.add(lblEntree, BorderLayout.WEST);
			}
			{
				JButton btnFichier = new JButton("Fichier...");
				btnFichier.addActionListener(new ActionListener() {
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
				panel.add(btnFichier, BorderLayout.EAST);
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
							fcSortie.setDialogTitle("Fichier de sortie");
							fcSortie.setApproveButtonText("Export");
							fcSortie.setCurrentDirectory(fSortie.getParentFile());
							fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
							fcSortie.setSelectedFile(fSortie);
							fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter());
							fcSortie.removeChoosableFileFilter(fcSortie.getAcceptAllFileFilter());
							fcSortie.addChoosableFileFilter(new ExportFileFilter("xlsx"));
							int result = fcSortie.showDialog(panel, "OK");
							if (result == JFileChooser.APPROVE_OPTION) {
								fSortie = ajouteExtensionFichier(fcSortie, lblSortie, "xlsx");
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
		if (fModele == null) {
			logger_.error("Modèle non-sélectionnée");
			return false;
		}
		if (fSortie == null) {
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
		
		EngineAnalyseurRegistreDePresence en = new EngineAnalyseurRegistreDePresence(progress, logger_);

		new Thread(() -> {
			progress.start();
			initLog();
			boolean ret = check();
			progress.setProgress(20, null);
			if (ret) {
				logger_.info("Lancement");
				try {
					en.go(fEntreeP, fEntree, fModele, fSortie, null, false, Params.getb(Consts.PARAMS_ANONYMISER, false));
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
		Preferences.sauve(Consts.REPERTOIRE_SORTIE, this.fSortie.getParent(), false);
		super.dispose();
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
	public BoutonOuvrir getBtnOuvrir() {
		return btnOuvrir;
	}
	protected JLabel getLblEntreeP() {
		return lblEntreeP;
	}
}
