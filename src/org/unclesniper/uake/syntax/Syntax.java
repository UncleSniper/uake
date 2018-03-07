package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public abstract class Syntax {

	private Location location;

	public Syntax(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
