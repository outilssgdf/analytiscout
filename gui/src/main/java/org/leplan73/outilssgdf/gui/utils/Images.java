package org.leplan73.outilssgdf.gui.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.ImageIcon;

public class Images {
	public final static String ROOT_FOLDER = "org/leplan73/outilssgdf/gui";
	private final static Random random_ = new Random(5);

	public static Image getIcon() {
		return Toolkit.getDefaultToolkit()
				.getImage(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/icone.png"));
	}
	
	public static ImageIcon getIconCog() {
		return new ImageIcon(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/cog.png"));
	}
	
	public static ImageIcon getImage() {
		int random = random_.nextInt(5)+1;
		return new ImageIcon(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/panneau_gauche-"+random+".png"));
	}
}
