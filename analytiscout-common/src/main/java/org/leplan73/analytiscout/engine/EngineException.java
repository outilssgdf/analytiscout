package org.leplan73.analytiscout.engine;

public class EngineException extends Exception {

	private static final long serialVersionUID = 1L;

	public EngineException(String message, boolean lance) {
		super(message);
	}

	public EngineException(String message, Exception e) {
		super(message, e);
	}
}
