package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class PropertyDefinition extends AbstractTemplate implements Parameterized {

	public static class Bottom extends Syntax implements TemplateInvocation {

		private final QualifiedName name;

		private final List<TypeSpecifier> templateArguments = new LinkedList<TypeSpecifier>();

		public Bottom(Location initiator, QualifiedName name) {
			super(initiator);
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

	}

	private TypeSpecifier returnType;

	private String name;

	private Location nameLocation;

	private LinkedList<Parameter> parameters;

	private Bottom bottom;

	private final List<PropertyTrigger> triggers = new LinkedList<PropertyTrigger>();

	public PropertyDefinition(Location initiator, TypeSpecifier returnType, String name, Location nameLocation) {
		super(initiator);
		this.returnType = returnType;
		this.name = name;
		this.nameLocation = nameLocation;
	}

	public TypeSpecifier getReturnType() {
		return returnType;
	}

	public void setReturnType(TypeSpecifier returnType) {
		this.returnType = returnType;
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

	public Bottom getBottom() {
		return bottom;
	}

	public void setBottom(Bottom bottom) {
		this.bottom = bottom;
	}

	public Iterable<PropertyTrigger> getTriggers() {
		return triggers;
	}

	public void addTrigger(PropertyTrigger trigger) {
		if(trigger != null)
			triggers.add(trigger);
	}

	public void addParameter(Parameter parameter) {
		if(parameter == null)
			return;
		if(parameters == null)
			parameters = new LinkedList<Parameter>();
		parameters.add(parameter);
	}

	public void ensureParameters() {
		if(parameters == null)
			parameters = new LinkedList<Parameter>();
	}

	public Iterable<Parameter> getParameters() {
		return parameters;
	}

	public boolean isElliptic() {
		return parameters != null && !parameters.isEmpty() && parameters.getLast().isElliptic();
	}

	public void createElements(CompilationContext cctx) {
		//TODO
	}

}
