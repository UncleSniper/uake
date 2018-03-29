package org.unclesniper.uake.semantics;

import java.util.Deque;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractFunctionTemplateInstance extends AbstractTemplateInstance implements UakeFunction {

	private final Location emissionLocation;

	private UakeType returnType;

	public AbstractFunctionTemplateInstance(QualifiedName qualifiedName, Location definitionLocation,
			Location emissionLocation, UakeType returnType) {
		super(qualifiedName, definitionLocation);
		this.emissionLocation = emissionLocation;
		this.returnType = returnType;
	}

	public Location getEmissionLocation() {
		return emissionLocation;
	}

	protected abstract UakeFunctionTemplate getBackingTemplate();

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
