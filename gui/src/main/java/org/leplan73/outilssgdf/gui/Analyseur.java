package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.leplan73.outilssgdf.gui.utils.Appender;
import org.leplan73.outilssgdf.gui.utils.ExportFileFilter;
import org.leplan73.outilssgdf.gui.utils.GuiCommand;
import org.leplan73.outilssgdf.gui.utils.LoggedDialog;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.manifests.Manifests;

import net.sf.jett.transform.ExcelTransformer;

public class Analyseur extends JDialog implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private Logger logger_ = LoggerFactory.getLogger(Analyseur.class);
	private JFileChooser fcBatch = new JFileChooser();
	private File fBatch = new File("./conf/batch.txt");
	private JFileChooser fcEntree = new JFileChooser();
	private File fEntree = new File("./données");
	private JFileChooser fcModele = new JFileChooser();
	private File fModele = new File("conf/modele.xlsx");
	private JFileChooser fcSortie = new JFileChooser();
	private File fSortie = new File("./données/analyse.xlsx");
	private JCheckBox chcAge;
	private JLabel lblSortie;
	private JLabel lblBatch;
	private JLabel lblEntree;
	private JLabel lblModele;
	private JTextArea txtLog;
	private JButton btnGo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Analyseur dialog = new Analyseur();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Analyseur() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Appender.setLoggedDialog(this);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Analyseur");
		double x = Preferences.litd(Consts.FENETRE_ANALYSEUR_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEUR_Y, 100);
		setBounds((int)x, (int)y, 778, 608);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 211, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Batch", TitledBorder.LEADING,
					TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblBatch = new JLabel(fBatch.getAbsolutePath());
				panel.add(lblBatch, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcBatch.setDialogTitle("Fichier batch");
						fcBatch.setApproveButtonText("Go");
						fcBatch.setCurrentDirectory(new File("."));
						fcBatch.setSelectedFile(fBatch);
						fcBatch.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcBatch.removeChoosableFileFilter(fcBatch.getFileFilter());
						fcBatch.removeChoosableFileFilter(fcBatch.getAcceptAllFileFilter());
						fcBatch.addChoosableFileFilter(new ExportFileFilter("txt"));
						int result = fcBatch.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fBatch = fcBatch.getSelectedFile();
							lblBatch.setText(fBatch.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Entr\u00E9e", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
				JButton btnDossier = new JButton("Répertoire...");
				btnDossier.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcEntree.setDialogTitle("Répertoire de données");
						fcEntree.setApproveButtonText("Go");
						fcEntree.setCurrentDirectory(new File("."));
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
			panel.setBorder(new TitledBorder(null, "Mod\u00E8le", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblModele = new JLabel(fModele.getAbsolutePath());
				panel.add(lblModele, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcModele.setDialogTitle("Fichier modèle");
						fcModele.setApproveButtonText("Go");
						fcModele.setCurrentDirectory(new File("."));
						fcModele.setSelectedFile(fModele);
						fcModele.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcModele.removeChoosableFileFilter(fcModele.getFileFilter());
						fcModele.removeChoosableFileFilter(fcModele.getAcceptAllFileFilter());
						fcModele.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcModele.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fModele = fcModele.getSelectedFile();
							lblModele.setText(fModele.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
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
			gbc_panel.gridy = 3;
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
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel(fSortie.getAbsolutePath());
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcSortie.setDialogTitle("Export Configuration");
						fcSortie.setApproveButtonText("Export");
						fcSortie.setCurrentDirectory(new File("."));
						fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter());
						fcSortie.removeChoosableFileFilter(fcSortie.getAcceptAllFileFilter());
						fcSortie.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcSortie.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {
							fSortie = fcSortie.getSelectedFile();
							lblSortie.setText(fSortie.getPath());
						}
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
		if (fSortie == null) {
			logger_.error("Sortie non-sélectionnée");
			return false;
		}
		return true;
	}

	@Override
	public void go() {
		ProgressMonitor progress = new ProgressMonitor(this, "Analyseur", "", 0, 100);
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);

		new Thread(() -> {
			progress.setProgress(0);
			txtLog.setText("");
			btnGo.setEnabled(false);

			Instant now = Instant.now();

			boolean ret = check();
			progress.setProgress(20);
			if (ret) {
				logger_.info("Lancement");

				try {
					logger_.info("Chargement du fichier de traitement");

					Properties pbatch = new Properties();
					pbatch.load(new FileInputStream(fBatch));

					Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
					File fichierAdherents = null;

					File dossierStructure = fEntree;
					dossierStructure.exists();
					progress.setProgress(40);

					int index = 1;
					for (;;) {
						String generateur = pbatch.getProperty("generateur." + index);
						if (generateur == null) {
							break;
						}

						ExtraKey extra = new ExtraKey(pbatch.getProperty("nom." + index, ""),
								pbatch.getProperty("batchtype." + index, "tout"));
						File fichier = new File(dossierStructure, extra.nom_ + "." + generateur);

						logger_.info("Chargement du fichier \"" + fichier.getName() + "\"");

						if (extra.ifTout()) {
							fichierAdherents = fichier;
						} else
						{
							extraMap.put(extra, new ExtracteurExtraHtml(fichier, getChcAge().isSelected()));
						}
						index++;
					}
					progress.setProgress(50);
					logger_.info("Chargement du fichier \"" + fichierAdherents.getName() + "\"");
					ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, extraMap, getChcAge().isSelected());

					AdherentFormes compas = new AdherentFormes();
					compas.charge(adherents, extraMap);
					progress.setProgress(60);

					String version = "";
					try {
						version = Manifests.read("version");
					} catch (java.lang.IllegalArgumentException e) {
					}
					General general = new General(version);
					Global global = new Global(adherents.getGroupe(), adherents.getMarins());
					adherents.calculGlobal(global);
					progress.setProgress(80);

					FileOutputStream outputStream = new FileOutputStream(fSortie);

					logger_.info("Génération du fichier \"" + fSortie.getName()
							+ "\" à partir du modèle \"" + fModele.getName() + "\"");
					ExcelTransformer trans = new ExcelTransformer();
					Map<String, Object> beans = new HashMap<String, Object>();
					beans.put("chefs", adherents.getChefsList());
					beans.put("compas", adherents.getCompasList());
					beans.put("unites", adherents.getUnitesList());
					beans.put("general", general);
					beans.put("global", global);
					Workbook workbook = trans.transform(new FileInputStream(fModele), beans);
					workbook.write(outputStream);

					outputStream.flush();
					outputStream.close();

				} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
					logger_.error(Logging.dumpStack(null, e));
				}
			}
			progress.setProgress(100);
			long d = Instant.now().getEpochSecond() - now.getEpochSecond();
			logger_.info("Terminé en " + d + " secondes");

			btnGo.setEnabled(true);
		}).start();
	}

	@Override
	public void dispose() {
		Appender.setLoggedDialog(null);
		Preferences.sauved(Consts.FENETRE_ANALYSEUR_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_ANALYSEUR_Y, this.getLocation().getY());
		super.dispose();
	}

	@Override
	public void addLog(String message) {
		String texte = txtLog.getText();
		if (texte.length() > 0)
			txtLog.append("\n");
		txtLog.append(message);
		txtLog.setCaretPosition(txtLog.getDocument().getLength());
	}

	public JCheckBox getChcAge() {
		return chcAge;
	}

	public JLabel getLblSortie() {
		return lblSortie;
	}

	public JLabel getLblBatch() {
		return lblBatch;
	}

	public JLabel getLblEntree() {
		return lblEntree;
	}

	public JLabel getLblModele() {
		return lblModele;
	}

	public JTextArea getTxtLog() {
		return txtLog;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
}
