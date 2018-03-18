package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class BinaryClassName extends ClassReference {

	private final String binaryName;

	public BinaryClassName(Location location, String binaryName) {
		super(location);
		this.binaryName = binaryName;
	}

	public String getBinaryName() {
		return binaryName;
	}

}
