package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;

public class ForConstruct extends Expression {

	public static abstract class Initialization extends Syntax {

		public Initialization(Location location) {
			super(location);
		}

	}

	public static class VariableInitialization extends Initialization {

		private VariableDefinition variable;

		public VariableInitialization(VariableDefinition variable) {
			super(variable.getLocation());
			this.variable = variable;
		}

		public VariableDefinition getVariable() {
			return variable;
		}

		public void setVariable(VariableDefinition variable) {
			this.variable = variable;
		}

	}

	public static class StatementInitialization extends Initialization {

		private Statement statement;

		public StatementInitialization(Statement statement) {
			super(statement.getLocation());
			this.statement = statement;
		}

		public Statement getStatement() {
			return statement;
		}

		public void setStatement(Statement statement) {
			this.statement = statement;
		}

	}

	private Initialization initialization;

	private Expression condition;

	private Expression update;

	private Statement body;

	public ForConstruct(Location initiator, Initialization initialization, Expression condition,
			Expression update, Statement body) {
		super(initiator);
		this.initialization = initialization;
		this.condition = condition;
		this.update = update;
		this.body = body;
	}

	public Initialization getInitialization() {
		return initialization;
	}

	public void setInitialization(Initialization initialization) {
		this.initialization = initialization;
	}

	public Expression getCondition() {
		return condition;
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public Expression getUpdate() {
		return update;
	}

	public void setUpdate(Expression update) {
		this.update = update;
	}

	public Statement getBody() {
		return body;
	}

	public void setBody(Statement body) {
		this.body = body;
	}

}
