package org.unclesniper.uake.semantics;

public interface Transform<SourceT, DestinationT> {

	DestinationT transform(SourceT source);

}
