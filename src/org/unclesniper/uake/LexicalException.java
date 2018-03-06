package org.unclesniper.uake;

public class LexicalException extends CompilationException {

	public LexicalException(Location location, String message) {
		super(location, message);
	}

	public LexicalException(Location location, String message, Throwable cause) {
		super(location, message, cause);
	}

}
