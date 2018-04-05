package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.UakeType;
import org.unclesniper.uake.semantics.Transform;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.UakeScoped;
import org.unclesniper.uake.semantics.UakeTypeTemplate;
import org.unclesniper.uake.semantics.InstanceofTransform;
import org.unclesniper.uake.ElementReferenceTypeException;
import org.unclesniper.uake.semantics.TemplateArityFilter;
import org.unclesniper.uake.AmbiguousElementReferenceException;

public class TypeSpecifier extends Syntax {

	private static final Transform<UakeScoped, UakeType> SCOPED_TO_TYPE
			= new InstanceofTransform<UakeScoped, UakeType>(UakeType.class);

	private static final Transform<UakeScoped, UakeTypeTemplate> SCOPED_TO_TYPE_TEMPLATE
			= new InstanceofTransform<UakeScoped, UakeTypeTemplate>(UakeTypeTemplate.class);

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
			UakeModule.Group<UakeType> types = potentialBases.map(TypeSpecifier.SCOPED_TO_TYPE);
			if(types.isEmpty()) {
				QualifiedName.Segment tail = name.getTail();
				throw new ElementReferenceTypeException(tail.getLocation(), tail.getName(),
						potentialBases.getSize(), ElementReferenceTypeException.RequiredType.TYPE);
			}
			if(types.isAmbiguous()) {
				QualifiedName.Segment tail = name.getTail();
				throw new AmbiguousElementReferenceException(tail.getLocation(), tail.getName(),
						AmbiguousElementReferenceException.RequiredType.TYPE);
			}
			return types.getFirst();
		}
		else {
			UakeModule.Group<UakeTypeTemplate> templates = potentialBases.map(TypeSpecifier.SCOPED_TO_TYPE_TEMPLATE);
			if(templates.isEmpty()) {
				QualifiedName.Segment tail = name.getTail();
				throw new ElementReferenceTypeException(tail.getLocation(), tail.getName(),
						potentialBases.getSize(), ElementReferenceTypeException.RequiredType.TYPE_TEMPLATE);
			}
			UakeModule.Group<UakeTypeTemplate> matching
					= templates.select(new TemplateArityFilter<UakeTypeTemplate>(templateArguments.size()));
			if(matching.isEmpty()) {
				//TODO
			}
			if(matching.isAmbiguous()) {
				QualifiedName.Segment tail = name.getTail();
				throw new AmbiguousElementReferenceException(tail.getLocation(), tail.getName(),
						AmbiguousElementReferenceException.RequiredType.TYPE_TEMPLATE);
			}
			return matching.getFirst().emitType(null/*TODO*/, name.getTail().getLocation());
		}
	}

}
