package org.unclesniper.uake.syntax;

import java.util.LinkedList;
import org.unclesniper.uake.Location;

public abstract class AbstractTemplate extends Definition implements Template {

	protected final LinkedList<TemplateParameter> templateParameters = new LinkedList<TemplateParameter>();

	public AbstractTemplate(Location location) {
		super(location);
	}

	public void addTemplateParameter(TemplateParameter parameter) {
		if(parameter != null)
			templateParameters.add(parameter);
	}

	public Iterable<TemplateParameter> getTemplateParameters() {
		return templateParameters;
	}

	public boolean isTemplate() {
		return !templateParameters.isEmpty();
	}

	public boolean isEllipticTemplate() {
		return !templateParameters.isEmpty() && templateParameters.getLast().isElliptic();
	}

}
