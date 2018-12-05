package org.leplan73.outilssgdf.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jdom2.JDOMException;
import org.leplan73.outilssgdf.ExtracteurExtraHtml;
import org.leplan73.outilssgdf.ExtracteurHtml;
import org.leplan73.outilssgdf.ExtractionException;
import org.leplan73.outilssgdf.calcul.General;
import org.leplan73.outilssgdf.calcul.Global;
import org.leplan73.outilssgdf.extraction.AdherentForme.ExtraKey;
import org.leplan73.outilssgdf.extraction.AdherentFormes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.manifests.Manifests;

import net.sf.jett.transform.ExcelTransformer;
import javax.swing.JScrollPane;

public class Analyseur extends JDialog implements LoggedDialog,GuiCommand {

	private final JPanel contentPanel = new JPanel();
	private Logger logger_ = LoggerFactory.getLogger(Analyseur.class);
	private JFileChooser fcBatch;
	private JFileChooser fcEntree;
	private JFileChooser fcModele;
	private JFileChooser fcSortie;
	private JTextField txfStructure;
	private JCheckBox chcAge;
	private JLabel lblSortie;
	private JLabel lblBatch;
	private JLabel lblEntree;
	private JLabel lblModele;
	private JTextArea txtLog;
	private JProgressBar progress;
	private JButton btnOk;
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
		setBounds(100, 100, 556, 608);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{211, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Batch", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
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
						fcBatch.removeChoosableFileFilter(fcSortie.getFileFilter() );
						fcBatch.addChoosableFileFilter(new ExportFileFilter("txt"));
						int result = fcBatch.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {  
							File targetFile = fcBatch.getSelectedFile();
							lblBatch.setText(targetFile.getPath());
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
				lblEntree = new JLabel("<rien>");
				panel.add(lblEntree, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcEntree = new JFileChooser();
						fcEntree.setDialogTitle("Répertoire de données");
						fcEntree.setApproveButtonText("Go");
						fcEntree.setCurrentDirectory(new File("."));
						fcEntree.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int result = fcEntree.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {  
							File targetFile = fcBatch.getSelectedFile();
							lblEntree.setText(targetFile.getPath());
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
			gbc_panel.gridy = 2;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblModele = new JLabel("<rien>");
				panel.add(lblModele, BorderLayout.WEST);
			}
			{
				JButton button = new JButton("Fichier...");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fcModele = new JFileChooser();
						fcModele.setDialogTitle("Fichier modèle");
						fcModele.setApproveButtonText("Go");
						fcModele.setCurrentDirectory(new File("."));
						fcModele.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fcModele.removeChoosableFileFilter(fcSortie.getFileFilter() );
						fcModele.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcModele.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {  
							File targetFile = fcModele.getSelectedFile();
							lblModele.setText(targetFile.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
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
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				txfStructure = new JTextField();
				panel.add(txfStructure);
				txfStructure.setColumns(25);
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
			gbc_panel.gridy = 4;
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
			gbc_panel.gridy = 5;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				lblSortie = new JLabel("<rien>");
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
						fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter() );
						fcSortie.addChoosableFileFilter(new ExportFileFilter("xlsx"));
						int result = fcSortie.showDialog(panel, "OK");
						if (result == JFileChooser.APPROVE_OPTION) {  
							File targetFile = fcSortie.getSelectedFile();
							lblSortie.setText(targetFile.getPath());
						}
					}
				});
				panel.add(button, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 6;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				progress = new JProgressBar();
				progress.setStringPainted(true);
				panel.add(progress, BorderLayout.CENTER);
			}
			{
				btnGo = new JButton("Go");
				btnGo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						go();
					}
				});
				panel.add(btnGo, BorderLayout.EAST);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 7;
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
				btnOk = new JButton("OK");
				btnOk.setActionCommand("OK");
				buttonPane.add(btnOk);
				getRootPane().setDefaultButton(btnOk);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setEnabled(false);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	@Override
	public boolean check()
	{
		logger_.info("Vérification des paramètres");
		if (fcBatch==null)
		{
			logger_.error("Batch non-sélectionnée");
			return false;
		}
		if (fcEntree==null)
		{
			logger_.error("Entrée non-sélectionnée");
			return false;
		}
		if (fcModele==null)
		{
			logger_.error("Modèle non-sélectionnée");
			return false;
		}
		if (fcSortie==null)
		{
			logger_.error("Sortie non-sélectionnée");
			return false;
		}
		if (txfStructure.getText().isEmpty())
		{
			logger_.error("Structure est vide");
			return false;
		}
		return true;
	}
	
	@Override
	public void go()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progress.setValue(0);
				txtLog.setText("");
				btnGo.setEnabled(false);
				btnOk.setEnabled(false);
				
				Instant now = Instant.now();
				
				boolean ret = check();
				progress.setValue(20);
				if (ret)
				{
					logger_.info("Lancement");
				    
					try {
					    logger_.info("Chargement du fichier de traitement");
						
						Properties pbatch = new Properties();
						pbatch.load(new FileInputStream(fcBatch.getSelectedFile()));
				
						Map<ExtraKey, ExtracteurExtraHtml> extraMap = new TreeMap<ExtraKey, ExtracteurExtraHtml>();
						File fichierAdherents = null;
				
						String structure = getTxfStructure().getText();
						File dossierStructure = new File(fcEntree.getSelectedFile(),""+structure);
						dossierStructure.exists();
						
						int index=1;
						for(;;)
						{
							String generateur = pbatch.getProperty("generateur."+index);
							if (generateur == null)
							{
								break;
							}
							
							ExtraKey extra = new ExtraKey(pbatch.getProperty("nom."+index,""), pbatch.getProperty("batchtype."+index,"tout"));
							File fichier = new File(dossierStructure, extra.nom_+"."+generateur);
							
						    logger_.info("Chargement du fichier \""+fichier.getName()+"\"");
						    
							if (extra.ifTout())
							{
								fichierAdherents = fichier;
							}
							else
								extraMap.put(extra, new ExtracteurExtraHtml(fichier.getAbsolutePath(),getChcAge().isSelected()));
							index++;
						}
				
						logger_.info("Chargement du fichier \""+fichierAdherents.getName()+"\"");
						ExtracteurHtml adherents = new ExtracteurHtml(fichierAdherents, extraMap,getChcAge().isSelected());
						 
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
				
						FileOutputStream outputStream = new FileOutputStream(fcSortie.getSelectedFile());
				
					    logger_.info("Génération du fichier \""+fcSortie.getSelectedFile().getName()+"\" à partir du modèle \""+fcModele.getSelectedFile().getName()+"\"");
						ExcelTransformer trans = new ExcelTransformer();
						Map<String, Object> beans = new HashMap<String, Object>();
						beans.put("chefs", adherents.getChefsList());
						beans.put("compas", adherents.getCompasList());
						beans.put("unites", adherents.getUnitesList());
						beans.put("general", general);
						beans.put("global", global);
						Workbook workbook = trans.transform(new FileInputStream(fcModele.getSelectedFile()), beans);
						workbook.write(outputStream);
						
						outputStream.flush();
						outputStream.close();
						
					} catch (IOException|JDOMException | InvalidFormatException | ExtractionException e) {
						logger_.error(Logging.dumpStack(null,e));
					}
				}
				progress.setValue(100);
				long d = Instant.now().getEpochSecond() - now.getEpochSecond();
				logger_.info("Terminé en "+d+" seconds");
				
				btnGo.setEnabled(true);
				btnOk.setEnabled(true);
			}
		});
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
	
	public JTextField getTxfStructure() {
		return txfStructure;
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
	public JProgressBar getProgress() {
		return progress;
	}
	public JButton getBtnOk() {
		return btnOk;
	}
	public JButton getBtnGo() {
		return btnGo;
	}
}
