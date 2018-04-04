package org.unclesniper.uake;

public class UnboundNameException extends SemanticsException {

	private final String unboundName;

	public UnboundNameException(Location location, String unboundName) {
		super(location, "Name '" + unboundName + "' is not bound at " + location);
		this.unboundName = unboundName;
	}

	public String getUnboundName() {
		return unboundName;
	}

}
