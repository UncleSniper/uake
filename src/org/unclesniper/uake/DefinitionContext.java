package org.unclesniper.uake;

import org.unclesniper.uake.syntax.QualifiedName;
import org.unclesniper.uake.semantics.UakeModule;

public class DefinitionContext {

	private final UakeModule rootModule = new UakeModule(new QualifiedName(), Location.UNKNOWN, null);

	public DefinitionContext() {}

	public UakeModule getRootModule() {
		return rootModule;
	}

}
