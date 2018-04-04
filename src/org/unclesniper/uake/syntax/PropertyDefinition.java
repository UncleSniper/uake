package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.Property;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.SoftFunction;

public class PropertyDefinition extends Definition implements Parameterized {

	private TypeSpecifier returnType;

	private String name;

	private Location nameLocation;

	private LinkedList<Parameter> parameters;

	private Expression bottom;

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

	public Expression getBottom() {
		return bottom;
	}

	public void setBottom(Expression bottom) {
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
		UakeModule targetModule = cctx.getTargetModule();
		QualifiedName qname = new QualifiedName(targetModule.getQualifiedName(), name, nameLocation);
		Property property = new Property(qname, getLocation(), null);
		for(Parameter param : parameters)
			property.addParameter(new SoftFunction.SoftParameter(param.getName(), null, param.isElliptic()));
		targetModule.put(property);
		cctx.putPropertyForDefinition(this, property);
	}

}
