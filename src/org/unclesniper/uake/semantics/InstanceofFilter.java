package org.unclesniper.uake.semantics;

public class InstanceofFilter<T> implements Filter<T> {

	private Class<?> type;

	public InstanceofFilter(Class<?> type) {
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public boolean accept(T subject) {
		return type != null && type.isInstance(subject);
	}

}
