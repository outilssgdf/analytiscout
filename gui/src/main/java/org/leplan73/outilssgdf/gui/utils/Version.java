package org.leplan73.outilssgdf.gui.utils;

public class Version {
	
	private boolean complete_;
	private int major_;
	private int minor_;
	private int subminor_;
	private String mode_;
	
	public int getMajor() { return major_; };
	public int getMinor() { return minor_; };
	public int getSubMinor() { return subminor_; };
	public String getMode() { return mode_; }
	
	@Override
	public String toString()
	{
		if (complete_)
			return major_+"."+minor_+"."+subminor_;
		return "0.0.0";
	}
	
	public static Version parse(final String st)
	{
		Version v = new Version();
		v.complete_ = false;
		String parts[] = st.split("\\.");
		if (parts.length == 3)
		{
			v.major_ = Integer.valueOf(parts[0]);
			v.minor_ = Integer.valueOf(parts[1]);
			
			String subminor = parts[2];
			String[] subparts = subminor.split("-");
			if (subparts.length >= 1)
			{
				v.subminor_ = Integer.valueOf(subparts[0]);
				if (subparts.length == 2)
				{
					v.mode_ = subparts[1];
				}
			}
			v.complete_ = true;
		}
		return v;
	}
}
