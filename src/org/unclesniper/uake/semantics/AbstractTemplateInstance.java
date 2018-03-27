package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractTemplateInstance extends AbstractMember {

	private final List<UakeType> templateArguments = new LinkedList<UakeType>();

	public AbstractTemplateInstance(QualifiedName qualifiedName, Location definitionLocation) {
		super(qualifiedName, definitionLocation);
	}

	public Iterable<UakeType> getTemplateArguments() {
		return AbstractMember.NO_TEMPLATE_ARGUMENTS;
	}

	public void addTemplateArgument(UakeType argument) {
		if(argument != null)
			templateArguments.add(argument);
	}

}
