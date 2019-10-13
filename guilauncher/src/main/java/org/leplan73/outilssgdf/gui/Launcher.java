package org.leplan73.outilssgdf.gui;

public class Launcher {
	public static void main(String[] args) {
		try {
			ClassLoader parentClassLoader = Launcher.class.getClassLoader();
			Class<?> myObjectClass = parentClassLoader.loadClass("org.leplan73.outilssgdf.gui.GuiCmd");
			GuiCmd o = (GuiCmd)myObjectClass.newInstance();
			o.go(args);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
