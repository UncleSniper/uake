package org.unclesniper.uake;

import java.io.IOException;

public class SourceReadIOException extends CompilationException {

	private final IOException ioException;

	public SourceReadIOException(Location location, IOException ioException) {
		super(location, "I/O error reading script" + (location != null ? " at " + location : "")
				+ (ioException != null && ioException.getMessage() != null ? ": " + ioException.getMessage() : ""));
		this.ioException = ioException;
	}

	public IOException getIOException() {
		return ioException;
	}

}
