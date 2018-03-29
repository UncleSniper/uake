package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class DependentType implements UakeTypeEmitter {

	private UakeTypeTemplate dependentTemplate;

	private final List<UakeTypeEmitter> templateArguments = new LinkedList<UakeTypeEmitter>();

	public DependentType(UakeTypeTemplate dependentTemplate) {
		this.dependentTemplate = dependentTemplate;
	}

	public UakeTypeTemplate getDependentTemplate() {
		return dependentTemplate;
	}

	public void setDependentTemplate(UakeTypeTemplate dependentTemplate) {
		this.dependentTemplate = dependentTemplate;
	}

	public Iterable<UakeTypeEmitter> getTemplateArguments() {
		return templateArguments;
	}

	public void addTemplateArgument(UakeTypeEmitter argument) {
		if(argument != null)
			templateArguments.add(argument);
	}

	public UakeType emitType(UakeType[] innerArguments, Location emissionLocation) {
		UakeType[] outerArguments = new UakeType[templateArguments.size()];
		int index = 0;
		for(UakeTypeEmitter argument : templateArguments)
			outerArguments[index++] = argument.emitType(innerArguments, emissionLocation);
		return dependentTemplate.emitType(outerArguments, emissionLocation);
	}

}
