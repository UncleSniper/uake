package org.unclesniper.uake.semantics;

public interface UakeFunctionTemplate extends UakeTemplate {

	UakeFunction emitFunction(UakeType... templateArguments);

}
