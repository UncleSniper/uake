package org.unclesniper.uake.semantics;

import java.util.Deque;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class JavaFunction extends AbstractFunction {

	public static class JavaParameter extends AbstractParameter {

		public JavaParameter(String name, UakeType type, boolean elliptic) {
			super(name, type, elliptic);
		}

	}

	private final LinkedList<JavaParameter> parameters = new LinkedList<JavaParameter>();

	private Class<?> enclosingClass;

	public JavaFunction(QualifiedName qualifiedName, Location definitionLocation, Class<?> enclosingClass,
			UakeType returnType) {
		super(qualifiedName, definitionLocation, returnType);
		this.enclosingClass = enclosingClass;
	}

	public Class<?> getEnclosingClass() {
		return enclosingClass;
	}

	public void setEnclosingClass(Class<?> enclosingClass) {
		this.enclosingClass = enclosingClass;
	}

	protected Deque<? extends Parameter> getInternalParameters() {
		return parameters;
	}

	public void addParameter(JavaParameter parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

}
