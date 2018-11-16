package org.leplan73.outilssgdf.calcul;

import java.time.Instant;

public class General {
	
	private Instant now = Instant.now();
	private String version_;
	
	public General(String version)
	{
		version_ = version;
	}
	
	public String getDategeneration()
	{
		return now.toString();
	}
	
	public String getVersion()
	{
		return version_;
	}
}
