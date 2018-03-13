package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class ModuleDefinition extends Definition {

	private QualifiedName name;

	private final List<TopLevel> children = new LinkedList<TopLevel>();

	public ModuleDefinition(Location initiator, QualifiedName name) {
		super(initiator);
		this.name = name;
	}

	public QualifiedName getName() {
		return name;
	}

	public void setName(QualifiedName name) {
		this.name = name;
	}

	public Iterable<TopLevel> getChildren() {
		return children;
	}

	public void addChild(TopLevel child) {
		if(child != null)
			children.add(child);
	}

}
