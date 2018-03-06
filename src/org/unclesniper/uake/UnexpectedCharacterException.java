package org.unclesniper.uake;

public class UnexpectedCharacterException extends LexicalException {

	private final char badChar;

	public UnexpectedCharacterException(Location location, char badChar) {
		super(location, "Unexpected character at " + location + ": " + ScriptUtils.escapeChar(badChar, true));
		this.badChar = badChar;
	}

	public char getBadChar() {
		return badChar;
	}

}
