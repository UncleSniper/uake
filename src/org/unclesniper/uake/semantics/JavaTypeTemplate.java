package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.syntax.QualifiedName;

public class JavaTypeTemplate extends AbstractTemplate implements UakeTypeTemplate {

	public static class Instance extends AbstractTemplateInstance implements UakeType {

		private final JavaTypeTemplate backingTemplate;

		public Instance(JavaTypeTemplate backingTemplate) {
			super(backingTemplate.getQualifiedName());
			this.backingTemplate = backingTemplate;
		}

		public JavaTypeTemplate getBackingTemplate() {
			return backingTemplate;
		}

		public Class<?> getBackingClass() {
			return backingTemplate.getBackingClass();
		}

		public UakeModule.Group<UakeType> getDirectSupertypes() {
			UakeType[] templateArguments = TypeUtils.getTemplateArgumentsAsArray(this);
			UakeModule.Group<UakeType> supertypes = new UakeModule.Group<UakeType>();
			for(UakeTypeEmitter supertype : backingTemplate.directSupertypes)
				supertypes.addMember(supertype.emitType(templateArguments));
			return supertypes;
		}

	}

	private Class<?> backingClass;

	private final List<UakeTypeEmitter> directSupertypes = new LinkedList<UakeTypeEmitter>();

	public JavaTypeTemplate(QualifiedName qualifiedName, Class<?> backingClass) {
		super(qualifiedName);
		this.backingClass = backingClass;
	}

	public Class<?> getBackingClass() {
		return backingClass;
	}

	public void setBackingClass(Class<?> backingClass) {
		this.backingClass = backingClass;
	}

	public Iterable<UakeTypeEmitter> getDirectSupertypes() {
		return directSupertypes;
	}

	public void addDirectSupertype(UakeTypeEmitter supertype) {
		if(supertype != null)
			directSupertypes.add(supertype);
	}

	public UakeType emitType(UakeType... templateArguments) {
		//TODO
		return null;
	}

}
