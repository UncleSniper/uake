package org.unclesniper.uake.semantics;

public interface UakeType extends UakeMember {

	Class<?> getBackingClass();

	UakeModule.Group<UakeType> getDirectSupertypes();

}
