package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class WhileConstruct extends Expression {

	private boolean negated;

	private Expression condition;

	private Statement body;

	public WhileConstruct(Location initiator, boolean negated, Expression condition, Statement body) {
		super(initiator);
		this.negated = negated;
		this.condition = condition;
		this.body = body;
	}

	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public Statement getBody() {
		return body;
	}

	public void setBody(Statement body) {
		this.body = body;
	}

	public void bindTypes(CompilationContext cctx) {
		condition.bindTypes(cctx);
		body.bindTypes(cctx);
	}

}
