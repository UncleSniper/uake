package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractTypeTemplate extends AbstractTemplate implements UakeTypeTemplate {

	private final List<UakeTypeEmitter> directSupertypes = new LinkedList<UakeTypeEmitter>();

	public AbstractTypeTemplate(QualifiedName qualifiedName, Location definitionLocation) {
		super(qualifiedName, definitionLocation);
	}

	public Iterable<UakeTypeEmitter> getDirectSupertypes() {
		return directSupertypes;
	}

	public void addDirectSupertype(UakeTypeEmitter supertype) {
		if(supertype != null)
			directSupertypes.add(supertype);
	}

}
