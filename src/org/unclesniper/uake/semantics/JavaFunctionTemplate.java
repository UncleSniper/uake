package org.unclesniper.uake.semantics;

import java.util.Deque;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class JavaFunctionTemplate extends AbstractFunctionTemplate {

	public static class JavaParameterTemplate extends AbstractParameterTemplate {

		public JavaParameterTemplate(String name, UakeTypeEmitter type, boolean elliptic) {
			super(name, type, elliptic);
		}

		public Instance.ParameterInstance emitParameter(UakeType[] templateArguments, Location emissionLocation) {
			UakeType type = getType().emitType(templateArguments, emissionLocation);
			return new Instance.ParameterInstance(getName(), type, isElliptic());
		}

	}

	public static class Instance extends AbstractFunctionTemplateInstance {

		public static class ParameterInstance extends AbstractFunction.AbstractParameter {

			public ParameterInstance(String name, UakeType type, boolean elliptic) {
				super(name, type, elliptic);
			}

		}

		private final JavaFunctionTemplate backingTemplate;

		private final LinkedList<ParameterInstance> parameters = new LinkedList<ParameterInstance>();

		public Instance(JavaFunctionTemplate backingTemplate, Location emissionLocation, UakeType returnType) {
			super(backingTemplate.getQualifiedName(), backingTemplate.getDefinitionLocation(),
					emissionLocation, returnType);
			this.backingTemplate = backingTemplate;
		}

		public JavaFunctionTemplate getBackingTemplate() {
			return backingTemplate;
		}

		protected Deque<? extends Parameter> getInternalParameters() {
			return parameters;
		}

		public void addParameter(ParameterInstance parameter) {
			if(parameter != null)
				parameters.add(parameter);
		}

	}

	private final LinkedList<JavaParameterTemplate> parameters = new LinkedList<JavaParameterTemplate>();

	private Class<?> enclosingClass;

	public JavaFunctionTemplate(QualifiedName qualifiedName, Location definitionLocation,
			UakeTypeEmitter returnType) {
		super(qualifiedName, definitionLocation, returnType);
	}

	public Class<?> getEnclosingClass() {
		return enclosingClass;
	}

	public void setEnclosingClass(Class<?> enclosingClass) {
		this.enclosingClass = enclosingClass;
	}

	protected Deque<? extends ParameterTemplate> getInternalParameters() {
		return parameters;
	}

	public void addParameter(JavaParameterTemplate parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

	public UakeFunction emitFunction(UakeType[] templateArguments, Location emissionLocation) {
		TypeUtils.checkTemplateArgumentsAgainstArity(this, templateArguments, emissionLocation);
		UakeType rtinstance = getReturnType().emitType(templateArguments, emissionLocation);
		Instance instance = new Instance(this, emissionLocation, rtinstance);
		for(int i = 0; i < templateArguments.length; ++i)
			instance.addTemplateArgument(templateArguments[i]);
		for(JavaParameterTemplate parameter : parameters)
			instance.addParameter(parameter.emitParameter(templateArguments, emissionLocation));
		return instance;
	}

}
