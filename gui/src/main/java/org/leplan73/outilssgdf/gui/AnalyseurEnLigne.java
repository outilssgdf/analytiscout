package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.Consts;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurIndividusHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Transformeur;
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
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionIntranet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.manifests.Manifests;
import java.awt.Toolkit;

public class AnalyseurEnLigne extends JDialog implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextArea txtLog;
	private JFileChooser fcSortie;
	private File fSortie = new File("./données/analyse.xlsx");
	private JFileChooser fcBatch;
	private File fBatch = new File("./conf/batch_responsables.txt");
	private JFileChooser fcModele = new JFileChooser();
	private File fModele = new File("conf/modele_responsables.xlsx");

	private Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigne.class);

	/**
	 * Create the dialog.
	 * @param fModele 
	 * @param fBatch 
	 * @param fSortie 
	 * @param logger 
	 * @throws URISyntaxException 
	 */
	public AnalyseurEnLigne(String titre, Logger logger, File pfSortie, File pfBatch, File pfModele) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(AnalyseurEnLigne.class.getResource("/org/leplan73/outilssgdf/gui/icone.png")));
		this.logger_ = logger;
		this.fSortie = pfSortie;
		this.fBatch = pfBatch;
		this.fModele = pfModele;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Appender.setLoggedDialog(this);

		setResizable(false);
		setTitle(titre);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		double x = Preferences.litd(Consts.FENETRE_ANALYSEURENLIGNE_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEURENLIGNE_Y, 100);
		setBounds((int)x, (int)y, 726, 627);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 121, 0 };
		gbl_contentPanel.rowHeights = new int[] { 46, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
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
					txfIdentifiant.setColumns(15);
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
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Code structure", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				txfCodeStructure = new JTextField();
				txfCodeStructure.setColumns(30);
				txfCodeStructure.setText(Preferences.lit(Consts.INTRANET_STRUCTURE, "", true));
				panel.add(txfCodeStructure, BorderLayout.NORTH);
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
				lblBatch = new JLabel(fBatch.getAbsolutePath());
				panel.add(lblBatch, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcBatch = new JFileChooser();
						fcBatch.setDialogTitle("Fichier batch");
						fcBatch.setApproveButtonText("Go");
						fcBatch.setSelectedFile(fBatch);
						fcBatch.setCurrentDirectory(new File("."));
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
			panel.setBorder(new TitledBorder(null, "Mod\u00E8le", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblModele = new JLabel(fModele.getAbsolutePath());
				panel.add(lblModele, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
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
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				chkAge = new JCheckBox("Gestion de l'âge");
				panel.add(chkAge, BorderLayout.WEST);
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
			gbc_panel.gridy = 5;
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
						fcSortie = new JFileChooser();
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
			gbc_panel.gridy = 6;
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
					btnGo = new JButton("Go");
					panel.add(btnGo);
					{
						JButton btnFermer = new JButton("Fermer");
						panel.add(btnFermer);
						btnFermer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								dispose();
							}
						});
					}
					btnGo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							go();
						}
					});
				}
				{
					JPanel panel = new JPanel();
					buttonPane.add(panel, BorderLayout.WEST);
					{
						JButton btnAide = new JButton("Aide");
						btnAide.setEnabled(false);
						panel.add(btnAide);
					}
				}
			}
		}
	}

	private ExtractionIntranet connection_;
	private JLabel lblBatch;
	private JLabel lblSortie;
	private JTextField txfCodeStructure;
	private JButton btnGo;
	private JCheckBox chkMemoriser;
	private JCheckBox chkAge;
	private JLabel lblModele;

	private void login(ExtractionIntranet connection) throws ClientProtocolException, IOException {
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
			logger_.error("Le fichier batch est non-sélectionnée");
			return false;
		}
		if (fSortie == null) {
			logger_.error("Le répertoire de sortie est non-sélectionnée");
			return false;
		}
		if (txfIdentifiant.getText().isEmpty()) {
			logger_.error("L'identifiant est vide");
			return false;
		}
		if (txfMotdepasse.getPassword().length == 0) {
			logger_.error("Le mode de passe est vide");
			return false;
		}
		if (txfCodeStructure.getText().isEmpty()) {
			logger_.error("Le code de structure est vide");
			return false;
		}
		if (txfCodeStructure.getText().compareTo(Consts.STRUCTURE_NATIONAL) == 0)
		{
			logger_.error("Code de structure interdit");
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

					String stStructures[] = txfCodeStructure.getText().split(",");
					for (String stStructure : stStructures) {
						int structure = Integer.parseInt(stStructure);

						logger_.info("Traitement de la structure " + structure);

						Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
						
						String donneesAdherents=null;
						int index = 1;
						for (;;) {
							
							if (progress.isCanceled()) {
								logger_.info("Action annulée");
								break;
							}
							
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
									? ExtractionIntranet.DIPLOME_TOUT
									: Integer.parseInt(pbatch.getProperty("diplome." + index));
							int qualif = pbatch.getProperty("qualif." + index, "").isEmpty()
									? ExtractionIntranet.QUALIFICATION_TOUT
									: Integer.parseInt(pbatch.getProperty("qualif." + index));
							int formation = pbatch.getProperty("formation." + index, "").isEmpty()
									? ExtractionIntranet.FORMATION_TOUT
									: Integer.parseInt(pbatch.getProperty("formation." + index));
							int format = pbatch.getProperty("format." + index, "").isEmpty()
									? ExtractionIntranet.FORMAT_INDIVIDU
									: Integer.parseInt(pbatch.getProperty("format." + index));
							int categorie = pbatch.getProperty("categorie." + index, "").isEmpty()
									? ExtractionIntranet.CATEGORIE_TOUT
									: Integer.parseInt(pbatch.getProperty("categorie." + index));
							int type = pbatch.getProperty("type." + index, "").isEmpty() ? ExtractionIntranet.TYPE_TOUT
									: Integer.parseInt(pbatch.getProperty("type." + index));
							int specialite = pbatch.getProperty("specialite." + index, "").isEmpty()
									? ExtractionIntranet.SPECIALITE_SANS_IMPORTANCE
									: Integer.parseInt(pbatch.getProperty("specialite." + index));
							boolean adherentsseuls = pbatch.getProperty("adherents." + index, "").isEmpty() ? false
									: Boolean.parseBoolean(pbatch.getProperty("adherents." + index));
							String nom = pbatch.getProperty("nom." + index, "");
							String fonction = pbatch.getProperty("fonction." + index);

							ExtraKey extra = new ExtraKey(pbatch.getProperty("fichier." + index, nom), nom, pbatch.getProperty("batchtype." + index, "tout_responsables"));
							
							logger_.info("Extraction de "+nom);
							String donnees = app.extract(structure,true,type,adherentsseuls,fonction,specialite,categorie, diplome,qualif,formation,format, false);
							logger_.info("Extraction de "+nom+" fait");
							
							if (extra.ifTout()) {
								donneesAdherents = donnees;
							} else {
								InputStream in = new ByteArrayInputStream(donnees.getBytes(Consts.ENCODING_UTF8));
								extraMap.put(extra, new ExtracteurExtraHtml(in, chkAge.isSelected()));
							}
							index++;
						}
						progress.setProgress(50);
						
						InputStream in = new ByteArrayInputStream(donneesAdherents.getBytes(Consts.ENCODING_UTF8));
						ExtracteurIndividusHtml adherents = new ExtracteurIndividusHtml(in, extraMap,chkAge.isSelected());
				 
						AdherentFormes compas = new AdherentFormes();
						compas.charge(adherents,extraMap);
						progress.setProgress(60);
						
						String version = "";
						try
						{
							version = Manifests.read("version");
						}
						catch(java.lang.IllegalArgumentException e) {
						}
						General general = new General(version);
						Global global = new Global(adherents.getGroupe(), adherents.getMarins());
						adherents.calculGlobal(global);
						progress.setProgress(80);
				
					    logger_.info("Génération du fichier \""+fSortie.getName()+"\" à partir du modèle \""+fModele.getName()+"\"");
						Map<String, Object> beans = new HashMap<String, Object>();
						beans.put("adherents", adherents.getAdherentsList());
						beans.put("chefs", adherents.getChefsList());
						beans.put("compas", adherents.getCompasList());
						beans.put("unites", adherents.getUnitesList());
						beans.put("general", general);
						beans.put("global", global);

						Transformeur.go(fModele, beans, fSortie);
					}
					logout();
				} catch (IOException | JDOMException | InvalidFormatException | ExtractionException e) {
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
		Preferences.sauved(Consts.FENETRE_ANALYSEURENLIGNE_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_ANALYSEURENLIGNE_Y, this.getLocation().getY());
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
		Preferences.sauve(Consts.INTRANET_STRUCTURE, txfCodeStructure.getText(), true);
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
		return txfCodeStructure;
	}

	public JButton getBtnGo() {
		return btnGo;
	}
	public JCheckBox getChkMemoriser() {
		return chkMemoriser;
	}
	public JCheckBox getChkAge() {
		return chkAge;
	}
	public JLabel getLblModele() {
		return lblModele;
	}
}
