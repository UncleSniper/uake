package org.unclesniper.uake;

public class UnexpectedTokenException extends SyntaxException {

	private final Token unexpected;

	public UnexpectedTokenException(Token unexpected, String expected) {
		super(unexpected.getLocation(), "Syntax error at " + unexpected.getLocation() + " near "
				+ unexpected.reproduce() + ": Expected " + expected, expected);
		this.unexpected = unexpected;
	}

	public UnexpectedTokenException(Token unexpected, Token.Type... expected) {
		this(unexpected, UnexpectedTokenException.makeExpectation(expected));
	}

	public Token getUnexpected() {
		return unexpected;
	}

	public static String makeExpectation(Token.Type[] types) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < types.length; ++i) {
			if(i > 0)
				builder.append(i == types.length - 1 ? ", or " : ", ");
			builder.append(types[i].getDescription());
		}
		return builder.toString();
	}

}
