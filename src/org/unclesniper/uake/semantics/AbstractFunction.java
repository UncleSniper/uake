package org.unclesniper.uake.semantics;

import java.util.Deque;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractFunction extends AbstractMember implements UakeFunction {

	public static abstract class AbstractParameter implements Parameter {

		private final String name;

		private final UakeType type;

		private final boolean elliptic;

		public AbstractParameter(String name, UakeType type, boolean elliptic) {
			this.name = name;
			this.type = type;
			this.elliptic = elliptic;
		}

		public String getName() {
			return name;
		}

		public UakeType getType() {
			return type;
		}

		public boolean isElliptic() {
			return elliptic;
		}

	}

	private UakeType returnType;

	public AbstractFunction(QualifiedName qualifiedName, Location definitionLocation, UakeType returnType) {
		super(qualifiedName, definitionLocation);
		this.returnType = returnType;
	}

	protected abstract Deque<? extends Parameter> getInternalParameters();

	public Iterable<? extends Parameter> getParameters() {
		return getInternalParameters();
	}

	public boolean isElliptic() {
		Deque<? extends Parameter> parameters = getInternalParameters();
		return !parameters.isEmpty() && parameters.getLast().isElliptic();
	}

	public int getMinArity() {
		return getInternalParameters().size() - (isElliptic() ? 1 : 0);
	}

	public int getMaxArity() {
		return isElliptic() ? -1 : getInternalParameters().size();
	}

	public UakeType getReturnType() {
		return returnType;
	}

	public void setReturnType(UakeType returnType) {
		this.returnType = returnType;
	}

}
