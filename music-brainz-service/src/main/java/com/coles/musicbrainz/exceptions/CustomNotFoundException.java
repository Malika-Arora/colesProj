package com.coles.musicbrainz.exceptions;

public class CustomNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomNotFoundException() {

	}

	public CustomNotFoundException(String message) {
		super(message);
	}

}
