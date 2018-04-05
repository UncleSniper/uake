package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class BinaryOperation extends Expression {

	public enum Function {
		ASSIGN,
		PLUS_ASSIGN,
		MINUS_ASSIGN,
		MULTIPLY_ASSIGN,
		DIVIDE_ASSIGN,
		MODULO_ASSIGN,
		AND_ASSIGN,
		XOR_ASSIGN,
		OR_ASSIGN,
		SHIFT_LEFT_ASSIGN,
		SIGNED_SHIFT_RIGHT_ASSIGN,
		UNSIGNED_SHIFT_RIGHT_ASSIGN,
		LOGICAL_OR,
		LOGICAL_AND,
		BITWISE_OR,
		BITWISE_XOR,
		BITWISE_AND,
		IN,
		NOT_IN,
		EQUAL,
		UNEQUAL,
		LESS,
		LESS_EQUAL,
		GREATER,
		GREATER_EQUAL,
		SHIFT_LEFT,
		SIGNED_SHIFT_RIGHT,
		UNSIGNED_SHIFT_RIGHT,
		PLUS,
		MINUS,
		MULTIPLY,
		DIVIDE,
		MODULO,
		SUBSCRIPT
	}

	private Expression leftOperand;

	private Function function;

	private Expression rightOperand;

	public BinaryOperation(Location location, Expression leftOperand, Function function, Expression rightOperand) {
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

	public Expression getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(Expression rightOperand) {
		this.rightOperand = rightOperand;
	}

	public void createElements(CompilationContext cctx) {
		leftOperand.createElements(cctx);
		rightOperand.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		leftOperand.bindTypes(cctx);
		rightOperand.bindTypes(cctx);
	}

}
