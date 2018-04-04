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

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
