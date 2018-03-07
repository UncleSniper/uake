package org.unclesniper.uake;

public class SyntaxException extends CompilationException {

	private final String expected;

	public SyntaxException(Location location, String message, String expected) {
		super(location, message);
		this.expected = expected;
	}

	public String getExpected() {
		return expected;
	}

}
