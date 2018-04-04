package org.unclesniper.uake.semantics;

import java.util.Deque;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class Provision extends AbstractFunction {

	private final LinkedList<SoftFunction.SoftParameter> parameters = new LinkedList<SoftFunction.SoftParameter>();

	public Provision(QualifiedName qualifiedName, Location definitionLocation, UakeType returnType) {
		super(qualifiedName, definitionLocation, returnType);
	}

	protected Deque<? extends Parameter> getInternalParameters() {
		return parameters;
	}

	public void addParameter(SoftFunction.SoftParameter parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

}
