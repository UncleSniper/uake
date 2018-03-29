package org.unclesniper.uake;

import org.unclesniper.uake.syntax.QualifiedName;

public class AmbiguousTargetModuleException extends SemanticsException {

	private final QualifiedName moduleName;

	public AmbiguousTargetModuleException(Location location, QualifiedName moduleName) {
		super(location, "Target module '" + moduleName.toString(':') + "' is ambiguous at "
				+ (location == null ? Location.UNKNOWN : location));
		this.moduleName = moduleName;
	}

	public QualifiedName getModuleName() {
		return moduleName;
	}

}
