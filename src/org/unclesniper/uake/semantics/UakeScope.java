package org.unclesniper.uake.semantics;

public interface UakeScope {

	void resolve(String name, UakeModule.Group<UakeScoped> sink);

}
