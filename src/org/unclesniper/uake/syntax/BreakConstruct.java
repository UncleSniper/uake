package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class BreakConstruct extends Expression {

	public enum Semantics {
		BREAK,
		CONTINUE
	}

	private Semantics semantics;

	private String levelSpec;

	private Location levelLocation;

	private Expression returnValue;

	public BreakConstruct(Location initiator, Semantics semantics, String levelSpec, Location levelLocation,
			Expression returnValue) {
		super(initiator);
		this.semantics = semantics;
		this.levelSpec = levelSpec;
		this.levelLocation = levelLocation;
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

	public Location getLevelLocation() {
		return levelLocation;
	}

	public void setLevelLocation(Location levelLocation) {
		this.levelLocation = levelLocation;
	}

	public Expression getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Expression returnValue) {
		this.returnValue = returnValue;
	}

	public void createElements(CompilationContext cctx) {
		if(returnValue != null)
			returnValue.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		if(returnValue != null)
			returnValue.bindTypes(cctx);
	}

}
