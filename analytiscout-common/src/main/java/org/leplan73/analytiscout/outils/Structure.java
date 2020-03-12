package org.leplan73.analytiscout.outils;

public class Structure {
	public static String formatStructure(int structure)
	{
		String s = String.valueOf(structure);
		while (s.length() < 9)
		{
			s = "0"+s;
		}
		return s;
	}
}
