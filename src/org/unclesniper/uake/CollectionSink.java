package org.unclesniper.uake;

import java.util.Collection;

public class CollectionSink<T> implements Sink<T> {

	private Collection<T> destination;

	public CollectionSink(Collection<T> destination) {
		this.destination = destination;
	}

	public Collection<T> getDestination() {
		return destination;
	}

	public void setDestination(Collection<T> destination) {
		this.destination = destination;
	}

	public void sink(T element) {
		destination.add(element);
	}

}
