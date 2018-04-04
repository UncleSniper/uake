package org.unclesniper.uake.semantics;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public interface UakeMember extends UakeScoped {

	String getUnqualifiedName();

	QualifiedName getQualifiedName();

	Location getDefinitionLocation();

	Iterable<UakeType> getTemplateArguments();

}
