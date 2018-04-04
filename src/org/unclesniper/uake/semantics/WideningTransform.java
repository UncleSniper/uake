package org.unclesniper.uake.semantics;

public class WideningTransform<SourceT extends DestinationT, DestinationT>
		implements Transform<SourceT, DestinationT> {

	public WideningTransform() {}

	public DestinationT transform(SourceT source) {
		return source;
	}

}
