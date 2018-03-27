package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public interface UakeMember {

	String getUnqualifiedName();

	QualifiedName getQualifiedName();

	Location getDefinitionLocation();

	Iterable<UakeType> getTemplateArguments();

}
