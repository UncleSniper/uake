package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class NameReference extends Expression {

	private String name;

	public NameReference(Location location, String name) {
		super(location);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void bindTypes(CompilationContext cctx) {}

}
