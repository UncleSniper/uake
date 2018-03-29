package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.TemplateParameter;

public class TemplateParameterReference implements UakeTypeEmitter {

	private TemplateParameter parameter;

	private int argumentIndex;

	public TemplateParameterReference(TemplateParameter parameter, int argumentIndex) {
		this.parameter = parameter;
		this.argumentIndex = argumentIndex;
	}

	public TemplateParameter getParameter() {
		return parameter;
	}

	public void setParameter(TemplateParameter parameter) {
		this.parameter = parameter;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	public void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}

	public UakeType emitType(UakeType[] templateArguments, Location emissionLocation) {
		return templateArguments[argumentIndex];
	}

}
