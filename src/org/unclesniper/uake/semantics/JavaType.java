package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.syntax.QualifiedName;

public class JavaType extends AbstractMember {

	private Class<?> backingClass;

	private final List<UakeType> directSupertypes = new LinkedList<UakeType>();

	public JavaType(QualifiedName qualifiedName, Class<?> backingClass) {
		super(qualifiedName);
		this.backingClass = backingClass;
	}

	public Class<?> getBackingClass() {
		return backingClass;
	}

	public void setBackingClass(Class<?> backingClass) {
		this.backingClass = backingClass;
	}

	public void addDirectSupertype(UakeType supertype) {
		if(supertype != null)
			directSupertypes.add(supertype);
	}

	public UakeModule.Group<UakeType> getDirectSupertypes() {
		return new UakeModule.Group<UakeType>(directSupertypes);
	}

}
