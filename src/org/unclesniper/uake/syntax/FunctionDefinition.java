package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;
import org.unclesniper.uake.semantics.UakeModule;
import org.unclesniper.uake.semantics.SoftFunction;
import org.unclesniper.uake.semantics.JavaFunction;
import org.unclesniper.uake.semantics.UakeVariable;
import org.unclesniper.uake.semantics.SoftFunctionTemplate;
import org.unclesniper.uake.semantics.JavaFunctionTemplate;

public class FunctionDefinition extends AbstractTemplate implements Parameterized, SoftCall {

	public static abstract class Body extends Syntax {

		public Body(Location location) {
			super(location);
		}

		public abstract void createElement(FunctionDefinition definition, CompilationContext cctx);

	}

	public static abstract class ScriptBody extends Body {

		private final List<PropertyTrigger> triggers = new LinkedList<PropertyTrigger>();

		public ScriptBody(Location location) {
			super(location);
		}

		public Iterable<PropertyTrigger> getTriggers() {
			return triggers;
		}

		public void addTrigger(PropertyTrigger trigger) {
			if(trigger != null)
				triggers.add(trigger);
		}

		public abstract Expression getExpression();

		public void createElement(FunctionDefinition definition, CompilationContext cctx) {
			UakeModule targetModule = cctx.getTargetModule();
			QualifiedName qname = new QualifiedName(targetModule.getQualifiedName(),
					definition.name, definition.nameLocation);
			boolean isTemplate = definition.isTemplate();
			if(isTemplate) {
				SoftFunctionTemplate function = new SoftFunctionTemplate(qname, definition.getLocation(), null);
				for(TemplateParameter tparam : definition.getTemplateParameters())
					function.addTemplateParameter(tparam);
				for(Parameter param : definition.getParameters())
					function.addParameter(new SoftFunctionTemplate.SoftParameterTemplate(param.getName(),
							null, param.isElliptic()));
				targetModule.put(function);
				cctx.putFunctionTemplateForDefinition(definition, function);
			}
			else {
				SoftFunction function = new SoftFunction(qname, definition.getLocation(), null);
				for(Parameter param : definition.getParameters())
					function.addParameter(new SoftFunction.SoftParameter(param.getName(), null, param.isElliptic()));
				targetModule.put(function);
				cctx.putFunctionForDefinition(definition, function);
			}
			UakeModule callModule = new UakeModule(qname, definition.getLocation(), targetModule);
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

	public static class BlockBody extends ScriptBody {

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

	public static class ExpressionBody extends ScriptBody {

		private final Expression expression;

		public ExpressionBody(Location initiator, Expression expression) {
			super(initiator);
			this.expression = expression;
		}

		public Expression getExpression() {
			return expression;
		}

	}

	public static class NativeBody extends Body {

		private final ClassReference classReference;

		private final boolean staticMethod;

		private final String methodName;

		private final Location methodNameLocation;

		public NativeBody(Location initiator, ClassReference classReference,
				boolean staticMethod, String methodName, Location methodNameLocation) {
			super(initiator);
			this.classReference = classReference;
			this.staticMethod = staticMethod;
			this.methodName = methodName;
			this.methodNameLocation = methodNameLocation;
		}

		public ClassReference getClassReference() {
			return classReference;
		}

		public String getMethodName() {
			return methodName;
		}

		public Location getMethodNameLocation() {
			return methodNameLocation;
		}

		public boolean isStaticMethod() {
			return staticMethod;
		}

		public void createElement(FunctionDefinition definition, CompilationContext cctx) {
			UakeModule targetModule = cctx.getTargetModule();
			QualifiedName qname = new QualifiedName(targetModule.getQualifiedName(),
					definition.name, definition.nameLocation);
			if(definition.isTemplate()) {
				JavaFunctionTemplate function = new JavaFunctionTemplate(qname, definition.getLocation(), null);
				for(TemplateParameter tparam : definition.getTemplateParameters())
					function.addTemplateParameter(tparam);
				for(Parameter param : definition.getParameters())
					function.addParameter(new JavaFunctionTemplate.JavaParameterTemplate(param.getName(),
							null, param.isElliptic()));
				targetModule.put(function);
				cctx.putFunctionTemplateForDefinition(definition, function);
			}
			else {
				JavaFunction function = new JavaFunction(qname, definition.getLocation(), null, null);
				for(Parameter param : definition.getParameters())
					function.addParameter(new JavaFunction.JavaParameter(param.getName(), null, param.isElliptic()));
				targetModule.put(function);
				cctx.putFunctionForDefinition(definition, function);
			}
		}

	}

	private TypeSpecifier returnType;

	private String name;

	private Location nameLocation;

	private final LinkedList<Parameter> parameters = new LinkedList<Parameter>();

	private Body body;

	public FunctionDefinition(Location initiator, TypeSpecifier returnType, String name, Location nameLocation) {
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

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void createElements(CompilationContext cctx) {
		body.createElement(this, cctx);
	}

	public void bindTypes(CompilationContext cctx) {
		//TODO
	}

}
