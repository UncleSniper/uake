package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class Block extends Expression {

	public static abstract class Item extends Syntax {

		public Item(Location location) {
			super(location);
		}

		public abstract void createElements(CompilationContext cctx);

		public abstract void bindTypes(CompilationContext cctx);

	}

	public static class StatementItem extends Item {

		private final Statement statement;

		public StatementItem(Statement statement) {
			super(statement.getLocation());
			this.statement = statement;
		}

		public Statement getStatement() {
			return statement;
		}

		public void createElements(CompilationContext cctx) {
			statement.createElements(cctx);
		}

		public void bindTypes(CompilationContext cctx) {
			statement.bindTypes(cctx);
		}

	}

	public static class VariableItem extends Item {

		private final VariableDefinition variable;

		public VariableItem(VariableDefinition variable) {
			super(variable.getLocation());
			this.variable = variable;
		}

		public VariableDefinition getVariable() {
			return variable;
		}

		public void createElements(CompilationContext cctx) {
			variable.createElements(cctx);
		}

		public void bindTypes(CompilationContext cctx) {
			variable.bindTypes(cctx);
		}

	}

	private final List<Item> items = new LinkedList<Item>();

	public Block(Location location) {
		super(location);
	}

	public Iterable<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		if(item != null)
			items.add(item);
	}

	public void createElements(CompilationContext cctx) {
		for(Item item : items)
			item.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		for(Item item : items)
			item.bindTypes(cctx);
	}

}
