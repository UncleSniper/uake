package org.unclesniper.uake.syntax;

public class QualifiedClassName extends ClassReference {

	private final QualifiedName qualifiedName;

	public QualifiedClassName(QualifiedName qualifiedName) {
		super(qualifiedName.getLocation());
		this.qualifiedName = qualifiedName;
	}

	public QualifiedName getQualifiedName() {
		return qualifiedName;
	}

}
