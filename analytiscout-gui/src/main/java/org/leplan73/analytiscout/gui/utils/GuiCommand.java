package org.leplan73.analytiscout.gui.utils;

public interface GuiCommand {
	public void go();
	public boolean check();
	
	static public int extract(String key)
	{
		int value = -1;
		
		int i = key.lastIndexOf("(");
		int j = key.lastIndexOf(")");
		if (i > -1 && j > -1)
		{
			String v = key.substring(i+1, j);
			return Integer.valueOf(v);
		}
		return value;
	}

}
