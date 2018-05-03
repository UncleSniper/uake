package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class InstanceofOperation extends Expression {

	public enum Function {
		IS,
		AS
	}

	private Expression leftOperand;

	private Function function;

	private TypeSpecifier rightOperand;

	public InstanceofOperation(Location location,
			Expression leftOperand, Function function, TypeSpecifier rightOperand) {
		super(location);
		this.leftOperand = leftOperand;
		this.function = function;
		this.rightOperand = rightOperand;
	}

	public Expression getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(Expression leftOperand) {
		this.leftOperand = leftOperand;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public TypeSpecifier getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(TypeSpecifier rightOperand) {
		this.rightOperand = rightOperand;
	}

	public void createElements(CompilationContext cctx) {
		leftOperand.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		leftOperand.bindTypes(cctx);
		//TODO: rightOperand.bindType(cctx)
	}

}
