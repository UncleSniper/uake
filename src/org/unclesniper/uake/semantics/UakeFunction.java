package org.unclesniper.uake.semantics;

public interface UakeFunction extends UakeMember {

	public interface Parameter {

		String getName();

		UakeType getType();

		boolean isElliptic();

	}

	Iterable<? extends Parameter> getParameters();

	boolean isElliptic();

	int getMinArity();

	int getMaxArity();

	UakeType getReturnType();

}
