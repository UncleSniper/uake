package org.unclesniper.uake.semantics;

import org.unclesniper.uake.syntax.TemplateParameter;

public interface UakeTemplate extends UakeMember {

	Iterable<TemplateParameter> getTemplateParameters();

	boolean isEllipticTemplate();

	int getMinTemplateArity();

	int getMaxTemplateArity();

}
