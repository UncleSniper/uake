package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class JavaTypeTemplate extends AbstractTypeTemplate {

	public static class Instance extends AbstractTypeTemplateInstance {

		private final JavaTypeTemplate backingTemplate;

		public Instance(JavaTypeTemplate backingTemplate, Location emissionLocation) {
			super(backingTemplate.getQualifiedName(), backingTemplate.getDefinitionLocation(), emissionLocation);
			this.backingTemplate = backingTemplate;
		}

		public JavaTypeTemplate getBackingTemplate() {
			return backingTemplate;
		}

		public Class<?> getBackingClass() {
			return backingTemplate.getBackingClass();
		}

	}

	private Class<?> backingClass;

	public JavaTypeTemplate(QualifiedName qualifiedName, Location definitionLocation, Class<?> backingClass) {
		super(qualifiedName, definitionLocation);
		this.backingClass = backingClass;
	}

	public Class<?> getBackingClass() {
		return backingClass;
	}

	public void setBackingClass(Class<?> backingClass) {
		this.backingClass = backingClass;
	}

	public UakeType emitType(UakeType[] templateArguments, Location emissionLocation) {
		TypeUtils.checkTemplateArgumentsAgainstArity(this, templateArguments, emissionLocation);
		Instance instance = new Instance(this, emissionLocation);
		for(int i = 0; i < templateArguments.length; ++i)
			instance.addTemplateArgument(templateArguments[i]);
		return instance;
	}

}
