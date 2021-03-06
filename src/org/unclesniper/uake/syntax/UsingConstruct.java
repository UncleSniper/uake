package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class UsingConstruct extends Expression {

	private Expression property;

	private Expression implementation;

	private Statement body;

	public UsingConstruct(Location initiator, Expression property, Expression implementation, Statement body) {
		super(initiator);
		this.property = property;
		this.implementation = implementation;
		this.body = body;
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

	public Statement getBody() {
		return body;
	}

	public void setBody(Statement body) {
		this.body = body;
	}

	public void createElements(CompilationContext cctx) {
		property.createElements(cctx);
		implementation.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		property.bindTypes(cctx);
		implementation.bindTypes(cctx);
	}

}
