package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public abstract class Expression extends Syntax {

	public Expression(Location location) {
		super(location);
	}

	public abstract void bindTypes(CompilationContext cctx);

}
