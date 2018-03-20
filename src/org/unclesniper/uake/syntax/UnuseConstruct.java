package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

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

}
