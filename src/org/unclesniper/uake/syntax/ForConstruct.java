package org.unclesniper.uake.syntax;

import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.UakeVariable;
import org.unclesniper.uake.semantics.OverlayLevel;

public class ForConstruct extends Expression implements BindingExpression {

	public static abstract class Initialization extends Syntax {

		public Initialization(Location location) {
			super(location);
		}

		public abstract void createElements(ForConstruct construct, CompilationContext cctx);

		public abstract void beginBindTypes(ForConstruct construct, CompilationContext cctx);

		public abstract void endBindTypes(ForConstruct construct, CompilationContext cctx);

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

		public void createElements(ForConstruct construct, CompilationContext cctx) {
			Expression initializer = variable.getInitializer();
			if(initializer != null)
				initializer.createElements(cctx);
		}

		public void beginBindTypes(ForConstruct construct, CompilationContext cctx) {
			UakeVariable bindVar = new UakeVariable(new QualifiedName(variable.getName(),
					variable.getNameLocation()), variable.getLocation(), null, variable.isConstant());
			cctx.putVariableForBindingExpression(construct, bindVar);
			OverlayLevel scope = new OverlayLevel();
			scope.put(bindVar);
			cctx.putScopeForBindingExpression(construct, scope);
			Expression initializer = variable.getInitializer();
			if(initializer != null)
				initializer.bindTypes(cctx);
			cctx.pushScope(scope);
		}

		public void endBindTypes(ForConstruct construct, CompilationContext cctx) {
			cctx.popScope();
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

		public void createElements(ForConstruct construct, CompilationContext cctx) {
			statement.createElements(cctx);
		}

		public void beginBindTypes(ForConstruct construct, CompilationContext cctx) {
			statement.bindTypes(cctx);
		}

		public void endBindTypes(ForConstruct construct, CompilationContext cctx) {}

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

	public void createElements(CompilationContext cctx) {
		if(initialization != null)
			initialization.createElements(this, cctx);
		if(condition != null)
			condition.createElements(cctx);
		if(update != null)
			update.createElements(cctx);
		body.createElements(cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		if(initialization != null)
			initialization.beginBindTypes(this, cctx);
		try {
			if(condition != null)
				condition.bindTypes(cctx);
			if(update != null)
				update.bindTypes(cctx);
			body.bindTypes(cctx);
		}
		finally {
			if(initialization != null)
				initialization.endBindTypes(this, cctx);
		}
	}

}
