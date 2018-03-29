package org.unclesniper.uake;

import org.unclesniper.uake.semantics.UakeTemplate;

public class TemplateArityException extends SemanticsException {

	private final UakeTemplate templateElement;

	private final int expectedArity;

	private final int givenArity;

	public TemplateArityException(UakeTemplate templateElement, Location emissionLocation,
			int expectedArity, int givenArity) {
		super(emissionLocation, TemplateArityException.makeMessage(templateElement, emissionLocation,
				expectedArity, givenArity));
		this.templateElement = templateElement;
		this.expectedArity = expectedArity;
		this.givenArity = givenArity;
	}

	public UakeTemplate getTemplateElement() {
		return templateElement;
	}

	public int getExpectedArity() {
		return expectedArity;
	}

	public int getGivenArity() {
		return givenArity;
	}

	private static String makeMessage(UakeTemplate templateElement, Location emissionLocation,
			int expectedArity, int givenArity) {
		StringBuilder builder = new StringBuilder("Illegal instantiation of template '");
		builder.append(templateElement.getQualifiedName().toString(':'));
		builder.append('\'');
		if(emissionLocation != null) {
			builder.append(" at ");
			builder.append(emissionLocation.toString());
		}
		builder.append(": Template takes at ");
		builder.append(expectedArity < givenArity ? "most " : "least ");
		builder.append(String.valueOf(expectedArity));
		builder.append(", but ");
		builder.append(String.valueOf(givenArity));
		builder.append(" given");
		return builder.toString();
	}

}
