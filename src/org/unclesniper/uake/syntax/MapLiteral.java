package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class MapLiteral extends Expression {

	public static class Binding extends Syntax {

		private Expression key;

		private Expression value;

		public Binding(Expression key, Location location, Expression value) {
			super(location);
			this.key = key;
			this.value = value;
		}

		public Expression getKey() {
			return key;
		}

		public Expression getValue() {
			return value;
		}

	}

	private final List<Binding> bindings = new LinkedList<Binding>();

	public MapLiteral(Location location) {
		super(location);
	}

	public Iterable<Binding> getBindings() {
		return bindings;
	}

	public void addBinding(Binding binding) {
		if(binding != null)
			bindings.add(binding);
	}

	public void createElements(CompilationContext cctx) {
		for(Binding binding : bindings) {
			binding.getKey().createElements(cctx);
			binding.getValue().createElements(cctx);
		}
	}

	public void bindTypes(CompilationContext cctx) {
		for(Binding binding : bindings) {
			binding.getKey().bindTypes(cctx);
			binding.getValue().bindTypes(cctx);
		}
	}

}
