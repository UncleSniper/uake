package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class ForeachConstruct extends Expression {

	private TypeSpecifier elementType;

	private String bindName;

	private Location bindLocation;

	private Expression collection;

	private Statement body;

	public ForeachConstruct(Location initiator, TypeSpecifier elementType,
			String bindName, Location bindLocation, Expression collection, Statement body) {
		super(initiator);
		this.elementType = elementType;
		this.bindName = bindName;
		this.bindLocation = bindLocation;
		this.collection = collection;
		this.body = body;
	}

	public TypeSpecifier getElementType() {
		return elementType;
	}

	public void setElementType(TypeSpecifier elementType) {
		this.elementType = elementType;
	}

	public String getBindName() {
		return bindName;
	}

	public void setBindName(String bindName) {
		this.bindName = bindName;
	}

	public Location getBindLocation() {
		return bindLocation;
	}

	public void setBindLocation(Location bindLocation) {
		this.bindLocation = bindLocation;
	}

	public Expression getCollection() {
		return collection;
	}

	public void setCollection(Expression collection) {
		this.collection = collection;
	}

	public Statement getBody() {
		return body;
	}

	public void setBody(Statement body) {
		this.body = body;
	}

}
