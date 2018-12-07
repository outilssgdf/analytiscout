package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.manifests.Manifests;

import net.sf.jett.transform.ExcelTransformer;

public class AnalyseurEnLigne extends JDialog implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextArea txtLog;
	private JFileChooser fcSortie;
	private File fSortie = new File("./données/analyse.xlsx");
	private JFileChooser fcBatch;
	private File fBatch = new File("./conf/batch.txt");
	private JFileChooser fcModele = new JFileChooser();
	private File fModele = new File("conf/modele.xlsx");

	private Logger logger_ = LoggerFactory.getLogger(AnalyseurEnLigne.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AnalyseurEnLigne dialog = new AnalyseurEnLigne();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AnalyseurEnLigne() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Appender.setLoggedDialog(this);

		setResizable(false);
		setTitle("AnalyseurEnLigne");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		double x = Preferences.litd(Consts.FENETRE_ANALYSEURENLIGNE_X, 100);
		double y = Preferences.litd(Consts.FENETRE_ANALYSEURENLIGNE_Y, 100);
		setBounds((int)x, (int)y, 566, 627);
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
				txfStructure = new JTextField();
				txfStructure.setColumns(30);
				panel.add(txfStructure);
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
				chkRecursif = new JCheckBox("Récursif");
				chkRecursif.setSelected(true);
				panel.add(chkRecursif, BorderLayout.WEST);
			}
			{
				chkAge = new JCheckBox("Gestion de l'âge");
				panel.add(chkAge, BorderLayout.EAST);
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
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnGo = new JButton("Go");
				buttonPane.add(btnGo);
				{
					JButton button = new JButton("Quitter");
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					buttonPane.add(button);
				}
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
	private JTextField txfStructure;
	private JButton btnGo;
	private JCheckBox chkMemoriser;
	private JCheckBox chkRecursif;
	private JCheckBox chkAge;
	private JLabel lblModele;

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
		if (txfStructure.getText().isEmpty()) {
			logger_.error("Structure est vide");
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

					String stStructures[] = txfStructure.getText().split(",");
					for (String stStructure : stStructures) {
						int structure = Integer.parseInt(stStructure);

						logger_.info("Traitement de la structure " + structure);

						Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
						
						String donneesAdherents=null;
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
							boolean adherentsseuls = pbatch.getProperty("adherents." + index, "").isEmpty() ? false
									: Boolean.parseBoolean(pbatch.getProperty("adherents." + index));
							String nom = pbatch.getProperty("nom." + index, "");
							String fonction = pbatch.getProperty("fonction." + index);

							ExtraKey extra = new ExtraKey(pbatch.getProperty("nom." + index, ""),
									pbatch.getProperty("batchtype." + index, "tout"));
							
							logger_.info("Extraction de  "+nom);
							String donnees = app.extract(structure,chkRecursif.isSelected(),type,adherentsseuls,fonction,specialite,categorie, diplome,qualif,formation,format, false);
							logger_.info("Extraction de  "+nom+" fait");
							
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
						ExtracteurHtml adherents = new ExtracteurHtml(in, extraMap,chkAge.isSelected());
				 
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
				
						FileOutputStream outputStream = new FileOutputStream(fSortie);
				
					    logger_.info("Génération du fichier \""+fSortie.getName()+"\" à partir du modèle \""+fModele.getName()+"\"");
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
		return txfStructure;
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
	public JCheckBox getChkAge() {
		return chkAge;
	}
	public JLabel getLblModele() {
		return lblModele;
	}
}
