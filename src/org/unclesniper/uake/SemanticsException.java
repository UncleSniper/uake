package org.unclesniper.uake;

public class SemanticsException extends CompilationException {

	public SemanticsException(Location location, String message) {
		super(location, message);
	}

}
