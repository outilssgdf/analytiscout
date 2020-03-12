package org.leplan73.analytiscout;

public interface Progress {

	void setMillisToPopup(int i);

	void setMillisToDecideToPopup(int i);

	void start();
	void stop();
	void setProgress(int i);
	void setProgress(int i, String note);
	
	void setNote(String note);

	boolean isCanceled();

}
