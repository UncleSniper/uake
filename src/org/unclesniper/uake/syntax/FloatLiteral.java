package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

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

	public void createElements(CompilationContext cctx) {}

	public void bindTypes(CompilationContext cctx) {}

}
