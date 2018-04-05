package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class ListLiteral extends Expression {

	private final List<Expression> elements = new LinkedList<Expression>();

	public ListLiteral(Location location) {
		super(location);
	}

	public Iterable<Expression> getElements() {
		return elements;
	}

	public void addElement(Expression element) {
		if(element != null)
			elements.add(element);
	}

	public void createElements(CompilationContext cctx) {
		for(Expression element : elements)
			element.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		for(Expression element : elements)
			element.bindTypes(cctx);
	}

}
