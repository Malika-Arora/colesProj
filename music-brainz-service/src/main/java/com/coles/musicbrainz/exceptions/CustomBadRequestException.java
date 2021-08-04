package com.coles.musicbrainz.exceptions;

public class CustomBadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomBadRequestException() {

	}

	public CustomBadRequestException(String message) {
		super(message);
	}

}
