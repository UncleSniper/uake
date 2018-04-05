package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class IfConstruct extends Expression {

	public static class ElseIf extends Syntax {

		private boolean negated;

		private Expression condition;

		private Statement thenBranch;

		public ElseIf(Location initiator, boolean negated, Expression condition, Statement thenBranch) {
			super(initiator);
			this.negated = negated;
			this.condition = condition;
			this.thenBranch = thenBranch;
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

		public Statement getThenBranch() {
			return thenBranch;
		}

		public void setThenBranch(Statement thenBranch) {
			this.thenBranch = thenBranch;
		}

	}

	private boolean negated;

	private Expression condition;

	private Statement thenBranch;

	private final List<ElseIf> elseIfs = new LinkedList<ElseIf>();

	private Location elseLocation;

	private Statement elseBranch;

	public IfConstruct(Location initiator, boolean negated, Expression condition, Statement thenBranch) {
		super(initiator);
		this.negated = negated;
		this.condition = condition;
		this.thenBranch = thenBranch;
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

	public Statement getThenBranch() {
		return thenBranch;
	}

	public void setThenBranch(Statement thenBranch) {
		this.thenBranch = thenBranch;
	}

	public Iterable<ElseIf> getElseIfs() {
		return elseIfs;
	}

	public void addElseIf(ElseIf elseIf) {
		if(elseIf != null)
			elseIfs.add(elseIf);
	}

	public Location getElseLocation() {
		return elseLocation;
	}

	public void setElseLocation(Location elseLocation) {
		this.elseLocation = elseLocation;
	}

	public Statement getElseBranch() {
		return elseBranch;
	}

	public void setElseBranch(Statement elseBranch) {
		this.elseBranch = elseBranch;
	}

	public void createElements(CompilationContext cctx) {
		condition.createElements(cctx);
		thenBranch.createElements(cctx);
		for(ElseIf elseIf : elseIfs) {
			elseIf.getCondition().createElements(cctx);
			elseIf.getThenBranch().createElements(cctx);
		}
		if(elseBranch != null)
			elseBranch.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		condition.bindTypes(cctx);
		thenBranch.bindTypes(cctx);
		for(ElseIf elseIf : elseIfs) {
			elseIf.getCondition().bindTypes(cctx);
			elseIf.getThenBranch().bindTypes(cctx);
		}
		if(elseBranch != null)
			elseBranch.bindTypes(cctx);
	}

}
