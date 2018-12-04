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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
import org.leplan73.outilssgdf.intranet.ExtractionAdherents;
import org.leplan73.outilssgdf.intranet.ExtractionMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extracteur extends JDialog implements LoggedDialog {

	private static final String ENCODING_WINDOWS = "Windows-1252";
	private static final String ENCODING_UTF8 = "UTF-8";
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextField txfStructure;
	private JTextField txfCodefonction;
	private JComboBox cbxGenerateur;
	private static Logger logger_ = LoggerFactory.getLogger(Extracteur.class);
	
	private JFileChooser fcSortie;
	private JLabel lblSortie;
	private JTextArea txtLog;
	private JCheckBox chkFormatIndividu;
	private JCheckBox chkFormatParents;
	private JCheckBox chkFormatInscription;
	private JCheckBox chkFormatAdhesion;
	private JCheckBox chkFormatJS;
	private JCheckBox chkFormatSansQF;
	private JComboBox cbxTypeinscription;
	private JComboBox cbxType;
	private JComboBox cbxCategorie;
	private JComboBox cbxSpecialite;
	private JCheckBox chkAdherents;
	private JComboBox cbxFormation;
	private JComboBox cbxQualification;
	private JComboBox cbxDiplome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Extracteur dialog = new Extracteur();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Extracteur() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Appender.setLoggedDialog(this);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Export");
		setBounds(100, 100, 676, 666);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 155, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Identifiant", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				panel.add(panel_1, BorderLayout.WEST);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfIdentifiant = new JTextField();
					panel_1.add(txfIdentifiant);
					txfIdentifiant.setColumns(30);
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
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Structure", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.WEST);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfStructure = new JTextField();
					panel_1.add(txfStructure);
					txfStructure.setColumns(30);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Code fonction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
				{
					txfCodefonction = new JTextField();
					panel_1.add(txfCodefonction);
					txfCodefonction.setColumns(10);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{141, 207, 134, 0};
			gbl_panel.rowHeights = new int[]{56, 0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Dipl\u00F4me", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxDiplome = new JComboBox();
					cbxDiplome.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "2 Buchettes", "3 Buchettes", "4 Buchettes", "AFPS", "BAFA", "BAFA Qualification Voile", "BAFD", "BP JEPS", "Brevet d'état (activités marines)", "Brevet Professionnel (Skipper, marine marchande)", "Carte Mer", "Certificat Radio Restreint CRR", "Chef de Flottille", "Chef de quart", "DE JEPS", "DEUG STAPS", "Directeur nautique", "DUT animation socioculturelle ", "Licence STAPS", "Médaille Argent de la JS", "Médaille Bronze de la JS", "Médaille Or de la JS", "Nœud de tisserand", "Patron d'embarcation", "Permis Côtier", "Permis E", "Permis fluvial", "Permis Hauturier", "PSC1 Prévention et Secours Civiques de niveau 1", "Spirale AMGE", "Surveillant de Baignade"}));
					panel_1.add(cbxDiplome);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Type d'inscription", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 1;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxTypeinscription = new JComboBox();
					cbxTypeinscription.setModel(new DefaultComboBoxModel(new String[] {"Toutes (-1)", "Inscrit(e) (0)", "Invité(e) (1)", "Pré-inscrit(e) (2)"}));
					panel_1.add(cbxTypeinscription);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Type", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 2;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxType = new JComboBox();
					cbxType.setModel(new DefaultComboBoxModel(new String[] {"tout (-1)", "inscrit (0)", "invite (1)", "pré-inscrit (2)"}));
					panel_1.add(cbxType);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Qualification", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxQualification = new JComboBox();
					cbxQualification.setModel(new DefaultComboBoxModel(new String[] {"Sans importance (-1)", "Animateur SF (CAFASF)", "Directeur SF (CAFDSF)", "Responsable Unité SF (CAFRUSF)"}));
					panel_1.add(cbxQualification);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Sp\u00E9cialit\u00E9", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 1;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxSpecialite = new JComboBox();
					cbxSpecialite.setModel(new DefaultComboBoxModel(new String[] {"Sans importance (-1)", "Marine (622)", "Sans spécialité (624)", "Vent du Large (623)"}));
					panel_1.add(cbxSpecialite);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Cat\u00E9gorie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 2;
				gbc_panel_1.gridy = 1;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxCategorie = new JComboBox();
					cbxCategorie.setModel(new DefaultComboBoxModel(new String[] {"Toutes (-1)", "Jeune (0)", "Responsable (1)"}));
					panel_1.add(cbxCategorie);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Formation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.insets = new Insets(0, 0, 0, 5);
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 2;
				panel.add(panel_1, gbc_panel_1);
				{
					cbxFormation = new JComboBox();
					cbxFormation.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "AFPS", "AIDE A LA PRISE DE FONCTION", "APF AUMONIER ET AVSC", "APF CHEFS - CHEFTAINES", "APF RESPONSABLE DE GROUPE", "APF RESPONSABLE LOCAL DEVELOPPEMENT ET RESEAUX", "APF SECRETAIRE TRESORIER", "APPRO", "APPRO ACCUEIL DE SCOUTISME", "APPRO ANIMATION", "APPRO KAYAK", "APPRO SURVEILLANT DE BAIGNADE", "APPRO TERRE ET MER", "ASSISES DES FORMATEURS", "AUTRE STAGE", "BAFA APPROFONDISSEMENT", "BAFA FORMATION GENERALE", "BAFD FORMATION GENERALE", "BAFD PERFECTIONNEMENT", "CEP 1", "CEP 2", "CHAM", "CHEF DE FLOTTILLE", "CHEF DE QUART", "FAC", "FIAC", "FIRPAF", "FIRPDEV", "FIRPP", "FORMATION A LA COMMUNICATION", "FORMATION ACCUEIL DE SCOUTISME RG", "FORMATION COMPAGNON 1ER TEMPS", "FORMATION COMPAGNON 2EME TEMPS", "FORMATION COMPLEMENTAIRE DE FORMATEUR", "FORMATION CONTINUE DE DIRECTEURS", "FORMATION CONTINUE DE FORMATEURS", "FORMATION CONTINUE DES RESPONSABLES DE GROUPE", "FORMATION CONTINUE VENT DU LARGE", "FORMATION DE DIRECTEUR DE STAGE", "FORMATION DEPART A L'ETRANGER", "FORMATION DES ACCOMPAGNATEURS COMPAGNONS", "FORMATION DES AUMONIERS ET ANIMATEURS CLEOPHAS ", "FORMATION DES RESPONSABLES FARFADETS", "FORMATION DEVELOPPEMENT ET RESEAUX", "FORMATION GENERALE RESPONSABLE DE GROUPE", "FORMATION INITIALE ACCOMPAGNATEURS PEDAGOGIQUES", "FORMATION INITIALE DE FORMATEURS", "FORMATION INITIALE DES AUMONIERS", "FORMATION INITIALE DES AVSC", "FORMATION INITIALE DES DELEGUES TERRITORIAUX", "FORMATION INITIALE DES EQUIPIERS NATIONAUX", "FORMATION INITIALE DES EQUIPIERS TERRITORIAUX", "FORMATION INITIALE DES MEDIATEURS", "FORMATION INITIALE DES POLES ADMIN ET FINANCIER", "FORMATION INITIALE DES POLES DEVELOPPEMENT", "FORMATION INITIALE DES RESPONSABLES DE GROUPE", "FORMATION INITIALE DES RESPONSABLES DE P&#212;LES", "FORMATION INITIALE TRESORIER-SECRETAIRE", "FORMATION INITIALE VENT DU LARGE", "FORMATION JEUNES ADULTES - DEPART A L'ETRANGER", "FORMATION SERVICE CIVIQUE", "FORMATION SPECIFIQUE MARINE", "MASTERCLASS", "MODULE ANIMATEUR DE SCOUTISME ET CAMPISME", "MODULE APPRO ACCUEIL DE SCOUTISME", "MODULE APPRO ANIMATION", "MODULE APPRO CONSTRUCTION-FABRICATION", "MODULE APPRO CUISINE EN CAMPS", "MODULE APPRO EXPRESSION ARTISTIQUE", "MODULE APPRO HANDICAP", "MODULE APPRO RENCONTRES INTERNATIONALES", "MODULE DE FORMATION CONTINUE DE FORMATEURS", "MODULE SPECIFIQUE  DE FORMATION", "PATRON D'EMBARCATION", "PSC1 ", "SEMINAIRE DE FORMATION DES RESPONSABLES DE GROUPE", "SEMINAIRE DELEGUES TERRITORIAUX", "SEMINAIRE DES POLES DEVELOPPEMENT", "SENAMCO 2", "STAF", "STAGE 2 QUALIFICATION VOILE", "STIF", "TECH", "VALIDATION MONITORAT VOILE 1ER DEGRE"}));
					panel_1.add(cbxFormation);
				}
			}
			{
				chkAdherents = new JCheckBox("Adhérents uniquement");
				GridBagConstraints gbc_chkAdherents = new GridBagConstraints();
				gbc_chkAdherents.anchor = GridBagConstraints.WEST;
				gbc_chkAdherents.insets = new Insets(0, 0, 0, 5);
				gbc_chkAdherents.gridx = 1;
				gbc_chkAdherents.gridy = 2;
				panel.add(chkAdherents, gbc_chkAdherents);
			}
			{
				chkRecursif = new JCheckBox("Récursif");
				chkRecursif.setSelected(true);
				GridBagConstraints gbc_chkRecursif = new GridBagConstraints();
				gbc_chkRecursif.anchor = GridBagConstraints.WEST;
				gbc_chkRecursif.gridx = 2;
				gbc_chkRecursif.gridy = 2;
				panel.add(chkRecursif, gbc_chkRecursif);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.VERTICAL;
			gbc_panel.anchor = GridBagConstraints.WEST;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				GridBagLayout gbl_panel_1 = new GridBagLayout();
				gbl_panel_1.columnWidths = new int[]{515, 0, 0};
				gbl_panel_1.rowHeights = new int[]{0, 0, 0};
				gbl_panel_1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				gbl_panel_1.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				panel_1.setLayout(gbl_panel_1);
				{
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new TitledBorder(null, "Sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					GridBagConstraints gbc_panel_2 = new GridBagConstraints();
					gbc_panel_2.anchor = GridBagConstraints.NORTH;
					gbc_panel_2.insets = new Insets(0, 0, 5, 5);
					gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
					gbc_panel_2.gridx = 0;
					gbc_panel_2.gridy = 0;
					panel_1.add(panel_2, gbc_panel_2);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						lblSortie = new JLabel("<rien>");
						panel_2.add(lblSortie, BorderLayout.WEST);
					}
					{
						JButton button = new JButton("Fichier...");
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								fcSortie = new JFileChooser();
								fcSortie.setDialogTitle("Export Configuration");
								fcSortie.setApproveButtonText("Export");
								fcSortie.setCurrentDirectory(new File("."));
								fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
								fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter() );
								fcSortie.addChoosableFileFilter(new ExportFileFilter(cbxGenerateur.getSelectedItem().toString()));
								int result = fcSortie.showDialog(panel_2, "OK");
								if (result == JFileChooser.APPROVE_OPTION) {  
									File targetFile = fcSortie.getSelectedFile();
									lblSortie.setText(targetFile.getPath());
								}
							}
						});
						panel_2.add(button, BorderLayout.EAST);
					}
				}
				{
					JPanel panel_2_1 = new JPanel();
					GridBagConstraints gbc_panel_2_1 = new GridBagConstraints();
					gbc_panel_2_1.insets = new Insets(0, 0, 5, 0);
					gbc_panel_2_1.gridx = 1;
					gbc_panel_2_1.gridy = 0;
					panel_1.add(panel_2_1, gbc_panel_2_1);
					panel_2_1.setBorder(new TitledBorder(null, "G\u00E9n\u00E9rateur", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					{
						cbxGenerateur = new JComboBox();
						cbxGenerateur.setModel(new DefaultComboBoxModel(new String[] {"cvs", "xlsx", "xml"}));
						panel_2_1.add(cbxGenerateur);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					GridBagConstraints gbc_panel_2 = new GridBagConstraints();
					gbc_panel_2.anchor = GridBagConstraints.WEST;
					gbc_panel_2.insets = new Insets(0, 0, 0, 5);
					gbc_panel_2.fill = GridBagConstraints.VERTICAL;
					gbc_panel_2.gridx = 0;
					gbc_panel_2.gridy = 1;
					panel_1.add(panel_2, gbc_panel_2);
					{
						JPanel panel_2_1 = new JPanel();
						panel_2.add(panel_2_1);
						panel_2_1.setBorder(new TitledBorder(null, "Format", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						{
							chkFormatIndividu = new JCheckBox("individu");
							panel_2_1.add(chkFormatIndividu);
						}
						{
							chkFormatParents = new JCheckBox("parents");
							panel_2_1.add(chkFormatParents);
						}
						{
							chkFormatInscription = new JCheckBox("inscription");
							panel_2_1.add(chkFormatInscription);
						}
						{
							chkFormatAdhesion = new JCheckBox("adhesion");
							panel_2_1.add(chkFormatAdhesion);
						}
						{
							chkFormatJS = new JCheckBox("JS");
							panel_2_1.add(chkFormatJS);
						}
						{
							chkFormatSansQF = new JCheckBox("sans QF");
							panel_2_1.add(chkFormatSansQF);
						}
					}
				}
			}
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JProgressBar progress = new JProgressBar();
				progress.setStringPainted(true);
				panel.add(progress, BorderLayout.CENTER);
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
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
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
		if (txfStructure.getText().isEmpty())
		{
			logger_.error("Structure est vide");
			return false;
		}
		return true;
	}

	protected ExtractionMain connection_;
	private JCheckBox chkRecursif;
	
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
	
	private void export()
	{
		txtLog.setText("");
		
		Instant now = Instant.now();
		
		boolean ret = check();
		if (ret)
		{
			String generateur = (String)cbxGenerateur.getSelectedItem();
			int type = extract(((String)cbxType.getSelectedItem()));
			int specialite = extract((String)cbxSpecialite.getSelectedItem());
			int categorie = extract((String)cbxCategorie.getSelectedItem());
			int diplome = extract((String)cbxDiplome.getSelectedItem());
			int qualif = extract((String)cbxQualification.getSelectedItem());
			int formation = extract((String)cbxFormation.getSelectedItem());
			int structure = Integer.parseInt(txfStructure.getText());
			int format = 0;
			if (chkFormatIndividu.isSelected())
				format+=1;
			if (chkFormatParents.isSelected())
				format+=2;
			if (chkFormatInscription.isSelected())
				format+=4;
			if (chkFormatAdhesion.isSelected())
				format+=8;
			if (chkFormatJS.isSelected())
				format+=16;
			if (chkFormatSansQF.isSelected())
				format+=32;
			
			Logging.logger_.info("Lancement");
		    
			try {
				if (generateur.compareTo(ExtractionMain.GENERATEUR_XLS) == 0)
				{
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fcSortie.getSelectedFile()), ENCODING_WINDOWS));

					ExtractionAdherents app = new ExtractionAdherents();
					login(app);
				    Logging.logger_.info("Extraction");
					String donnees = app.extract(structure,chkRecursif.isSelected(),type, chkAdherents.isSelected(), txfCodefonction.getText(),specialite,categorie,diplome,qualif,formation,format, true);
					logout();
					
					Logging.logger_.info("Ecriture du fichier \""+fcSortie.getSelectedFile().getAbsolutePath()+"\"");
					out.write(donnees);
					out.flush();
					out.close();
				}
				else
				if (generateur.compareTo(ExtractionMain.GENERATEUR_XML) == 0)
				{
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fcSortie.getSelectedFile()), ENCODING_UTF8));

					ExtractionAdherents app = new ExtractionAdherents();
					login(app);
				    Logging.logger_.info("Extraction");
					String donnees = app.extract(structure,chkRecursif.isSelected(),type, chkAdherents.isSelected(), txfCodefonction.getText(),specialite,categorie,diplome,qualif,formation,format, false);
					logout();

					Logging.logger_.info("Ecriture du fichier \""+fcSortie.getSelectedFile().getAbsolutePath()+"\"");
					out.write(donnees);
					out.flush();
					out.close();
				}
				else
				if (generateur.compareTo(ExtractionMain.GENERATEUR_CSV) == 0)
				{
					final CSVPrinter out = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(fcSortie.getSelectedFile(), Charset.forName(ENCODING_WINDOWS));
					
					ExtractionAdherents app = new ExtractionAdherents();
					login(app);
				    Logging.logger_.info("Extraction");
					String donnees = app.extract(structure,chkRecursif.isSelected(),type, chkAdherents.isSelected(), txfCodefonction.getText(),specialite,categorie,diplome,qualif,formation,format, false);
					logout();
					
					XPathFactory xpfac = XPathFactory.instance();
					SAXBuilder builder = new SAXBuilder();
			        org.jdom2.Document docx = builder.build(new ByteArrayInputStream(donnees.getBytes(Charset.forName(ENCODING_UTF8))));
			        
			        // Scan des colonnes
			     	XPathExpression<?> xpac = xpfac.compile("tbody/tr[1]/td/text()");
			     	List<?> resultsc = xpac.evaluate(docx);
			     	int nbColumns = resultsc.size();	 
			     			 
			        XPathExpression<?> xpa = xpfac.compile("tbody/tr/td");
			        
			        List<?> results = xpa.evaluate(docx);

					Logging.logger_.info("Ecriture du fichier \""+fcSortie.getSelectedFile().getAbsolutePath()+"\"");
			        int index = 0;
					Iterator<?> iter = results.iterator();
					while (iter.hasNext())
					{
						Object result = iter.next();
						Element resultElement = (Element) result;
						out.print(resultElement.getText());
		                index++;
			        	if (index % nbColumns == 0)
			        	{
			        		out.println();
			        	}
					}
					out.flush();
					out.close();
				}
				
			} catch (IOException|JDOMException e) {
				logger_.error(Logging.dumpStack(null,e));
			}
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
	
	public JLabel getLblSortie() {
		return lblSortie;
	}
	public JTextArea getTxtLog() {
		return txtLog;
	}
	public JTextField getTxfIdentifiant() {
		return txfIdentifiant;
	}
	public JPasswordField getTxfMotdepasse() {
		return txfMotdepasse;
	}
	public JTextField getTxfStructure() {
		return txfStructure;
	}
	public JTextField getTxfCodefonction() {
		return txfCodefonction;
	}
	public JCheckBox getChkFormatIndividu() {
		return chkFormatIndividu;
	}
	public JCheckBox getChkFormatParents() {
		return chkFormatParents;
	}
	public JCheckBox getChkFormatInscription() {
		return chkFormatInscription;
	}
	public JCheckBox getChkFormatAdhesion() {
		return chkFormatAdhesion;
	}
	public JCheckBox getChkFormatJS() {
		return chkFormatJS;
	}
	public JCheckBox getChkFormatSansQF() {
		return chkFormatSansQF;
	}
	public JComboBox getCbxTypeinscription() {
		return cbxTypeinscription;
	}
	public JComboBox getCbxType() {
		return cbxType;
	}
	public JComboBox getCbxCategorie() {
		return cbxCategorie;
	}
	public JComboBox getCbxSpecialite() {
		return cbxSpecialite;
	}
	public JCheckBox getChkAdherents() {
		return chkAdherents;
	}
	public JComboBox getCbxFormation() {
		return cbxFormation;
	}
	public JComboBox getCbxQualification() {
		return cbxQualification;
	}
	public JComboBox getCbxDiplome() {
		return cbxDiplome;
	}
	public JCheckBox getChkRecursif() {
		return chkRecursif;
	}
}
