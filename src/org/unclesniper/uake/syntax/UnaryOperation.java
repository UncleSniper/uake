package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class UnaryOperation extends Expression {

	public enum Function {
		PRE_INCREMENT,
		PRE_DECREMENT,
		POSITIVE,
		NEGATIVE,
		BITWISE_NOT,
		LOGICAL_NOT,
		POST_INCREMENT,
		POST_DECREMENT
	}

	private Function function;

	private Expression operand;

	public UnaryOperation(Location location, Function function, Expression operand) {
		super(location);
		this.function = function;
		this.operand = operand;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public Expression getOperand() {
		return operand;
	}

	public void setOperand(Expression operand) {
		this.operand = operand;
	}

	public void createElements(CompilationContext cctx) {
		operand.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		operand.bindTypes(cctx);
	}

}
