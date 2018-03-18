package org.unclesniper.uake.syntax;

public interface TemplateInvocation {

	Iterable<TypeSpecifier> getTemplateArguments();

	void addTemplateArgument(TypeSpecifier argument);

}
