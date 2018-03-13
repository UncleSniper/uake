package org.unclesniper.uake.syntax;

public class Statement extends TopLevel {

	private final Expression expression;

	public Statement(Expression expression) {
		super(expression.getLocation());
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

}
