package org.unclesniper.uake.semantics;

public interface UakeTypeTemplate extends UakeTemplate, UakeTypeEmitter {

	Class<?> getBackingClass();

	Iterable<UakeTypeEmitter> getDirectSupertypes();

}
