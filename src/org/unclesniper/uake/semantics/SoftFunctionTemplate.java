package org.unclesniper.uake.semantics;

import java.util.Deque;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class SoftFunctionTemplate extends AbstractFunctionTemplate {

	public static class SoftParameterTemplate extends AbstractParameterTemplate {

		public SoftParameterTemplate(String name, UakeTypeEmitter type, boolean elliptic) {
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

		private final SoftFunctionTemplate backingTemplate;

		private final LinkedList<ParameterInstance> parameters = new LinkedList<ParameterInstance>();

		public Instance(SoftFunctionTemplate backingTemplate, Location emissionLocation, UakeType returnType) {
			super(backingTemplate.getQualifiedName(), backingTemplate.getDefinitionLocation(),
					emissionLocation, returnType);
			this.backingTemplate = backingTemplate;
		}

		public SoftFunctionTemplate getBackingTemplate() {
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

	private final LinkedList<SoftParameterTemplate> parameters = new LinkedList<SoftParameterTemplate>();

	public SoftFunctionTemplate(QualifiedName qualifiedName, Location definitionLocation,
			UakeTypeEmitter returnType) {
		super(qualifiedName, definitionLocation, returnType);
	}

	protected Deque<? extends ParameterTemplate> getInternalParameters() {
		return parameters;
	}

	public void addParameter(SoftParameterTemplate parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

	public UakeFunction emitFunction(UakeType[] templateArguments, Location emissionLocation) {
		TypeUtils.checkTemplateArgumentsAgainstArity(this, templateArguments, emissionLocation);
		UakeType rtinstance = getReturnType().emitType(templateArguments, emissionLocation);
		Instance instance = new Instance(this, emissionLocation, rtinstance);
		for(int i = 0; i < templateArguments.length; ++i)
			instance.addTemplateArgument(templateArguments[i]);
		for(SoftParameterTemplate parameter : parameters)
			instance.addParameter(parameter.emitParameter(templateArguments, emissionLocation));
		return instance;
	}

}
