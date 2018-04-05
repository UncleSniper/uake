package org.unclesniper.uake;

public class ElementReferenceTypeException extends SemanticsException {

	public enum RequiredType {

		TYPE("types"),
		TYPE_TEMPLATE("type templates");

		private final String messageRendition;

		private RequiredType(String messageRendition) {
			this.messageRendition = messageRendition;
		}

		public String getMessageRendition() {
			return messageRendition;
		}

	}

	private final String illTypedName;

	private final int boundCount;

	private final RequiredType requiredType;

	public ElementReferenceTypeException(Location location, String illTypedName,
			int boundCount, RequiredType requiredType) {
		super(location, "Name '" + illTypedName + "' is bound " + boundCount + " times at " + location
				+ ", but none of the bindings are " + requiredType.getMessageRendition());
		this.illTypedName = illTypedName;
		this.boundCount = boundCount;
		this.requiredType = requiredType;
	}

	public String getIllTypedName() {
		return illTypedName;
	}

	public int getBoundCount() {
		return boundCount;
	}

	public RequiredType getRequiredType() {
		return requiredType;
	}

}
