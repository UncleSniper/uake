package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractTypeTemplateInstance extends AbstractTemplateInstance implements UakeType {

	private final Location emissionLocation;

	public AbstractTypeTemplateInstance(QualifiedName qualifiedName, Location definitionLocation,
			Location emissionLocation) {
		super(qualifiedName, definitionLocation);
		this.emissionLocation = emissionLocation;
	}

	public Location getEmissionLocation() {
		return emissionLocation;
	}

	protected abstract UakeTypeTemplate getBackingTemplate();

	public UakeModule.Group<UakeType> getDirectSupertypes() {
		UakeType[] templateArguments = TypeUtils.getTemplateArgumentsAsArray(this);
		UakeModule.Group<UakeType> supertypes = new UakeModule.Group<UakeType>();
		for(UakeTypeEmitter supertype : getBackingTemplate().getDirectSupertypes())
			supertypes.addMember(supertype.emitType(templateArguments, emissionLocation));
		return supertypes;
	}

	public UakeType emitType(UakeType[] templateArguments, Location emissionLocation) {
		return this;
	}

}
