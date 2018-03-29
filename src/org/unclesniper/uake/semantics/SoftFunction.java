package org.unclesniper.uake.semantics;

import java.util.Deque;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class SoftFunction extends AbstractFunction {

	public static class SoftParameter extends AbstractParameter {

		public SoftParameter(String name, UakeType type, boolean elliptic) {
			super(name, type, elliptic);
		}

	}

	private final LinkedList<SoftParameter> parameters = new LinkedList<SoftParameter>();

	public SoftFunction(QualifiedName qualifiedName, Location definitionLocation, UakeType returnType) {
		super(qualifiedName, definitionLocation, returnType);
	}

	protected Deque<? extends Parameter> getInternalParameters() {
		return parameters;
	}

	public void addParameter(SoftParameter parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

}
