package org.unclesniper.uake.semantics;

import java.util.Deque;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class ProvisionTemplate extends AbstractFunctionTemplate {

	public static class Instance extends AbstractFunctionTemplateInstance {

		private final ProvisionTemplate backingTemplate;

		private final LinkedList<SoftFunctionTemplate.Instance.ParameterInstance> parameters
				= new LinkedList<SoftFunctionTemplate.Instance.ParameterInstance>();

		public Instance(ProvisionTemplate backingTemplate, Location emissionLocation, UakeType returnType) {
			super(backingTemplate.getQualifiedName(), backingTemplate.getDefinitionLocation(),
					emissionLocation, returnType);
			this.backingTemplate = backingTemplate;
		}

		public ProvisionTemplate getBackingTemplate() {
			return backingTemplate;
		}

		protected Deque<? extends Parameter> getInternalParameters() {
			return parameters;
		}

		public void addParameter(SoftFunctionTemplate.Instance.ParameterInstance parameter) {
			if(parameter != null)
				parameters.add(parameter);
		}

	}

	private final LinkedList<SoftFunctionTemplate.SoftParameterTemplate> parameters
			= new LinkedList<SoftFunctionTemplate.SoftParameterTemplate>();

	public ProvisionTemplate(QualifiedName qualifiedName, Location definitionLocation, UakeTypeEmitter returnType) {
		super(qualifiedName, definitionLocation, returnType);
	}

	protected Deque<? extends ParameterTemplate> getInternalParameters() {
		return parameters;
	}

	public void addParameter(SoftFunctionTemplate.SoftParameterTemplate parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

	public UakeFunction emitFunction(UakeType[] templateArguments, Location emissionLocation) {
		TypeUtils.checkTemplateArgumentsAgainstArity(this, templateArguments, emissionLocation);
		UakeType rtinstance = getReturnType().emitType(templateArguments, emissionLocation);
		Instance instance = new Instance(this, emissionLocation, rtinstance);
		for(int i = 0; i < templateArguments.length; ++i)
			instance.addTemplateArgument(templateArguments[i]);
		for(SoftFunctionTemplate.SoftParameterTemplate parameter : parameters)
			instance.addParameter(parameter.emitParameter(templateArguments, emissionLocation));
		return instance;
	}

}
