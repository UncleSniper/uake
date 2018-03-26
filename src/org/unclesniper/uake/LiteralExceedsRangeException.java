package org.unclesniper.uake;

public class LiteralExceedsRangeException extends SemanticsException {

	public enum Type {
		BYTE,
		SHORT,
		INT,
		LONG
	}

	private final Type literalType;

	private final String specifier;

	public LiteralExceedsRangeException(Location location, Type literalType, String specifier) {
		super(location, "Range of " + literalType.name().toLowerCase() + " literal exceeded"
				+ (location == null ? "" : " at " + location) + ": " + specifier);
		this.literalType = literalType;
		this.specifier = specifier;
	}

	public Type getLiteralType() {
		return literalType;
	}

	public String getSpecifier() {
		return specifier;
	}

}
