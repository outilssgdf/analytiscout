package org.leplan73.outilssgdf.cmd;

import org.leplan73.outilssgdf.Progress;

public class CmdProgress implements Progress {
	
	public CmdProgress()
	{
	}

	@Override
	public void setMillisToPopup(int i) {
	}

	@Override
	public void setMillisToDecideToPopup(int i) {
	}

	@Override
	public void setProgress(int i) {
	}

	@Override
	public boolean isCanceled() {
		return false;
	}
}
