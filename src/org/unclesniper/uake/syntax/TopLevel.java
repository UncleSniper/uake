package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public abstract class TopLevel extends Syntax {

	public TopLevel(Location location) {
		super(location);
	}

	public abstract void createElements(CompilationContext cctx);

	public abstract void bindTypes(CompilationContext cctx);

}
