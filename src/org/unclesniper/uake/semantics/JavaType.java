package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class JavaType extends AbstractType {

	private Class<?> backingClass;

	public JavaType(QualifiedName qualifiedName, Location definitionLocation, Class<?> backingClass) {
		super(qualifiedName, definitionLocation);
		this.backingClass = backingClass;
	}

	public Class<?> getBackingClass() {
		return backingClass;
	}

	public void setBackingClass(Class<?> backingClass) {
		this.backingClass = backingClass;
	}

}
