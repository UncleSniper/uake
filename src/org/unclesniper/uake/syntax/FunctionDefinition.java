package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class FunctionDefinition extends AbstractTemplate implements Parameterized {

	public static abstract class Body extends Syntax {

		public Body(Location location) {
			super(location);
		}

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

		public static abstract class ClassReference extends Syntax {

			public ClassReference(Location location) {
				super(location);
			}

		}

		public static class BinaryClassName extends ClassReference {

			private final String binaryName;

			public BinaryClassName(Location location, String binaryName) {
				super(location);
				this.binaryName = binaryName;
			}

			public String getBinaryName() {
				return binaryName;
			}

		}

		public static class QualifiedClassName extends ClassReference {

			private final QualifiedName qualifiedName;

			public QualifiedClassName(QualifiedName qualifiedName) {
				super(qualifiedName.getLocation());
				this.qualifiedName = qualifiedName;
			}

			public QualifiedName getQualifiedName() {
				return qualifiedName;
			}

		}

		private final ClassReference classReference;

		private final String methodName;

		private final Location methodNameLocation;

		public NativeBody(Location initiator, ClassReference classReference,
				String methodName, Location methodNameLocation) {
			super(initiator);
			this.classReference = classReference;
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

}
