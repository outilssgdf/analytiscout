package org.leplan73.analytiscout.extraction;

import java.util.HashMap;

public class Parents extends HashMap<String, Parent> {

	private static final long serialVersionUID = 1L;
	
	public Parents()
	{
	}
	
	public void complete()
	{
		this.forEach((code,parent) -> parent.complete());
	}
}
