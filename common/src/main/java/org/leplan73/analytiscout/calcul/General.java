package org.leplan73.analytiscout.calcul;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.jcabi.manifests.Manifests;

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
	
	static public General generer()
	{
		String version = "";
		try {
			version = Manifests.read("version");
		} catch (java.lang.IllegalArgumentException e) {
		}
		General general = new General(version);
		
		return general;
	}
}
