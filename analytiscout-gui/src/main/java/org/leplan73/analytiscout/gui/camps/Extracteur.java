package org.leplan73.analytiscout.gui.camps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.leplan73.analytiscout.Consts;
import org.leplan73.analytiscout.Params;
import org.leplan73.analytiscout.Progress;
import org.leplan73.analytiscout.engine.EngineExtracteurCamps;
import org.leplan73.analytiscout.gui.GuiProgress;
import org.leplan73.analytiscout.gui.utils.Appender;
import org.leplan73.analytiscout.gui.utils.BoutonOuvrir;
import org.leplan73.analytiscout.gui.utils.Dialogue;
import org.leplan73.analytiscout.gui.utils.ExportFileFilter;
import org.leplan73.analytiscout.gui.utils.GuiCommand;
import org.leplan73.analytiscout.gui.utils.LoggedDialog;
import org.leplan73.analytiscout.gui.utils.Logging;
import org.leplan73.analytiscout.gui.utils.Preferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class Extracteur extends Dialogue implements LoggedDialog, GuiCommand {

	private final JPanel contentPanel = new JPanel();

	private JTextField txfCodeStructure;
	private Logger logger_ = LoggerFactory.getLogger(Extracteur.class);
	private JFileChooser fcSortie;
	private File fSortie = new File(Preferences.lit(Consts.REPERTOIRE_SORTIE, "données", false),"camps.xls");
	private JLabel lblSortie;
	private UtilDateModel debutModel_ = new UtilDateModel(Params.getDateDebutSaison());
	private UtilDateModel finModel_ = new UtilDateModel(Params.getDateFinSaison());
	private BoutonOuvrir btnOuvrir;

	/**
	 * Create the dialog.
	 */
	public Extracteur() {
		super();
		setTitle("Extracteur des camps");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		Appender.setLoggedDialog(this);

		double x = Preferences.litd(Consts.FENETRE_GENERATEUR_X, 100);
		double y = Preferences.litd(Consts.FENETRE_GENERATEUR_Y, 100);
		setBounds((int)x, (int)y, 580, 627);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Code structure", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
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
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Dates", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			contentPanel.add(panel, gbc_panel);
			panel.setLayout(new BorderLayout(0, 0));
			
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.WEST);
				{
					JLabel lblNewLabel = new JLabel("Début :");
					panel_1.add(lblNewLabel);
					
					JDatePanelImpl datePanel = new JDatePanelImpl(debutModel_, new Properties());
					JDatePickerImpl debutDatePicker = new JDatePickerImpl(datePanel, new FormatteurDate());
					panel_1.add(debutDatePicker);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.EAST);
				{
					JLabel lblNewLabel_1 = new JLabel("Fin :");
					panel_1.add(lblNewLabel_1);
					
					JDatePanelImpl datePanel = new JDatePanelImpl(finModel_, new Properties());
					JDatePickerImpl finDatePicker = new JDatePickerImpl(datePanel, new FormatteurDate());
					panel_1.add(finDatePicker);
				}
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
					JButton button = new JButton("Fichier...");
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							fcSortie = new JFileChooser();
							fcSortie.setDialogTitle("Export");
							fcSortie.setApproveButtonText("Export");
							fcSortie.setCurrentDirectory(fSortie);
							fcSortie.setSelectedFile(fSortie);
							fcSortie.setFileSelectionMode(JFileChooser.FILES_ONLY);
							fcSortie.removeChoosableFileFilter(fcSortie.getFileFilter());
							fcSortie.removeChoosableFileFilter(fcSortie.getAcceptAllFileFilter());
							fcSortie.addChoosableFileFilter(new ExportFileFilter("xls"));
							int result = fcSortie.showDialog(panel, "OK");
							if (result == JFileChooser.APPROVE_OPTION) {
								fSortie = ajouteExtensionFichier(fcSortie, lblSortie, "xls");
							}
						}
					});
					panel_1.add(button);
				}
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
				JPanel panel = new JPanel();
				buttonPane.add(panel, BorderLayout.EAST);
				{
					JButton button = new JButton("Go");
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							go();
						}
					});
					panel.add(button);
				}
				{
					JButton btnFermer = new JButton("Fermer");
					btnFermer.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					panel.add(btnFermer);
				}
			}
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel, BorderLayout.WEST);
				{
					JButton button = new JButton("Aide");
					button.setEnabled(false);
					panel.add(button);
				}
			}
		}
	}
	
	public boolean check(JTextField txfStructure) {
		if (check() == false) return false;
		structure_ = txfCodeStructure.getText();
		return checkIntranet();
	}

	@Override
	public boolean check() {
		logger_.info("Vérification des paramètres");
		Date debut = debutModel_.getValue();
		Date fin = finModel_.getValue();
		if (fin.getTime() <= debut.getTime()) {
			logger_.error("La date de début doit être inférieure à la date de fin");
			return false;
		}
		if (fSortie == null) {
			logger_.error("Le répertoire de sortie est non-sélectionnée");
			return false;
		}
		return true;
	}

	@Override
	public void go() {
		ProgressMonitor guiprogress = new ProgressMonitor(this, "Generateur", "", 0, 100);
		
		Progress progress = new GuiProgress(guiprogress, this.getTitle());
		progress.setMillisToPopup(0);
		progress.setMillisToDecideToPopup(0);
		
		new Thread(() -> {
			initLog();
			boolean ret = check(txfCodeStructure);
			if (ret) {
				try {
					int structures[] = construitStructures();
					Date debut = debutModel_.getValue();
					Date fin = finModel_.getValue();
					EngineExtracteurCamps en = new EngineExtracteurCamps(progress, logger_);
					en.go(identifiant_, motdepasse_, fSortie, structures[0], debut, fin);
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
		Preferences.sauved(Consts.FENETRE_GENERATEUR_X, this.getLocation().getX());
		Preferences.sauved(Consts.FENETRE_GENERATEUR_Y, this.getLocation().getY());
		Preferences.sauve(Consts.REPERTOIRE_SORTIE, this.fSortie.getParent(), false);
		super.dispose();
	}
	public JLabel getLblSortie() {
		return lblSortie;
	}
}
