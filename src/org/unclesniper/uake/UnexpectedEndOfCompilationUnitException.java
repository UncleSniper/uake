package org.unclesniper.uake;

public class UnexpectedEndOfCompilationUnitException extends SyntaxException {

	public UnexpectedEndOfCompilationUnitException(Location location, String expected) {
		super(location, "Syntax error at " + location + " near end of input: Expected " + expected, expected);
	}

	public UnexpectedEndOfCompilationUnitException(Location location, Token.Type... expected) {
		this(location, UnexpectedTokenException.makeExpectation(expected));
	}

}
