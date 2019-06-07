package org.leplan73.outilssgdf.camp;

import java.util.ArrayList;
import java.util.List;

public class Camp {
	private String structure_;
	
	protected List<Chef> maitrise_ = new ArrayList<Chef>();

	public Camp(String structure) {
		structure_ = structure;
	}

	public String getStructuresOrganistratices() {
		return structure_;
	}

	public void init() {
		if (structure_.endsWith(","))
		{
			structure_ = structure_.substring(0, structure_.length()-1);
		}
	}
	
	@Override
	public String toString()
	{
		return structure_ + "/" + maitrise_.size();
	}

	public void add(Chef maitrise) {
		maitrise_.add(maitrise);
	}
}
