package org.leplan73.outilssgdf.gui.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

/**  Gui factory for WindowBuilder
 * @author tla
 *
 */
public final class ElementFactory {


	
    /**
    * @wbp.factory
    * @wbp.factory.parameter.source title "myTitle"
    * @wbp.factory.parameter.source help "Help of my element"
    */
	public static JPanel createActionTitleWithHelp(String title, String help) {

		JPanel panelTitleAndHelp = new JPanel();
		panelTitleAndHelp.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panelTitleAndHelp.add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {0, 0};
		gbl_panel_1.rowHeights = new int[] {0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 1.0};
		panel_1.setLayout(gbl_panel_1);
		
		JPanel panel_title_1 = ElementFactory.createActionTitle(title);
		GridBagConstraints gbc_panel_title_1 = new GridBagConstraints();
		gbc_panel_title_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_title_1.gridwidth = 0;
		gbc_panel_title_1.fill = GridBagConstraints.BOTH;
		gbc_panel_title_1.gridx = 0;
		gbc_panel_title_1.gridy = 0;
		panel_1.add(panel_title_1, gbc_panel_title_1);
		
		JLabel lblNewLabel = new JLabel(help);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblNewLabel.insets = new Insets(0, 20, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);
        
        
 
        return panelTitleAndHelp;		
		
	}	
	
    /**
    * @wbp.factory
    * @wbp.factory.parameter.source labeltext "myTitle"
    */
	public static JPanel createActionTitle(String labeltext) {

        JPanel jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        gridbagconstraints.gridx = -1;
        gridbagconstraints.gridy = 0;
        gridbagconstraints.anchor = 10;
        if(labeltext.trim().length() > 0)
        {
            JLabel jlabel = new JLabel(labeltext) ;
             jlabel.setFont(UIManager.getFont("TitledBorder.font"));
             jlabel.setForeground(UIManager.getColor("TitledBorder.titleColor"));
            jpanel.add(jlabel, gridbagconstraints);
            gridbagconstraints.insets.left = 5;
        }

        
        
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.fill = 2;
        JSeparator separator = new JSeparator(0);
        jpanel.add(separator, gridbagconstraints);
        return jpanel;		
		
	}

    /**
    * @wbp.factory
    * @wbp.factory.parameter.source source JLabel 
    * @wbp.factory.parameter.source labeltext "myTitle"
    */
	public static JPanel createActionTitleWithLabel(JLabel jlabel, String title) {

        JPanel jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        gridbagconstraints.gridx = -1;
        gridbagconstraints.gridy = 0;
        gridbagconstraints.anchor = 10;
        if(title.trim().length() > 0)
        {
        	jlabel.setText(title);
             jlabel.setFont(UIManager.getFont("TitledBorder.font"));
             jlabel.setForeground(UIManager.getColor("TitledBorder.titleColor"));
            jpanel.add(jlabel, gridbagconstraints);
            gridbagconstraints.insets.left = 5;
        }

        
        
        gridbagconstraints.weightx = 1.0D;
        gridbagconstraints.fill = 2;
        JSeparator separator = new JSeparator(0);
        jpanel.add(separator, gridbagconstraints);
        return jpanel;		
		
	}
	
	/**
	 * @wbp.factory
	 * @wbp.factory.parameter.source comp lblNewLabel
	 */
	public static JPanel createJPanel(Component comp) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(comp, BorderLayout.NORTH);
		return panel;
	}
	
	
}