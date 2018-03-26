package org.unclesniper.uake.semantics;

public class InstanceofTransform<SourceT, DestinationT extends SourceT>
		implements Transform<SourceT, DestinationT> {

	private Class<? extends DestinationT> type;

	public InstanceofTransform(Class<? extends DestinationT> type) {
		this.type = type;
	}

	public Class<? extends DestinationT> getType() {
		return type;
	}

	public void setType(Class<? extends DestinationT> type) {
		this.type = type;
	}

	public DestinationT transform(SourceT source) {
		if(type != null && type.isInstance(source))
			return type.cast(source);
		else
			return null;
	}

}
