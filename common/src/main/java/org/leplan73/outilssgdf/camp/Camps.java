package org.leplan73.outilssgdf.camp;

import java.util.HashMap;
import java.util.Map;

public class Camps extends HashMap<String,Camp> {
	
	Map<Integer, String> index;
	
	public Camps()
	{
		index = new HashMap<Integer, String>();
	}
			 
	@Override
	public Camp put(String key, Camp value)
	{
		index.put(this.size(), key);
		return super.put(key, value);
	}
	
	public Camp getNumeros(int numero)
	{
		return this.get(index.get(numero));
	}
}
