package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.LiteralExceedsRangeException;

public class ByteLiteral extends Expression {

	private final String specifier;

	private final byte value;

	public ByteLiteral(Location location, String specifier) {
		super(location);
		this.specifier = specifier;
		try {
			if(specifier.charAt(0) != '0')
				value = Byte.parseByte(specifier);
			else if(specifier.length() > 2 && (specifier.charAt(1) == 'x' || specifier.charAt(1) == 'X'))
				value = Byte.parseByte(specifier, 16);
			else
				value = Byte.parseByte(specifier, 8);
		}
		catch(NumberFormatException nfe) {
			throw new LiteralExceedsRangeException(location, LiteralExceedsRangeException.Type.BYTE, specifier);
		}
	}

	public String getSpecifier() {
		return specifier;
	}

	public byte getValue() {
		return value;
	}

}
