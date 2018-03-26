package org.unclesniper.uake.semantics;

import java.util.LinkedList;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractMember implements UakeMember {

	public static final Iterable<UakeType> NO_TEMPLATE_ARGUMENTS = new LinkedList<UakeType>();

	private final QualifiedName qualifiedName;

	public AbstractMember(QualifiedName qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	public String getUnqualifiedName() {
		QualifiedName.Segment tail = qualifiedName.getTail();
		return tail == null ? null : tail.getName();
	}

	public QualifiedName getQualifiedName() {
		return qualifiedName;
	}

	public Iterable<UakeType> getTemplateArguments() {
		return AbstractMember.NO_TEMPLATE_ARGUMENTS;
	}

}
