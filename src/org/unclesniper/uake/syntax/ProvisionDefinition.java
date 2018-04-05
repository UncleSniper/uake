package org.unclesniper.uake.syntax;

import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.Provision;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.SoftFunction;
import org.unclesniper.uake.semantics.UakeVariable;
import org.unclesniper.uake.semantics.ProvisionTemplate;
import org.unclesniper.uake.semantics.SoftFunctionTemplate;

public class ProvisionDefinition extends AbstractTemplate implements Parameterized, SoftCall {

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

		public abstract void createElements(ProvisionDefinition definition, QualifiedName qname,
				CompilationContext cctx);

	}

	public static abstract class SoftCallBody extends Body {

		public SoftCallBody(Location location) {
			super(location);
		}

		public abstract Expression getExpression();

		public void createElements(ProvisionDefinition definition, QualifiedName qname, CompilationContext cctx) {
			UakeModule callModule = new UakeModule(qname, definition.getLocation(), cctx.getTargetModule());
			for(Parameter param : definition.getParameters()) {
				UakeVariable paramVar = new UakeVariable(new QualifiedName(qname,
						param.getName(), param.getLocation()), param.getLocation(), null, false);
				cctx.putVariableForParameter(param, paramVar);
				callModule.put(paramVar);
			}
			cctx.putModuleForSoftCall(definition, callModule);
			UakeModule oldTarget = cctx.setTargetModule(callModule);
			try {
				getExpression().createElements(cctx);
			}
			finally {
				cctx.setTargetModule(oldTarget);
			}
		}

	}

	public static class BlockBody extends SoftCallBody {

		private final Block block;

		public BlockBody(Block block) {
			super(block.getLocation());
			this.block = block;
		}

		public Block getBlock() {
			return block;
		}

		public Expression getExpression() {
			return block;
		}

	}

	public static class ExpressionBody extends SoftCallBody {

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

		public void createElements(ProvisionDefinition definition, QualifiedName qname, CompilationContext cctx) {
			function.createElements(cctx);
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

	public void createElements(CompilationContext cctx) {
		UakeModule targetModule = cctx.getTargetModule();
		QualifiedName qname = new QualifiedName(targetModule.getQualifiedName(), name, nameLocation);
		if(isTemplate()) {
			ProvisionTemplate provision = new ProvisionTemplate(qname, getLocation(), null);
			for(TemplateParameter tparam : getTemplateParameters())
				provision.addTemplateParameter(tparam);
			for(Parameter param : getParameters())
				provision.addParameter(new SoftFunctionTemplate.SoftParameterTemplate(param.getName(),
						null, param.isElliptic()));
			targetModule.put(provision);
			cctx.putProvisionTemplateForDefinition(this, provision);
		}
		else {
			Provision provision = new Provision(qname, getLocation(), null);
			for(Parameter param : getParameters())
				provision.addParameter(new SoftFunction.SoftParameter(param.getName(), null, param.isElliptic()));
			targetModule.put(provision);
			cctx.putProvisionForDefinition(this, provision);
		}
		body.createElements(this, qname, cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
