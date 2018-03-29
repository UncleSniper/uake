package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;

public interface UakeFunctionTemplate extends UakeTemplate {

	public interface ParameterTemplate {

		String getName();

		UakeTypeEmitter getType();

		boolean isElliptic();

		UakeFunction.Parameter emitParameter(UakeType[] templateArguments, Location emissionLocation);

	}

	Iterable<? extends ParameterTemplate> getParameters();

	boolean isElliptic();

	int getMinArity();

	int getMaxArity();

	UakeTypeEmitter getReturnType();

	UakeFunction emitFunction(UakeType[] templateArguments, Location emissionLocation);

}
