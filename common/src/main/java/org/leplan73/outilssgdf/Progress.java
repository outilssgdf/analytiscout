package org.leplan73.outilssgdf;

public interface Progress {

	void setMillisToPopup(int i);

	void setMillisToDecideToPopup(int i);

	void setProgress(int i);
	
	void setNote(String note);

	boolean isCanceled();

}
