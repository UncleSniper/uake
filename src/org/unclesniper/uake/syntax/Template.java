package org.unclesniper.uake.syntax;

public interface Template {

	void addTemplateParameter(TemplateParameter parameter);

	Iterable<TemplateParameter> getTemplateParameters();

	boolean isTemplate();

	boolean isEllipticTemplate();

}
