package com.web.rest.api.exception;

public class RESTException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RESTException() {
		super();
	}

	public RESTException(String message) {
		super(message);
	}

	public RESTException(Throwable cause) {
		super(cause);
	}

	public RESTException(String message, Throwable cause) {
		super(message, cause);
	}
}
