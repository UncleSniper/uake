package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class UseConstruct extends Expression {

	private Expression property;

	private Expression implementation;

	public UseConstruct(Location initiator, Expression property, Expression implementation) {
		super(initiator);
		this.property = property;
		this.implementation = implementation;
	}

	public Expression getProperty() {
		return property;
	}

	public void setProperty(Expression property) {
		this.property = property;
	}

	public Expression getImplementation() {
		return implementation;
	}

	public void setImplementation(Expression implementation) {
		this.implementation = implementation;
	}

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
