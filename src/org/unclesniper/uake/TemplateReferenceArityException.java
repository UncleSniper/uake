package org.unclesniper.uake;

public class TemplateReferenceArityException extends SemanticsException {

	public enum RequiredType {

		TYPE("type");

		private final String messageRendition;

		private RequiredType(String messageRendition) {
			this.messageRendition = messageRendition;
		}

		public String getMessageRendition() {
			return messageRendition;
		}

	}

	private final String illKindedName;

	private final int boundCount;

	private final RequiredType requiredType;

	private final int requiredArity;

	public TemplateReferenceArityException(Location location, String illKindedName,
			int boundCount, RequiredType requiredType, int requiredArity) {
		super(location, "Name '" + illKindedName + "' is bound to " + boundCount + ' '
				+ requiredType.getMessageRendition() + " templates at " + location
				+ ", but none of them match template arity " + requiredArity);
		this.illKindedName = illKindedName;
		this.boundCount = boundCount;
		this.requiredType = requiredType;
		this.requiredArity = requiredArity;
	}

	public String getIllKindedName() {
		return illKindedName;
	}

	public int getBoundCount() {
		return boundCount;
	}

	public RequiredType getRequiredType() {
		return requiredType;
	}

	public int getRequiredArity() {
		return requiredArity;
	}

}
