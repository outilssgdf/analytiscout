package org.leplan73.analytiscout.intranet;

public class LoginEngineException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoginEngineException(String message) {
		super(message);
	}

	public LoginEngineException(String message, Exception e) {
		super(message, e);
	}
}
