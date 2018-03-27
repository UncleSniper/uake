package org.unclesniper.uake.semantics;

import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public abstract class AbstractMember implements UakeMember {

	public static final Iterable<UakeType> NO_TEMPLATE_ARGUMENTS = new LinkedList<UakeType>();

	private final QualifiedName qualifiedName;

	private Location definitionLocation;

	public AbstractMember(QualifiedName qualifiedName, Location definitionLocation) {
		this.qualifiedName = qualifiedName;
		this.definitionLocation = definitionLocation;
	}

	public String getUnqualifiedName() {
		QualifiedName.Segment tail = qualifiedName.getTail();
		return tail == null ? null : tail.getName();
	}

	public QualifiedName getQualifiedName() {
		return qualifiedName;
	}

	public Location getDefinitionLocation() {
		return definitionLocation;
	}

	public void setDefinitionLocation(Location definitionLocation) {
		this.definitionLocation = definitionLocation;
	}

	public Iterable<UakeType> getTemplateArguments() {
		return AbstractMember.NO_TEMPLATE_ARGUMENTS;
	}

}
