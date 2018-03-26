package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.LiteralExceedsRangeException;

public class ShortLiteral extends Expression {

	private final String specifier;

	private final short value;

	public ShortLiteral(Location location, String specifier) {
		super(location);
		this.specifier = specifier;
		try {
			if(specifier.charAt(0) != '0')
				value = Short.parseShort(specifier);
			else if(specifier.length() > 2 && (specifier.charAt(1) == 'x' || specifier.charAt(1) == 'X'))
				value = Short.parseShort(specifier, 16);
			else
				value = Short.parseShort(specifier, 8);
		}
		catch(NumberFormatException nfe) {
			throw new LiteralExceedsRangeException(location, LiteralExceedsRangeException.Type.SHORT, specifier);
		}
	}

	public String getSpecifier() {
		return specifier;
	}

	public short getValue() {
		return value;
	}

}
