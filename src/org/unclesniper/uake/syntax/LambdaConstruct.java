package org.unclesniper.uake.syntax;

import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class LambdaConstruct extends Expression implements Parameterized {

	private TypeSpecifier returnType;

	private final LinkedList<Parameter> parameters = new LinkedList<Parameter>();

	private Expression body;

	public LambdaConstruct(Location initiator, TypeSpecifier returnType) {
		super(initiator);
		this.returnType = returnType;
	}

	public TypeSpecifier getReturnType() {
		return returnType;
	}

	public void setReturnType(TypeSpecifier returnType) {
		this.returnType = returnType;
	}

	public Iterable<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(Parameter parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

	public boolean isElliptic() {
		return !parameters.isEmpty() && parameters.getLast().isElliptic();
	}

	public Expression getBody() {
		return body;
	}

	public void setBody(Expression body) {
		this.body = body;
	}

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
