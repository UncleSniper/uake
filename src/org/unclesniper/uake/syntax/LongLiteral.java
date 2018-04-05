package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.LiteralExceedsRangeException;

public class LongLiteral extends Expression {

	private final String specifier;

	private final long value;

	public LongLiteral(Location location, String specifier) {
		super(location);
		this.specifier = specifier;
		try {
			if(specifier.charAt(0) != '0')
				value = Long.parseLong(specifier);
			else if(specifier.length() > 2 && (specifier.charAt(1) == 'x' || specifier.charAt(1) == 'X'))
				value = Long.parseLong(specifier, 16);
			else
				value = Long.parseLong(specifier, 8);
		}
		catch(NumberFormatException nfe) {
			throw new LiteralExceedsRangeException(location, LiteralExceedsRangeException.Type.LONG, specifier);
		}
	}

	public void createElements(CompilationContext cctx) {}

	public void bindTypes(CompilationContext cctx) {}

}
