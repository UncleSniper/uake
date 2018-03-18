package org.unclesniper.uake.syntax;

public interface Parameterized {

	void addParameter(Parameter parameter);

	Iterable<Parameter> getParameters();

	boolean isElliptic();

}
