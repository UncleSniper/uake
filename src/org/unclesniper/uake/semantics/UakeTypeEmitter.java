package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;

public interface UakeTypeEmitter {

	UakeType emitType(UakeType[] templateArguments, Location emissionLocation);

}
