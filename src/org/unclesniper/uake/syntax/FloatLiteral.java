package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class FloatLiteral extends Expression {

	private final String specifier;

	private final float value;

	public FloatLiteral(Location location, String specifier) {
		super(location);
		this.specifier = specifier;
		value = Float.parseFloat(specifier);
	}

	public String getSpecifier() {
		return specifier;
	}

	public float getValue() {
		return value;
	}

}
