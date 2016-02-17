package com.web.rest.web.exception;

public class RSTException extends Exception {

	private static final long serialVersionUID = 1L;

	public RSTException() {
	}

	public RSTException(String message) {
		super(message);
	}

	public RSTException(Throwable cause) {
		super(cause);
	}

	public RSTException(String message, Throwable cause) {
		super(message, cause);
	}

	public RSTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
