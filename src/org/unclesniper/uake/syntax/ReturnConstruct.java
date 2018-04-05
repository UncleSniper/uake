package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class ReturnConstruct extends Expression {

	private Expression returnValue;

	public ReturnConstruct(Location initiator, Expression returnValue) {
		super(initiator);
		this.returnValue = returnValue;
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
