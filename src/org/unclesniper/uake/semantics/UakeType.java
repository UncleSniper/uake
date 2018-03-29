package org.unclesniper.uake.semantics;

public interface UakeType extends UakeMember, UakeTypeEmitter {

	Class<?> getBackingClass();

	UakeModule.Group<UakeType> getDirectSupertypes();

}
