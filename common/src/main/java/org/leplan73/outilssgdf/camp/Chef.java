package org.leplan73.outilssgdf.camp;

import java.util.Hashtable;
import java.util.Map;

public class Chef {
	protected Map<String, String> data_ = new Hashtable<String, String>();

	public void add(String champ, String text) {
		data_.put(champ, text);
	}

	public String getStructuresOrganistratices() {
		return data_.get("Structures organisatrices");
	}

}
