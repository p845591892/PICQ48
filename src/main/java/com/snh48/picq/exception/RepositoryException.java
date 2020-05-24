package com.snh48.picq.exception;

public class RepositoryException extends RuntimeException{

	private static final long serialVersionUID = -2756170208331071126L;

	public RepositoryException() {
		super();
	}

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryException(String message) {
		super(message);
	}

	public RepositoryException(Throwable cause) {
		super(cause);
	}
	
}
