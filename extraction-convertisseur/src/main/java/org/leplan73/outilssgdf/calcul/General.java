package org.leplan73.outilssgdf.calcul;

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
