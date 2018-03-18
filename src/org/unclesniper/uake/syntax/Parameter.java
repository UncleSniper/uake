package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class Parameter extends Syntax {

	private final TypeSpecifier type;

	private final String name;

	private final boolean elliptic;

	public Parameter(TypeSpecifier type, Location location, String name, boolean elliptic) {
		super(location);
		this.type = type;
		this.name = name;
		this.elliptic = elliptic;
	}

	public TypeSpecifier getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isElliptic() {
		return elliptic;
	}

}
