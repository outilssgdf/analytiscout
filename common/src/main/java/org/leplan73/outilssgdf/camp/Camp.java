package org.leplan73.outilssgdf.camp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.leplan73.outilssgdf.calcul.UniteSimple;

public class Camp {
	private int numero_;
	private int indexDirecteur_ = -1;
	
	private List<Chef> maitrise_ = new ArrayList<Chef>();
	
	private int getI(String champ)
	{
		return Integer.parseInt(maitrise_.get(0).get(champ));
	}

	public Camp(String numero) {
		numero_ = Integer.parseInt(numero);
	}
	
	public List<Chef> getMaitrise()
	{
		return maitrise_;
	}

	public int getNumero() {
		return numero_;
	}

	public UniteSimple getUnite() {
		if (indexDirecteur_ >= 0)
			return maitrise_.get(indexDirecteur_).getUnite();
		else
			return maitrise_.get(0).getUnite();
	}

	public String getDirecteur() {
		if (indexDirecteur_ >= 0)
			return maitrise_.get(indexDirecteur_).get("Nom du directeur");
		return "";
	}
	
	public int getJeunes() {
		return getI("Jeunes");
	}
	
	public int getChefs() {
		return maitrise_.size();
	}
	
	public String get(String champ)
	{
		return maitrise_.get(0).get(champ);
	}

	public String getStructuresOrganistratices() {
		return maitrise_.get(0).get("Structures organisatrices");
	}

	public void init() {
	}
	
	public void complete()
	{
		AtomicInteger index = new AtomicInteger();
		maitrise_.forEach(chef ->
		{
			String v = chef.get("Fonction");
			if (v.compareTo("Directeur") == 0)
			{
				indexDirecteur_ = index.get();
			}
			index.incrementAndGet();
		});
	}
	
	@Override
	public String toString()
	{
		return numero_ + "/" + maitrise_.size();
	}

	public void add(Chef maitrise) {
		maitrise_.add(maitrise);
	}
}
