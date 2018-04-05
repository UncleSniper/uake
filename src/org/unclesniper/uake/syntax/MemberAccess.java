package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class MemberAccess extends Expression {

	public enum Semantics {
		FIELD,
		MODULE_MEMBER
	}

	private Expression operand;

	private Semantics semantics;

	private String memberName;

	private Location memberLocation;

	public MemberAccess(Location location, Expression operand, Semantics semantics,
			String memberName, Location memberLocation) {
		super(location);
		this.operand = operand;
		this.semantics = semantics;
		this.memberName = memberName;
		this.memberLocation = memberLocation;
	}

	public Expression getOperand() {
		return operand;
	}

	public void setOperand(Expression operand) {
		this.operand = operand;
	}

	public Semantics getSemantics() {
		return semantics;
	}

	public void setSemantics(Semantics semantics) {
		this.semantics = semantics;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Location getMemberLocation() {
		return memberLocation;
	}

	public void setMemberLocation(Location memberLocation) {
		this.memberLocation = memberLocation;
	}

	public void createElements(CompilationContext cctx) {
		operand.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		operand.bindTypes(cctx);
	}

}
