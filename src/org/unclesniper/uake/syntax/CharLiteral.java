package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class CharLiteral extends Expression {

	private final char value;

	public CharLiteral(Location location, char value) {
		super(location);
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	public void createElements(CompilationContext cctx) {}

	public void bindTypes(CompilationContext cctx) {}

}
