package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.UakeType;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.UakeScoped;

public class TypeSpecifier extends Syntax {

	private final QualifiedName name;

	private final List<TypeSpecifier> templateArguments = new LinkedList<TypeSpecifier>();

	public TypeSpecifier(QualifiedName name) {
		super(name.getLocation());
		this.name = name;
	}

	public QualifiedName getName() {
		return name;
	}

	public Iterable<TypeSpecifier> getTemplateArguments() {
		return templateArguments;
	}

	public void addTemplateArgument(TypeSpecifier argument) {
		if(argument != null)
			templateArguments.add(argument);
	}

	public boolean hasTemplateArguments() {
		return !templateArguments.isEmpty();
	}

	public int getTemplateArgumentCount() {
		return templateArguments.size();
	}

	public UakeType bindType(CompilationContext cctx) {
		UakeModule.Group<UakeScoped> potentialBases = cctx.resolveNameFail(name);
		if(templateArguments.isEmpty()) {
			//TODO
		}
		else {
			//TODO
		}
		return null; //TODO
	}

}
