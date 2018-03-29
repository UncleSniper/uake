package org.unclesniper.uake.syntax;

import org.unclesniper.uake.CompilationContext;

public class Statement extends TopLevel {

	private final Expression expression;

	public Statement(Expression expression) {
		super(expression.getLocation());
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

	public void createElements(CompilationContext cctx) {}

}
