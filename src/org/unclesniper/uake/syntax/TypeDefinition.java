package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.JavaType;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.RecordType;
import org.unclesniper.uake.semantics.JavaTypeTemplate;
import org.unclesniper.uake.semantics.RecordTypeTemplate;

public class TypeDefinition extends AbstractTemplate {

	public static abstract class Body extends Syntax {

		public Body(Location location) {
			super(location);
		}

		public abstract void createElement(TypeDefinition definition, CompilationContext cctx);

	}

	public static class FieldsBody extends Body {

		public static class Field extends Syntax {

			private final TypeSpecifier type;

			private final String name;

			public Field(TypeSpecifier type, Location nameLocation, String name) {
				super(nameLocation);
				this.type = type;
				this.name = name;
			}

			public TypeSpecifier getType() {
				return type;
			}

			public String getName() {
				return name;
			}

		}

		private final List<Field> fields = new LinkedList<Field>();

		public FieldsBody(Location location) {
			super(location);
		}

		public Iterable<Field> getFields() {
			return fields;
		}

		public void addField(Field field) {
			if(field != null)
				fields.add(field);
		}

		public void createElement(TypeDefinition definition, CompilationContext cctx) {
			UakeModule targetModule = cctx.getTargetModule();
			QualifiedName qname = new QualifiedName(targetModule.getQualifiedName(),
					definition.name, definition.nameLocation);
			if(definition.isTemplate()) {
				RecordTypeTemplate type = new RecordTypeTemplate(qname, definition.getLocation());
				for(TemplateParameter tparam : definition.getTemplateParameters())
					type.addTemplateParameter(tparam);
				targetModule.put(type);
				cctx.putTypeTemplateForDefinition(definition, type);
			}
			else {
				RecordType type = new RecordType(qname, definition.getLocation());
				targetModule.put(type);
				cctx.putTypeForDefinition(definition, type);
			}
		}

	}

	public static class NativeBody extends Body {

		private final ClassReference classReference;

		public NativeBody(Location initiator, ClassReference classReference) {
			super(initiator);
			this.classReference = classReference;
		}

		public ClassReference getClassReference() {
			return classReference;
		}

		public void createElement(TypeDefinition definition, CompilationContext cctx) {
			UakeModule targetModule = cctx.getTargetModule();
			QualifiedName qname = new QualifiedName(targetModule.getQualifiedName(),
					definition.name, definition.nameLocation);
			if(definition.isTemplate()) {
				JavaTypeTemplate type = new JavaTypeTemplate(qname, definition.getLocation(), null);
				for(TemplateParameter tparam : definition.getTemplateParameters())
					type.addTemplateParameter(tparam);
				targetModule.put(type);
				cctx.putTypeTemplateForDefinition(definition, type);
			}
			else {
				JavaType type = new JavaType(qname, definition.getLocation(), null);
				targetModule.put(type);
				cctx.putTypeForDefinition(definition, type);
			}
		}

	}

	private String name;

	private Location nameLocation;

	private final List<TypeSpecifier> supertypes = new LinkedList<TypeSpecifier>();

	private Body body;

	public TypeDefinition(Location initiator, String name, Location nameLocation, Body body) {
		super(initiator);
		this.name = name;
		this.nameLocation = nameLocation;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(Location nameLocation) {
		this.nameLocation = nameLocation;
	}

	public Iterable<TypeSpecifier> getSupertypes() {
		return supertypes;
	}

	public void addSupertype(TypeSpecifier supertype) {
		if(supertype != null)
			supertypes.add(supertype);
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void createElements(CompilationContext cctx) {
		body.createElement(this, cctx);
	}

}
