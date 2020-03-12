package org.leplan73.analytiscout.cmd.utils;

public class CmdLineException extends Exception {

	private static final long serialVersionUID = 1L;

	private CmdParams cmd_;
	private boolean lance_;
	
	public CmdLineException(String message, boolean lance) {
		super(message);
		lance_ = lance;
	}

	public void setCommand(CmdParams cmd) {
		cmd_ = cmd;
	}

	public CmdParams getCmd() {
		return cmd_;
	}

	public boolean estLance() {
		return lance_;
	}
}
