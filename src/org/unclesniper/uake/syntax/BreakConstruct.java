package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class BreakConstruct extends Expression {

	public enum Semantics {
		BREAK,
		CONTINUE
	}

	private Semantics semantics;

	private String levelSpec;

	private Expression returnValue;

	public BreakConstruct(Location initiator, Semantics semantics, String levelSpec, Expression returnValue) {
		super(initiator);
		this.semantics = semantics;
		this.levelSpec = levelSpec;
		this.returnValue = returnValue;
	}

	public Semantics getSemantics() {
		return semantics;
	}

	public void setSemantics(Semantics semantics) {
		this.semantics = semantics;
	}

	public String getLevelSpec() {
		return levelSpec;
	}

	public void setLevelSpec(String levelSpec) {
		this.levelSpec = levelSpec;
	}

	public Expression getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Expression returnValue) {
		this.returnValue = returnValue;
	}

}
