package org.unclesniper.uake;

public class CompilationException extends UakeException {

	private final Location location;

	public CompilationException(Location location, String message) {
		super(message);
		this.location = location;
	}

	public CompilationException(Location location, String message, Throwable cause) {
		super(message, cause);
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

}
