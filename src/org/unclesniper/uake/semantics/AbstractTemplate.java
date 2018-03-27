package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;
import org.unclesniper.uake.syntax.TemplateParameter;

public abstract class AbstractTemplate extends AbstractMember implements UakeTemplate {

	private final List<TemplateParameter> templateParameters = new LinkedList<TemplateParameter>();

	public AbstractTemplate(QualifiedName qualifiedName, Location definitionLocation) {
		super(qualifiedName, definitionLocation);
	}

	public Iterable<TemplateParameter> getTemplateParameters() {
		return templateParameters;
	}

	public void addTemplateParameter(TemplateParameter parameter) {
		if(parameter != null)
			templateParameters.add(parameter);
	}

}
