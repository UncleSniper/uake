package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class StringLiteral extends Expression {

	private final String value;

	public StringLiteral(Location location, String value) {
		super(location);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
