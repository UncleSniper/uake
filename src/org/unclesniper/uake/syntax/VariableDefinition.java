package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class VariableDefinition extends Definition {

	private boolean constant;

	private TypeSpecifier type;

	private String name;

	private Location nameLocation;

	private Expression initializer;

	public VariableDefinition(Location initiator, boolean constant, TypeSpecifier type,
			String name, Location nameLocation, Expression initializer) {
		super(initiator);
		this.constant = constant;
		this.type = type;
		this.name = name;
		this.nameLocation = nameLocation;
		this.initializer = initializer;
	}

	public boolean isConstant() {
		return constant;
	}

	public void setConstant(boolean constant) {
		this.constant = constant;
	}

	public TypeSpecifier getType() {
		return type;
	}

	public void setType(TypeSpecifier type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(Location nameLocation) {
		this.nameLocation = nameLocation;
	}

	public Expression getInitializer() {
		return initializer;
	}

	public void setInitializer(Expression initializer) {
		this.initializer = initializer;
	}

}
