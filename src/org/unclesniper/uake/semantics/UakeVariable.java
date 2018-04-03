package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class UakeVariable extends AbstractMember {

	private UakeType type;

	private boolean constant;

	public UakeVariable(QualifiedName qualifiedName, Location definitionLocation, UakeType type, boolean constant) {
		super(qualifiedName, definitionLocation);
		this.type = type;
		this.constant = constant;
	}

	public UakeType getType() {
		return type;
	}

	public void setType(UakeType type) {
		this.type = type;
	}

	public boolean isConstant() {
		return constant;
	}

	public void setConstant(boolean constant) {
		this.constant = constant;
	}

}
