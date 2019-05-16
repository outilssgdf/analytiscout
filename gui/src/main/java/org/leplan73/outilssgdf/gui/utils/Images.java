package org.leplan73.outilssgdf.gui.utils;

import java.awt.Image;
import java.awt.Toolkit;

public class Images {
	public final static String ROOT_FOLDER = "org/leplan73/outilssgdf/gui/resources";

	public static Image getIcon() {
		return Toolkit.getDefaultToolkit()
				.getImage(Images.class.getClassLoader().getResource(ROOT_FOLDER + "/icone.png"));
	}
}
