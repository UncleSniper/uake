package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class UnuseConstruct extends Expression {

	private Expression property;

	public UnuseConstruct(Location initiator, Expression property) {
		super(initiator);
		this.property = property;
	}

	public Expression getProperty() {
		return property;
	}

	public void setProperty(Expression property) {
		this.property = property;
	}

	public void createElements(CompilationContext cctx) {
		property.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		property.bindTypes(cctx);
	}

}
