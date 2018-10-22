package org.leplan73.outilssgdf.extraction;

import java.time.Instant;

public class General {
	
	Instant now = Instant.now();
	
	public General()
	{
		
	}
	
	public String getDategeneration()
	{
		return now.toString();
	}
}
