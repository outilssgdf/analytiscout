package org.leplan73.outilssgdf.gui;

import javax.swing.ProgressMonitor;
import javax.swing.UIManager;

import org.leplan73.outilssgdf.Progress;

public class GuiProgress implements Progress {
	
	private ProgressMonitor progress_;
	
	public GuiProgress(ProgressMonitor progress, String titre)
	{
		UIManager.put("ProgressMonitor.progressText", titre);
		progress_ = progress;
	}

	@Override
	public void setMillisToPopup(int i) {
		if (progress_ != null) progress_.setMillisToPopup(i);
	}

	@Override
	public void setMillisToDecideToPopup(int i) {
		if (progress_ != null) progress_.setMillisToDecideToPopup(i);
	}

	@Override
	public void setProgress(int i) {
		if (progress_ != null) progress_.setProgress(i);
	}

	@Override
	public void setProgress(int i, String note) {
		if (progress_ != null) progress_.setProgress(i);
		if (note != null) progress_.setNote(note);
	}

	@Override
	public boolean isCanceled() {
		if (progress_ != null) return progress_.isCanceled();
		return false;
	}

	@Override
	public void setNote(String note) {
		if (progress_ != null) progress_.setNote(note);
	}

	@Override
	public void start() {
		this.setProgress(0, "Lancement");
	}

	@Override
	public void stop() {
		this.setProgress(100, "Fin");
	}
}
