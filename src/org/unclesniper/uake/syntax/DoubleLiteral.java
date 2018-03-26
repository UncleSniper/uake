package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class DoubleLiteral extends Expression {

	private final String specifier;

	private final double value;

	public DoubleLiteral(Location location, String specifier) {
		super(location);
		this.specifier = specifier;
		value = Double.parseDouble(specifier);
	}

	public String getSpecifier() {
		return specifier;
	}

	public double getValue() {
		return value;
	}

}
