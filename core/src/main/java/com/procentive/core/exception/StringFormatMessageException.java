package com.procentive.core.exception;

public abstract class StringFormatMessageException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6230205291061400166L;

	public StringFormatMessageException(String message, Throwable cause, Object... args) {
		super(String.format(message, args), cause);
	}

	public StringFormatMessageException(String message, Object... args) {
		super(String.format(message, args));
	}

	
	
}
