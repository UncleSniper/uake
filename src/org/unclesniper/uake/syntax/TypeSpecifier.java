package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;

public class TypeSpecifier extends Syntax {

	private final QualifiedName name;

	private final List<TypeSpecifier> templateArguments = new LinkedList<TypeSpecifier>();

	public TypeSpecifier(QualifiedName name) {
		super(name.getLocation());
		this.name = name;
	}

	public QualifiedName getName() {
		return name;
	}

	public Iterable<TypeSpecifier> getTemplateArguments() {
		return templateArguments;
	}

	public void addTemplateArgument(TypeSpecifier argument) {
		if(argument != null)
			templateArguments.add(argument);
	}

	public boolean hasTemplateArguments() {
		return !templateArguments.isEmpty();
	}

	public int getTemplateArgumentCount() {
		return templateArguments.size();
	}

}
