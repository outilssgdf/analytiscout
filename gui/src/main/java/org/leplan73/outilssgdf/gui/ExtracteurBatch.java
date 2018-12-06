package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.http.client.ClientProtocolException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.gui.utils.Appender;
import org.leplan73.outilssgdf.gui.utils.ExportFileFilter;
import org.leplan73.outilssgdf.gui.utils.GuiCommand;
import org.leplan73.outilssgdf.gui.utils.LoggedDialog;
import org.leplan73.outilssgdf.gui.utils.Logging;
import org.leplan73.outilssgdf.gui.utils.Preferences;
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtracteurBatch extends JDialog implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextArea txtLog;
	private JFileChooser fcSortie;
	private File fSortie;
	private JFileChooser fcBatch;
	private File fBatch;

	private Logger logger_ = LoggerFactory.getLogger(ExtracteurBatch.class);

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
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Appender.setLoggedDialog(this);

		setResizable(false);
		setTitle("ExtracteurBatch");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		double x = Preferences.litd(Consts.FENETRE_EXTRACTEURBATCH_X, 100);
		double y = Preferences.litd(Consts.FENETRE_EXTRACTEURBATCH_Y, 100);
		setBounds((int)x, (int)y, 566, 480);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 121, 0 };
		gbl_contentPanel.rowHeights = new int[] { 46, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(
					new TitledBorder(null, "Acc\u00E8s Intranet", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
				panel_1.setBorder(
						new TitledBorder(null, "Identifiant", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
				panel_1.setBorder(
						new TitledBorder(null, "Mot de passe", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfMotdepasse = new JPasswordField();
					txfMotdepasse.setColumns(10);
					panel_1.add(txfMotdepasse);
				}
			}
			{
				chkMemoriser = new JCheckBox("Mémoriser");
				chkMemoriser.setSelected(Preferences.litb(Consts.INTRANET_MEMORISER, false));
				if (chkMemoriser.isSelected())
				{
					txfIdentifiant.setText(Preferences.lit(Consts.INTRANET_IDENTIFIANT, "", true));
					txfMotdepasse.setText(Preferences.lit(Consts.INTRANET_MOTDEPASSE, "", true));
				}
				panel.add(chkMemoriser, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Structure", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				tfStructure = new JTextField();
				tfStructure.setColumns(30);
				panel.add(tfStructure);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Batch", TitledBorder.LEADING,
					TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
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
						fcBatch.removeChoosableFileFilter(fcBatch.getFileFilter());
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
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			{
				chkRecursif = new JCheckBox("Récursif");
				chkRecursif.setSelected(true);
				panel.add(chkRecursif);
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
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel("<rien>");
				panel.add(lblSortie, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Répertoire...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcSortie = new JFileChooser();
						fcSortie.setDialogTitle("Répertoire de données");
						fcSortie.setApproveButtonText("Go");
						fcSortie.setCurrentDirectory(new File("."));
						fcSortie.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnGo = new JButton("Go");
				buttonPane.add(btnGo);
				btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						go();
					}
				});
			}
		}
	}

	private ExtractionMain connection_;
	private JLabel lblBatch;
	private JLabel lblSortie;
	private JTextField tfStructure;
	private JButton btnGo;
	private JCheckBox chkMemoriser;
	private JCheckBox chkRecursif;

	private void login(ExtractionMain connection) throws ClientProtocolException, IOException {
		connection_ = connection;
		logger_.info("Connexion");

		connection_.init();
		if (connection_.login(txfIdentifiant.getText(), new String(txfMotdepasse.getPassword())) == false) {
			throw new IOException("erreur de connexion");
		}
	}

	public void logout() throws IOException {
		connection_.close();
	}

	@Override
	public boolean check() {
		logger_.info("Vérification des paramètres");
		if (fBatch == null) {
			logger_.error("Batch non-sélectionnée");
			return false;
		}
		if (fSortie == null) {
			logger_.error("Sortie non-sélectionnée");
			return false;
		}
		if (txfIdentifiant.getText().isEmpty()) {
			logger_.error("Identifiant est vide");
			return false;
		}
		if (txfMotdepasse.getPassword().length == 0) {
			logger_.error("Mode de passe est vide");
			return false;
		}
		return true;
	}

	@Override
	public void go() {
		ProgressMonitor progress = new ProgressMonitor(this, "ExtractionBatch", "", 0, 100);
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);

		new Thread(() -> {
			progress.setProgress(0);
			txtLog.setText("");

			Instant now = Instant.now();

			boolean ret = check();
			progress.setProgress(20);
			if (ret) {
				try {
					Properties pbatch = new Properties();
					pbatch.load(new FileInputStream(fBatch));

					ExtractionAdherents app = new ExtractionAdherents();
					login(app);
					progress.setProgress(40);

					String stStructures[] = tfStructure.getText().split(",");
					for (String stStructure : stStructures) {
						int structure = Integer.parseInt(stStructure);

						logger_.info("Traitement de la structure " + structure);

						int index = 1;
						for (;;) {
							// generateur.x
							// format.x
							// categorie.x
							// specialite.x
							// fonction.x
							// diplome.x
							// qualif.x
							// formation.x
							// nom.x
							// type.x
							String generateur = pbatch.getProperty("generateur." + index);
							if (generateur == null) {
								break;
							}
							int diplome = pbatch.getProperty("diplome." + index, "").isEmpty()
									? ExtractionMain.DIPLOME_TOUT
									: Integer.parseInt(pbatch.getProperty("diplome." + index));
							int qualif = pbatch.getProperty("qualif." + index, "").isEmpty()
									? ExtractionMain.QUALIFICATION_TOUT
									: Integer.parseInt(pbatch.getProperty("qualif." + index));
							int formation = pbatch.getProperty("formation." + index, "").isEmpty()
									? ExtractionMain.FORMATION_TOUT
									: Integer.parseInt(pbatch.getProperty("formation." + index));
							int format = pbatch.getProperty("format." + index, "").isEmpty()
									? ExtractionMain.FORMAT_INDIVIDU
									: Integer.parseInt(pbatch.getProperty("format." + index));
							int categorie = pbatch.getProperty("categorie." + index, "").isEmpty()
									? ExtractionMain.CATEGORIE_TOUT
									: Integer.parseInt(pbatch.getProperty("categorie." + index));
							int type = pbatch.getProperty("type." + index, "").isEmpty() ? ExtractionMain.TYPE_TOUT
									: Integer.parseInt(pbatch.getProperty("type." + index));
							int specialite = pbatch.getProperty("specialite." + index, "").isEmpty()
									? ExtractionMain.SPECIALITE_SANS_IMPORTANCE
									: Integer.parseInt(pbatch.getProperty("specialite." + index));
							boolean adherents = pbatch.getProperty("adherents." + index, "").isEmpty() ? false
									: Boolean.parseBoolean(pbatch.getProperty("adherents." + index));
							String nom = pbatch.getProperty("nom." + index, "");
							String fonction = pbatch.getProperty("fonction." + index);

							File dossierStructure = fSortie;
							dossierStructure.mkdirs();

							File fichier = new File(dossierStructure, nom + "." + generateur);

							if (generateur.compareTo(ExtractionMain.GENERATEUR_XLS) == 0) {
								logger_.info("Extraction du fichier " + index + " dans " + fichier);

								Writer out = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_WINDOWS));
								String donnees = app.extract(structure, chkRecursif.isSelected(), type, adherents,
										fonction, specialite, categorie, diplome, qualif, formation, format, true);
								out.write(donnees);
								out.flush();
								out.close();
								logger_.info("Extraction du fichier " + index + " fait");
							} else if (generateur.compareTo(ExtractionMain.GENERATEUR_XML) == 0) {
								logger_.info("Extraction du fichier " + index + " dans " + fichier);

								Writer out = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fichier), Consts.ENCODING_UTF8));
								String donnees = app.extract(structure, chkRecursif.isSelected(), type, adherents,
										fonction, specialite, categorie, diplome, qualif, formation, format, false);
								out.write(donnees);
								out.flush();
								out.close();
								logger_.info("Extraction du fichier " + index + " fait");
							} else if (generateur.compareTo(ExtractionMain.GENERATEUR_CSV) == 0) {
								logger_.info("Extraction du fichier " + index + " dans " + fichier);

								final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(fichier,
										Charset.forName(Consts.ENCODING_WINDOWS));

								String donnees = app.extract(structure, chkRecursif.isSelected(), type, adherents,
										fonction, specialite, categorie, diplome, qualif, formation, format, false);

								XPathFactory xpfac = XPathFactory.instance();
								SAXBuilder builder = new SAXBuilder();
								org.jdom2.Document docx = builder.build(
										new ByteArrayInputStream(donnees.getBytes(Charset.forName(Consts.ENCODING_UTF8))));

								// Scan des colonnes
								XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
								List<?> resultsc = xpac.evaluate(docx);
								int nbColumns = resultsc.size();

								XPathExpression<?> xpa = xpfac.compile("tbody/tr/td");

								List<?> results = xpa.evaluate(docx);

								int indexCsv = 0;
								Iterator<?> iter = results.iterator();
								while (iter.hasNext()) {
									Object result = iter.next();
									Element resultElement = (Element) result;
									out.print(resultElement.getText());
									indexCsv++;
									if (indexCsv % nbColumns == 0) {
										out.println();
									}
								}
								out.flush();
								out.close();

								logger_.info("Extraction du fichier " + index + " fait");
							}
							index++;
						}
					}
					logout();
				} catch (IOException | JDOMException e) {
					logger_.error(Logging.dumpStack(null, e));
				}
			}
			progress.setProgress(100);

			long d = Instant.now().getEpochSecond() - now.getEpochSecond();
			logger_.info("Terminé en " + d + " secondes");
		}).start();
	}

	@Override
	public void dispose() {
		Appender.setLoggedDialog(null);

		Preferences.sauveb(Consts.INTRANET_MEMORISER, chkMemoriser.isSelected());
		Preferences.sauved(Consts.FENETRE_EXTRACTEURBATCH_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_EXTRACTEURBATCH_Y, this.getLocation().getY());
		if (chkMemoriser.isSelected())
		{
			Preferences.sauve(Consts.INTRANET_IDENTIFIANT, txfIdentifiant.getText(), true);
			Preferences.sauve(Consts.INTRANET_MOTDEPASSE, new String(txfMotdepasse.getPassword()), true);
		}
		else
		{
			Preferences.sauve(Consts.INTRANET_IDENTIFIANT, "", true);
			Preferences.sauve(Consts.INTRANET_MOTDEPASSE, "", true);
		}
		super.dispose();
	}

	@Override
	public void addLog(String message) {
		String texte = txtLog.getText();
		if (texte.length() > 0)
			texte += "\n";
		texte += message;
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

	public JTextField getTfStructure() {
		return tfStructure;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
	public JCheckBox getChkMemoriser() {
		return chkMemoriser;
	}
	public JCheckBox getChkRecursif() {
		return chkRecursif;
	}
}
