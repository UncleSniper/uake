package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class TemplateParameter extends Syntax {

	private final String name;

	private final boolean elliptic;

	public TemplateParameter(Location location, String name, boolean elliptic) {
		super(location);
		this.name = name;
		this.elliptic = elliptic;
	}

	public String getName() {
		return name;
	}

	public boolean isElliptic() {
		return elliptic;
	}

}
