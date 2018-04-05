package org.unclesniper.uake;

public class AmbiguousElementReferenceException extends SemanticsException {

	public enum RequiredType {

		TYPE("Type"),
		TYPE_TEMPLATE("Type template");

		private final String messageRendition;

		private RequiredType(String messageRendition) {
			this.messageRendition = messageRendition;
		}

		public String getMessageRendition() {
			return messageRendition;
		}

	}

	private final String ambiguousName;

	private final RequiredType requiredType;

	public AmbiguousElementReferenceException(Location location, String ambiguousName, RequiredType requiredType) {
		super(location, requiredType.getMessageRendition() + " name '" + ambiguousName + "' is ambiguous at "
				+ location);
		this.ambiguousName = ambiguousName;
		this.requiredType = requiredType;
	}

	public String getAmbiguousName() {
		return ambiguousName;
	}

	public RequiredType getRequiredType() {
		return requiredType;
	}

}
