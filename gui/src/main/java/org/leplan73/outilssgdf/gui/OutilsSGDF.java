package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.Params;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;

import com.jcabi.manifests.Manifests;

import net.sf.jett.transform.ExcelTransformer;
import java.awt.FlowLayout;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;

public class OutilsSGDF extends JFrame {

	private JPanel contentPane;
	private JTextField txfStructure;

	private JFileChooser batchFileChooser;
	private JFileChooser entreeFileChooser;
	private JFileChooser modeleFileChooser;
	private JFileChooser sortieFileChooser;
	private JCheckBox chckbxNewCheckBox;
	private JTextField textField;
	private JTextField textField_1;
	
	
	
	
	static private OutilsSGDF frame_;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Logging.initLogger(OutilsSGDF.class, true);
		try {
//			UIManager.setLookAndFeel(useopenjdk ? "javax.swing.plaf.nimbus.NimbusLookAndFeel" : "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e1) {
		} catch (InstantiationException e1) {
		} catch (IllegalAccessException e1) {
		} catch (UnsupportedLookAndFeelException e1) {
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame_ = new OutilsSGDF();
					frame_.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OutilsSGDF() {
		setTitle("Outils SGDF v"+Manifests.read("version"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 569);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);
		
		JPanel panel_11 = new JPanel();
		tabbedPane.addTab("Extracteur", null, panel_11, null);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[]{0, 0};
		gbl_panel_11.rowHeights = new int[]{0, 0, 297, 0, 0};
		gbl_panel_11.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_11.rowWeights = new double[]{1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_11.setLayout(gbl_panel_11);
		
		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new TitledBorder(null, "Identifiants Intranet", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_13 = new GridBagConstraints();
		gbc_panel_13.insets = new Insets(0, 0, 5, 0);
		gbc_panel_13.fill = GridBagConstraints.BOTH;
		gbc_panel_13.gridx = 0;
		gbc_panel_13.gridy = 0;
		panel_11.add(panel_13, gbc_panel_13);
		panel_13.setLayout(new BoxLayout(panel_13, BoxLayout.X_AXIS));
		
		JPanel panel_14 = new JPanel();
		panel_13.add(panel_14);
		panel_14.setLayout(new BoxLayout(panel_14, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("Identifiant : ");
		panel_14.add(lblNewLabel_1);
		
		textField = new JTextField();
		panel_14.add(textField);
		textField.setColumns(25);
		
		JPanel panel_15 = new JPanel();
		panel_13.add(panel_15);
		panel_15.setLayout(new BoxLayout(panel_15, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_2 = new JLabel("Mot de passe :");
		panel_15.add(lblNewLabel_2);
		
		textField_1 = new JTextField();
		panel_15.add(textField_1);
		textField_1.setColumns(25);
		
		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new TitledBorder(null, "Filtrage", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_16 = new GridBagConstraints();
		gbc_panel_16.insets = new Insets(0, 0, 5, 0);
		gbc_panel_16.fill = GridBagConstraints.BOTH;
		gbc_panel_16.gridx = 0;
		gbc_panel_16.gridy = 1;
		panel_11.add(panel_16, gbc_panel_16);
		
		JPanel panel_17 = new JPanel();
		panel_17.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fichier de sortie", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_17 = new GridBagConstraints();
		gbc_panel_17.insets = new Insets(0, 0, 5, 0);
		gbc_panel_17.fill = GridBagConstraints.BOTH;
		gbc_panel_17.gridx = 0;
		gbc_panel_17.gridy = 2;
		panel_11.add(panel_17, gbc_panel_17);
		panel_17.setLayout(new BorderLayout(0, 0));
		
		JLabel label_1 = new JLabel("<rien>");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_17.add(label_1, BorderLayout.WEST);
		
		JButton button_2 = new JButton("Fichier...");
		panel_17.add(button_2, BorderLayout.EAST);
		
		JPanel panel_12 = new JPanel();
		GridBagConstraints gbc_panel_12 = new GridBagConstraints();
		gbc_panel_12.fill = GridBagConstraints.BOTH;
		gbc_panel_12.gridx = 0;
		gbc_panel_12.gridy = 3;
		panel_11.add(panel_12, gbc_panel_12);
		panel_12.setLayout(new BorderLayout(0, 0));
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setStringPainted(true);
		panel_12.add(progressBar_1, BorderLayout.CENTER);
		
		JButton button = new JButton("Go");
		panel_12.add(button, BorderLayout.EAST);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Analyseur", null, panel_3, null);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{536, 0};
		gbl_panel_3.rowHeights = new int[]{0, 33, 59, 33, 41, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(null, "Fichier batch", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.insets = new Insets(0, 0, 5, 0);
		gbc_panel_10.fill = GridBagConstraints.BOTH;
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 0;
		panel_3.add(panel_10, gbc_panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		JLabel lblFichierBatch = new JLabel("<rien>");
		panel_10.add(lblFichierBatch, BorderLayout.WEST);
		
		JButton btnNewButton_1 = new JButton("Fichier...");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				javax.swing.Action details = batchFileChooser.getActionMap().get("viewTypeList");
			 	details.actionPerformed(null);
				int result = batchFileChooser.showDialog(null, "Fichier batch");  
				if (result == JFileChooser.APPROVE_OPTION) { 
					File targetFile = batchFileChooser.getSelectedFile();
					lblFichierBatch.setText(targetFile.getPath());
				}
			}
		});
		panel_10.add(btnNewButton_1, BorderLayout.EAST);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "R\u00E9pertoire d'entr\u00E9e", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_4.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 1;
		panel_3.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRepertoireDentree = new JLabel("<rien>");
		panel_4.add(lblRepertoireDentree, BorderLayout.WEST);
		lblRepertoireDentree.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnRpertoire = new JButton("Répertoire...");
		btnRpertoire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				javax.swing.Action details = entreeFileChooser.getActionMap().get("viewTypeList");
			 	details.actionPerformed(null);
				int result = entreeFileChooser.showDialog(null, "Fichier d'entrée");  
				if (result == JFileChooser.APPROVE_OPTION) { 
					File targetFile = entreeFileChooser.getSelectedFile();
					lblRepertoireDentree.setText(targetFile.getPath());
				}
			}
		});
		panel_4.add(btnRpertoire, BorderLayout.EAST);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fichier de mod\u00E8le", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.insets = new Insets(0, 0, 5, 0);
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 2;
		panel_3.add(panel_5, gbc_panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JLabel lblFichierModele = new JLabel("<rien>");
		panel_5.add(lblFichierModele, BorderLayout.WEST);
		lblFichierModele.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton button_1 = new JButton("Fichier...");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					javax.swing.Action details = modeleFileChooser.getActionMap().get("viewTypeList");
				 	details.actionPerformed(null);
					int result = modeleFileChooser.showDialog(null, "Fichier modèle");  
					if (result == JFileChooser.APPROVE_OPTION) { 
						File targetFile = modeleFileChooser.getSelectedFile();
						lblFichierModele.setText(targetFile.getPath());
					}
			}
		});
		panel_5.add(button_1, BorderLayout.EAST);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.insets = new Insets(0, 0, 5, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 3;
		panel_3.add(panel_6, gbc_panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Structure");
		panel_6.add(lblNewLabel, BorderLayout.WEST);
		
		txfStructure = new JTextField();
		panel_6.add(txfStructure);
		txfStructure.setColumns(60);
		txfStructure.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "Fichier de sortie", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_7.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_7.insets = new Insets(0, 0, 5, 0);
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 4;
		panel_3.add(panel_7, gbc_panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JLabel label = new JLabel("<rien>");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel_7.add(label, BorderLayout.WEST);
		
		JButton button_3 = new JButton("Fichier...");
		panel_7.add(button_3, BorderLayout.EAST);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.anchor = GridBagConstraints.WEST;
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.VERTICAL;
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 5;
		panel_3.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[]{256, 0};
		gbl_panel_8.rowHeights = new int[]{23, 0};
		gbl_panel_8.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_8.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_8.setLayout(gbl_panel_8);
		
		chckbxNewCheckBox = new JCheckBox("Gestion de l'âge");
		GridBagConstraints gbc_chckbxAge = new GridBagConstraints();
		gbc_chckbxAge.anchor = GridBagConstraints.WEST;
		gbc_chckbxAge.gridx = 0;
		gbc_chckbxAge.gridy = 0;
		panel_8.add(chckbxNewCheckBox, gbc_chckbxAge);
		
		JPanel panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 6;
		panel_3.add(panel_9, gbc_panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		JProgressBar progressBar = new JProgressBar();
		panel_9.add(progressBar, BorderLayout.CENTER);
		progressBar.setStringPainted(true);
		
		JButton btnNewButton = new JButton("Go");
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				boolean ret = checkParams(tabbedPane.getSelectedIndex());
				if (ret == true)
				{
					SwingWorker sw = new SwingWorker<String, Void>() {
						@Override
						protected String doInBackground() throws Exception {
							go(tabbedPane.getSelectedIndex());
							return null;
						}
						
					};
					sw.execute();
				}
		}});
		panel_9.add(btnNewButton, BorderLayout.EAST);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(panel_2, BorderLayout.CENTER);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{552, 0};
		gbl_panel_2.rowHeights = new int[]{115, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panel_2.add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		entreeFileChooser	 = new JFileChooser();	
		entreeFileChooser.setDialogTitle("Entrée");
		entreeFileChooser.setApproveButtonText("Sélection");
		entreeFileChooser.setCurrentDirectory(new File(".")); //default
		entreeFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		batchFileChooser	 = new JFileChooser();	
		batchFileChooser.setDialogTitle("Batch");
		batchFileChooser.setApproveButtonText("Sélection");
		batchFileChooser.setCurrentDirectory(new File("./conf")); //default
		batchFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		modeleFileChooser	 = new JFileChooser();	
		modeleFileChooser.setDialogTitle("Modèle");
		modeleFileChooser.setApproveButtonText("Sélection");
		modeleFileChooser.setCurrentDirectory(new File("./conf")); //default
		modeleFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		modeleFileChooser.removeChoosableFileFilter(modeleFileChooser.getFileFilter() );
		modeleFileChooser.addChoosableFileFilter(new ExcelFileFilter());
		
		sortieFileChooser	 = new JFileChooser();	
		sortieFileChooser.setDialogTitle("Sortie");
		sortieFileChooser.setApproveButtonText("Sélection");
		sortieFileChooser.setCurrentDirectory(new File(".")); //default
		sortieFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		sortieFileChooser.removeChoosableFileFilter(sortieFileChooser.getFileFilter() );
		sortieFileChooser.addChoosableFileFilter(new ExcelFileFilter());
	}
	
	private boolean checkParams(int index)
	{
	    Logging.logger_.info("Vérification des paramètres");
		if (index == 0)
		{
			if (txfStructure.getText().length() == 0)
			{
				JOptionPane.showMessageDialog(this, "Structure est vide", "Erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (batchFileChooser.getSelectedFile() == null)
			{
				JOptionPane.showMessageDialog(this, "Pas de fichier batch", "Erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (entreeFileChooser.getSelectedFile() == null)
			{
				JOptionPane.showMessageDialog(this, "Pas de répertoire d'entrée", "Erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (modeleFileChooser.getSelectedFile() == null)
			{
				JOptionPane.showMessageDialog(this, "Pas de fichier modèle", "Erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (sortieFileChooser.getSelectedFile() == null)
			{
				JOptionPane.showMessageDialog(this, "Pas de fichier de sortie", "Erreur", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	private void go(int index)
	{
		if (index == 0)
		{
			try {
				Instant now = Instant.now();
				
				Logging.logger_.info("Lancement");
	
				Logging.logger_.info("Chargement du fichier de paramètres");
				Params.init("./conf/params.properties");
			    
			    Logging.logger_.info("Chargement du fichier de traitement");
				
				Properties pbatch = new Properties();
				pbatch.load(new FileInputStream(batchFileChooser.getSelectedFile()));
	
				Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
				File fichierAdherents = null;
	
				File dossierStructure = new File(entreeFileChooser.getSelectedFile(),""+txfStructure.getText());
				dossierStructure.exists();
				
				int indexBatch=1;
				for(;;)
				{
					String generateur = pbatch.getProperty("generateur."+indexBatch);
					if (generateur == null)
					{
						break;
					}
					
					ExtraKey extra = new ExtraKey(pbatch.getProperty("nom."+indexBatch,""), pbatch.getProperty("batchtype."+indexBatch,"tout"));
					File fichier = new File(dossierStructure, extra.nom_+"."+generateur);
					
				    Logging.logger_.info("Chargement du fichier \""+fichier.getName()+"\"");
				    
					if (extra.ifTout())
					{
						fichierAdherents = fichier;
					} else
							extraMap.put(extra, new ExtracteurExtraHtml(fichier.getAbsolutePath(),chckbxNewCheckBox.isSelected()));
				indexBatch++;
			}

			Logging.logger_.info("Chargement du fichier \""+fichierAdherents.getName()+"\"");
			ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, extraMap,chckbxNewCheckBox.isSelected());
			 
			AdherentFormes compas = new AdherentFormes();
			compas.charge(adherents,extraMap);
			
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

			FileOutputStream outputStream = new FileOutputStream(sortieFileChooser.getSelectedFile());

		    Logging.logger_.info("Génération du fichier \""+sortieFileChooser.getSelectedFile().getName()+"\" à partir du modèle \""+modeleFileChooser.getSelectedFile().getName()+"\"");
			ExcelTransformer trans = new ExcelTransformer();
			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("chefs", adherents.getChefsList());
			beans.put("compas", adherents.getCompasList());
			beans.put("unites", adherents.getUnitesList());
			beans.put("general", general);
			beans.put("global", global);
			Workbook workbook = trans.transform(new FileInputStream(modeleFileChooser.getSelectedFile()), beans);
			workbook.write(outputStream);
			
			outputStream.flush();
			outputStream.close();
			
			long d = Instant.now().getEpochSecond() - now.getEpochSecond();
			Logging.logger_.info("Terminé en "+d+" seconds");
			} catch (ExtractionException e) {
			} catch (IOException e) {
			} catch (JDOMException e) {
			} catch (InvalidFormatException e) {
			}
		}
	}

}