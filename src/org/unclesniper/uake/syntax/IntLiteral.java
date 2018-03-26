package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.LiteralExceedsRangeException;

public class IntLiteral extends Expression {

	private final String specifier;

	private final int value;

	public IntLiteral(Location location, String specifier) {
		super(location);
		this.specifier = specifier;
		try {
			if(specifier.charAt(0) != '0')
				value = Integer.parseInt(specifier);
			else if(specifier.length() > 2 && (specifier.charAt(1) == 'x' || specifier.charAt(1) == 'X'))
				value = Integer.parseInt(specifier, 16);
			else
				value = Integer.parseInt(specifier, 8);
		}
		catch(NumberFormatException nfe) {
			throw new LiteralExceedsRangeException(location, LiteralExceedsRangeException.Type.INT, specifier);
		}
	}

	public String getSpecifier() {
		return specifier;
	}

	public int getValue() {
		return value;
	}

}
