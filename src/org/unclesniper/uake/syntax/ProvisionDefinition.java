package org.unclesniper.uake.syntax;

import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class ProvisionDefinition extends AbstractTemplate implements Parameterized {

	public static abstract class Trigger extends Syntax {

		public Trigger(Location location) {
			super(location);
		}

	}

	public static class ConditionTrigger extends Trigger {

		private final Expression condition;

		public ConditionTrigger(Location initiator, Expression condition) {
			super(initiator);
			this.condition = condition;
		}

		public Expression getCondition() {
			return condition;
		}

	}

	public static class EquationTrigger extends Trigger {

		private final Expression referenceValue;

		public EquationTrigger(Location initiator, Expression referenceValue) {
			super(initiator);
			this.referenceValue = referenceValue;
		}

		public Expression getReferenceValue() {
			return referenceValue;
		}

	}

	public static abstract class Body extends Syntax {

		public Body(Location location) {
			super(location);
		}

	}

	public static class BlockBody extends Body {

		private final Block block;

		public BlockBody(Block block) {
			super(block.getLocation());
			this.block = block;
		}

		public Block getBlock() {
			return block;
		}

	}

	public static class ExpressionBody extends Body {

		private final Expression expression;

		public ExpressionBody(Location initiator, Expression expression) {
			super(initiator);
			this.expression = expression;
		}

		public Expression getExpression() {
			return expression;
		}

	}

	public static class CallBody extends Body {

		private final Expression function;

		public CallBody(Location initiator, Expression function) {
			super(initiator);
			this.function = function;
		}

		public Expression getFunction() {
			return function;
		}

	}

	private TypeSpecifier returnType;

	private String name;

	private Location nameLocation;

	private final LinkedList<Parameter> parameters = new LinkedList<Parameter>();

	private Trigger trigger;

	private Body body;

	public ProvisionDefinition(Location initiator, TypeSpecifier returnType, String name, Location nameLocation) {
		super(initiator);
		this.returnType = returnType;
		this.name = name;
		this.nameLocation = nameLocation;
	}

	public TypeSpecifier getReturnType() {
		return returnType;
	}

	public void setReturnType(TypeSpecifier returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(Location nameLocation) {
		this.nameLocation = nameLocation;
	}

	public Iterable<Parameter> getParameters() {
		return parameters;
	}

	public void addParameter(Parameter parameter) {
		if(parameter != null)
			parameters.add(parameter);
	}

	public boolean isElliptic() {
		return !parameters.isEmpty() && parameters.getLast().isElliptic();
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}
