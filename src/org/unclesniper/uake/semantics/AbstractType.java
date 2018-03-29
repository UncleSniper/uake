package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractType extends AbstractMember implements UakeType {

	private final List<UakeType> directSupertypes = new LinkedList<UakeType>();

	public AbstractType(QualifiedName qualifiedName, Location definitionLocation) {
		super(qualifiedName, definitionLocation);
	}

	public void addDirectSupertype(UakeType supertype) {
		if(supertype != null)
			directSupertypes.add(supertype);
	}

	public UakeModule.Group<UakeType> getDirectSupertypes() {
		return new UakeModule.Group<UakeType>(directSupertypes);
	}

	public UakeType emitType(UakeType[] templateArguments, Location emissionLocation) {
		return this;
	}

}
