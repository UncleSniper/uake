package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class NameReference extends Expression {

	private String name;

	public NameReference(Location location, String name) {
		super(location);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
