package org.unclesniper.uake.semantics;

public interface Filter<T> {

	boolean accept(T subject);

}
