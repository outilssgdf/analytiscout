package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Export extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txfIdentifiant;
	private JPasswordField txfMotdepasse;
	private JTextField txfStructure;
	private JTextField txfCodefonction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Export dialog = new Export();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Export() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Export");
		setBounds(100, 100, 742, 666);
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
			gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
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
					JComboBox cbxDiplome = new JComboBox();
					cbxDiplome.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "2 Buchettes", "3 Buchettes", "4 Buchettes", "AFPS", "BAFA", "BAFA Qualification Voile", "BAFD", "BP JEPS", "Brevet d'état (activités marines)", "Brevet Professionnel (Skipper, marine marchande)", "Carte Mer", "Certificat Radio Restreint CRR", "Chef de Flottille", "Chef de quart", "DE JEPS", "DEUG STAPS", "Directeur nautique", "DUT animation socioculturelle ", "Licence STAPS", "Médaille Argent de la JS", "Médaille Bronze de la JS", "Médaille Or de la JS", "Nœud de tisserand", "Patron d'embarcation", "Permis Côtier", "Permis E", "Permis fluvial", "Permis Hauturier", "PSC1 Prévention et Secours Civiques de niveau 1", "Spirale AMGE", "Surveillant de Baignade"}));
					panel_1.add(cbxDiplome);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Type d'inscription", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.anchor = GridBagConstraints.WEST;
				gbc_panel_1.fill = GridBagConstraints.VERTICAL;
				gbc_panel_1.gridx = 1;
				gbc_panel_1.gridy = 0;
				panel.add(panel_1, gbc_panel_1);
				{
					JComboBox cbxTypeinscription = new JComboBox();
					cbxTypeinscription.setModel(new DefaultComboBoxModel(new String[] {"Toutes", "Inscrit(e)", "Invité(e)", "Pré-inscrit(e)"}));
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
					JComboBox cbxType = new JComboBox();
					cbxType.setModel(new DefaultComboBoxModel(new String[] {"tout", "inscrit", "invite", "pré-inscrit"}));
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
					JComboBox cbxQualification = new JComboBox();
					cbxQualification.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "Animateur SF (CAFASF)", "Directeur SF (CAFDSF)", "Responsable Unité SF (CAFRUSF)"}));
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
					JComboBox cbxSpecialite = new JComboBox();
					cbxSpecialite.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "Marine", "Sans spécialité", "Vent du Large"}));
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
					JComboBox cbxCategorie = new JComboBox();
					cbxCategorie.setModel(new DefaultComboBoxModel(new String[] {"Toutes", "Jeune", "Membre Associé", "Responsable"}));
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
					JComboBox cbxFormation = new JComboBox();
					cbxFormation.setModel(new DefaultComboBoxModel(new String[] {"Sans importance", "AFPS", "AIDE A LA PRISE DE FONCTION", "APF AUMONIER ET AVSC", "APF CHEFS - CHEFTAINES", "APF RESPONSABLE DE GROUPE", "APF RESPONSABLE LOCAL DEVELOPPEMENT ET RESEAUX", "APF SECRETAIRE TRESORIER", "APPRO", "APPRO ACCUEIL DE SCOUTISME", "APPRO ANIMATION", "APPRO KAYAK", "APPRO SURVEILLANT DE BAIGNADE", "APPRO TERRE ET MER", "ASSISES DES FORMATEURS", "AUTRE STAGE", "BAFA APPROFONDISSEMENT", "BAFA FORMATION GENERALE", "BAFD FORMATION GENERALE", "BAFD PERFECTIONNEMENT", "CEP 1", "CEP 2", "CHAM", "CHEF DE FLOTTILLE", "CHEF DE QUART", "FAC", "FIAC", "FIRPAF", "FIRPDEV", "FIRPP", "FORMATION A LA COMMUNICATION", "FORMATION ACCUEIL DE SCOUTISME RG", "FORMATION COMPAGNON 1ER TEMPS", "FORMATION COMPAGNON 2EME TEMPS", "FORMATION COMPLEMENTAIRE DE FORMATEUR", "FORMATION CONTINUE DE DIRECTEURS", "FORMATION CONTINUE DE FORMATEURS", "FORMATION CONTINUE DES RESPONSABLES DE GROUPE", "FORMATION CONTINUE VENT DU LARGE", "FORMATION DE DIRECTEUR DE STAGE", "FORMATION DEPART A L'ETRANGER", "FORMATION DES ACCOMPAGNATEURS COMPAGNONS", "FORMATION DES AUMONIERS ET ANIMATEURS CLEOPHAS ", "FORMATION DES RESPONSABLES FARFADETS", "FORMATION DEVELOPPEMENT ET RESEAUX", "FORMATION GENERALE RESPONSABLE DE GROUPE", "FORMATION INITIALE ACCOMPAGNATEURS PEDAGOGIQUES", "FORMATION INITIALE DE FORMATEURS", "FORMATION INITIALE DES AUMONIERS", "FORMATION INITIALE DES AVSC", "FORMATION INITIALE DES DELEGUES TERRITORIAUX", "FORMATION INITIALE DES EQUIPIERS NATIONAUX", "FORMATION INITIALE DES EQUIPIERS TERRITORIAUX", "FORMATION INITIALE DES MEDIATEURS", "FORMATION INITIALE DES POLES ADMIN ET FINANCIER", "FORMATION INITIALE DES POLES DEVELOPPEMENT", "FORMATION INITIALE DES RESPONSABLES DE GROUPE", "FORMATION INITIALE DES RESPONSABLES DE P&#212;LES", "FORMATION INITIALE TRESORIER-SECRETAIRE", "FORMATION INITIALE VENT DU LARGE", "FORMATION JEUNES ADULTES - DEPART A L'ETRANGER", "FORMATION SERVICE CIVIQUE", "FORMATION SPECIFIQUE MARINE", "MASTERCLASS", "MODULE ANIMATEUR DE SCOUTISME ET CAMPISME", "MODULE APPRO ACCUEIL DE SCOUTISME", "MODULE APPRO ANIMATION", "MODULE APPRO CONSTRUCTION-FABRICATION", "MODULE APPRO CUISINE EN CAMPS", "MODULE APPRO EXPRESSION ARTISTIQUE", "MODULE APPRO HANDICAP", "MODULE APPRO RENCONTRES INTERNATIONALES", "MODULE DE FORMATION CONTINUE DE FORMATEURS", "MODULE SPECIFIQUE  DE FORMATION", "PATRON D'EMBARCATION", "PSC1 ", "SEMINAIRE DE FORMATION DES RESPONSABLES DE GROUPE", "SEMINAIRE DELEGUES TERRITORIAUX", "SEMINAIRE DES POLES DEVELOPPEMENT", "SENAMCO 2", "STAF", "STAGE 2 QUALIFICATION VOILE", "STIF", "TECH", "VALIDATION MONITORAT VOILE 1ER DEGRE"}));
					panel_1.add(cbxFormation);
				}
			}
			{
				JCheckBox chkAdherents = new JCheckBox("Adhérents uniquement");
				GridBagConstraints gbc_chkAdherents = new GridBagConstraints();
				gbc_chkAdherents.insets = new Insets(0, 0, 0, 5);
				gbc_chkAdherents.anchor = GridBagConstraints.WEST;
				gbc_chkAdherents.gridx = 1;
				gbc_chkAdherents.gridy = 2;
				panel.add(chkAdherents, gbc_chkAdherents);
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
						JLabel lblSortie = new JLabel("<rien>");
						panel_2.add(lblSortie, BorderLayout.WEST);
					}
					{
						JButton button = new JButton("Fichier...");
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
						JComboBox cbxGenerateur = new JComboBox();
						cbxGenerateur.setModel(new DefaultComboBoxModel(new String[] {"format cvs", "format xls", "format xml"}));
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
							JCheckBox chkFormatIndividu = new JCheckBox("individu");
							panel_2_1.add(chkFormatIndividu);
						}
						{
							JCheckBox chkFormatParents = new JCheckBox("parents");
							panel_2_1.add(chkFormatParents);
						}
						{
							JCheckBox chkFormatInscription = new JCheckBox("inscription");
							panel_2_1.add(chkFormatInscription);
						}
						{
							JCheckBox chkFormatAdhesion = new JCheckBox("adhesion");
							panel_2_1.add(chkFormatAdhesion);
						}
						{
							JCheckBox chkFormatJS = new JCheckBox("JS");
							panel_2_1.add(chkFormatJS);
						}
						{
							JCheckBox chkFormatSansQF = new JCheckBox("sans QF");
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
				JTextArea textArea = new JTextArea();
				textArea.setEditable(false);
				panel.add(textArea);
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
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
