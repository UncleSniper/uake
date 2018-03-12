package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class ModuleImport extends Header {

	private QualifiedName moduleName;

	public ModuleImport(Location location, QualifiedName moduleName) {
		super(location);
		this.moduleName = moduleName;
	}

	public QualifiedName getModuleName() {
		return moduleName;
	}

	public void setModuleName(QualifiedName moduleName) {
		this.moduleName = moduleName;
	}

}
