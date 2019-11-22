package org.leplan73.outilssgdf.calcul;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class General {
	
	private Date now = Date.from(Instant.now());
	private String version_;
	
	public General(String version)
	{
		version_ = version;
	}
	
	public String getDategeneration()
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(now);
	}
	
	public String getVersion()
	{
		return version_;
	}
}
