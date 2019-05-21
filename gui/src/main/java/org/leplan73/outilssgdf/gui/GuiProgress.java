package org.leplan73.outilssgdf.gui;

import javax.swing.ProgressMonitor;

import org.leplan73.outilssgdf.Progress;

public class GuiProgress implements Progress {
	
	private ProgressMonitor progress_;
	
	public GuiProgress(ProgressMonitor progress)
	{
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
	public boolean isCanceled() {
		if (progress_ != null) return progress_.isCanceled();
		return false;
	}
}
