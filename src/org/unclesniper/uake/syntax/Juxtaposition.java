package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class Juxtaposition extends Expression {

	private Location requireLocation;

	private final List<Expression> pieces = new LinkedList<Expression>();

	public Juxtaposition(Location location, Location requireLocation) {
		super(location);
		this.requireLocation = requireLocation;
	}

	public Location getRequireLocation() {
		return requireLocation;
	}

	public void setRequireLocation(Location requireLocation) {
		this.requireLocation = requireLocation;
	}

	public Iterable<Expression> getPieces() {
		return pieces;
	}

	public void addPiece(Expression piece) {
		if(piece != null)
			pieces.add(piece);
	}

	public void createElements(CompilationContext cctx) {
		for(Expression piece : pieces)
			piece.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		for(Expression piece : pieces)
			piece.bindTypes(cctx);
	}

}
