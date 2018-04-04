package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class NewConstruct extends Expression {

	public static class FieldInitializer extends Syntax {

		private String fieldName;

		private Expression value;

		public FieldInitializer(Location location, String fieldName, Expression value) {
			super(location);
			this.fieldName = fieldName;
			this.value = value;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public Expression getValue() {
			return value;
		}

		public void setValue(Expression value) {
			this.value = value;
		}

	}

	private TypeSpecifier type;

	private final List<Expression> arguments = new LinkedList<Expression>();

	private final List<FieldInitializer> fields = new LinkedList<FieldInitializer>();

	public NewConstruct(Location location, TypeSpecifier type) {
		super(location);
		this.type = type;
	}

	public TypeSpecifier getType() {
		return type;
	}

	public void setType(TypeSpecifier type) {
		this.type = type;
	}

	public Iterable<Expression> getArguments() {
		return arguments;
	}

	public void addArgument(Expression argument) {
		if(argument != null)
			arguments.add(argument);
	}

	public Iterable<FieldInitializer> getFields() {
		return fields;
	}

	public void addField(FieldInitializer field) {
		if(field != null)
			fields.add(field);
	}

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
