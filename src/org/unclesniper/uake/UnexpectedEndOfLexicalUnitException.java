package org.unclesniper.uake;

public class UnexpectedEndOfLexicalUnitException extends LexicalException {

	public UnexpectedEndOfLexicalUnitException(Location location) {
		super(location, "Unexpected end of lexical unit at " + location);
	}

}
