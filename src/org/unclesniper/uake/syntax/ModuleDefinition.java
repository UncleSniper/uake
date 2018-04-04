package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.AmbiguousTargetModuleException;

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

	public void createElements(CompilationContext cctx) {
		UakeModule.Group<UakeModule> modules = new UakeModule.Group<UakeModule>();
		modules.addMember(cctx.getDefinitionContext().getRootModule());
		String prevName = null;
		Location prevLocation = null;
		for(QualifiedName.Segment segment : name.getSegments()) {
			String uqname = segment.getName();
			if(prevName != null) {
				UakeModule.Group<UakeModule> nextModules = new UakeModule.Group<UakeModule>();
				for(UakeModule module : modules)
					module.gete(prevName).map(UakeModule.MODULE_TRANSFORM, nextModules);
				if(nextModules.isEmpty()) {
					for(UakeModule module : modules) {
						QualifiedName qname = new QualifiedName(module.getQualifiedName(), prevName, prevLocation);
						UakeModule inner = new UakeModule(qname, getLocation(), module);
						module.put(inner);
						nextModules.addMember(inner);
					}
				}
				else {
					for(UakeModule module : nextModules) {
						if(module.getDefinitionLocation() == null || module.getDefinitionLocation().isUnknown())
							module.setDefinitionLocation(getLocation());
					}
				}
				modules = nextModules;
			}
			prevName = uqname;
			prevLocation = segment.getLocation();
		}
		UakeModule.Group<UakeModule> finalModules = new UakeModule.Group<UakeModule>();
		for(UakeModule module : modules)
			module.gete(prevName).map(UakeModule.MODULE_TRANSFORM, finalModules);
		UakeModule targetModule;
		if(finalModules.isEmpty()) {
			if(modules.isAmbiguous())
				throw new AmbiguousTargetModuleException(getLocation(), name);
			UakeModule enclosing = modules.getFirst();
			QualifiedName qname = new QualifiedName(enclosing.getQualifiedName(), prevName, prevLocation);
			targetModule = new UakeModule(qname, getLocation(), enclosing);
			enclosing.put(targetModule);
		}
		else if(finalModules.isAmbiguous())
			throw new AmbiguousTargetModuleException(getLocation(), name);
		else
			targetModule = finalModules.getFirst();
		cctx.putModuleForDefinition(this, targetModule);
		UakeModule oldTarget = cctx.setTargetModule(targetModule);
		try {
			for(TopLevel child : children)
				child.createElements(cctx);
		}
		finally {
			cctx.setTargetModule(oldTarget);
		}
	}

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
