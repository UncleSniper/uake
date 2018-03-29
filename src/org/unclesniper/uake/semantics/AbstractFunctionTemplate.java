package org.unclesniper.uake.semantics;

import java.util.Deque;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractFunctionTemplate extends AbstractTemplate implements UakeFunctionTemplate {

	public static abstract class AbstractParameterTemplate implements ParameterTemplate {

		private final String name;

		private final UakeTypeEmitter type;

		private final boolean elliptic;

		public AbstractParameterTemplate(String name, UakeTypeEmitter type, boolean elliptic) {
			this.name = name;
			this.type = type;
			this.elliptic = elliptic;
		}

		public String getName() {
			return name;
		}

		public UakeTypeEmitter getType() {
			return type;
		}

		public boolean isElliptic() {
			return elliptic;
		}

	}

	private UakeTypeEmitter returnType;

	public AbstractFunctionTemplate(QualifiedName qualifiedName, Location definitionLocation,
			UakeTypeEmitter returnType) {
		super(qualifiedName, definitionLocation);
		this.returnType = returnType;
	}

	protected abstract Deque<? extends ParameterTemplate> getInternalParameters();

	public Iterable<? extends ParameterTemplate> getParameters() {
		return getInternalParameters();
	}

	public boolean isElliptic() {
		Deque<? extends ParameterTemplate> parameters = getInternalParameters();
		return !parameters.isEmpty() && parameters.getLast().isElliptic();
	}

	public int getMinArity() {
		return getInternalParameters().size() - (isElliptic() ? 1 : 0);
	}

	public int getMaxArity() {
		return isElliptic() ? -1 : getInternalParameters().size();
	}

	public UakeTypeEmitter getReturnType() {
		return returnType;
	}

	public void setReturnType(UakeTypeEmitter returnType) {
		this.returnType = returnType;
	}

}
