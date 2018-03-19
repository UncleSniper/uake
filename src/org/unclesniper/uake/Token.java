package org.unclesniper.uake;

import java.util.Map;
import java.util.HashMap;

public final class Token {

	public enum Type {

		NAME("identifier"),
		BYTE("byte literal"),
		SHORT("short literal"),
		INT("int literal"),
		LONG("long literal"),
		FLOAT("float literal"),
		DOUBLE("double literal"),
		CHAR("char literal"),
		STRING("string literal"),
		IMPORT,
		COLON("':'"),
		LEFT_CURLY("'{'"),
		RIGHT_CURLY("'}'"),
		COMMA("','"),
		MOD,
		FUN,
		NATIVE,
		TYPE,
		SEMICOLON("';'"),
		PROPERTY,
		PROVIDE,
		RIGHT_ARROW("'->'"),
		VAR,
		CONST,
		DOT("'.'"),
		ELLIPSIS("'..'"),
		LEFT_ROUND("'('"),
		RIGHT_ROUND("')'"),
		IF,
		UNLESS,
		THEN,
		ELIF,
		ELUNLESS,
		ELSE,
		FOR,
		DO,
		FOREACH,
		WHILE,
		UNTIL,
		USE,
		UNUSE,
		USING,
		LAMBDA("'\\'"),
		BREAK,
		CONTINUE,
		RETURN,
		ASSIGN("'='"),
		PLUS_ASSIGN("'+='"),
		MINUS_ASSIGN("'-='"),
		MULTIPLY_ASSIGN("'*='"),
		DIVIDE_ASSIGN("'/='"),
		MODULO_ASSIGN("'%='"),
		AND_ASSIGN("'&='"),
		XOR_ASSIGN("'^='"),
		OR_ASSIGN("'|='"),
		SHIFT_LEFT_ASSIGN("'<<='"),
		SIGNED_SHIFT_RIGHT_ASSIGN("'>>='"),
		UNSIGNED_SHIFT_RIGHT_ASSIGN("'>>>='"),
		LOGICAL_OR("'||'"),
		LOGICAL_AND("'&&'"),
		IS,
		AS,
		BITWISE_OR("'|'"),
		BITWISE_XOR("'^'"),
		BITWISE_AND("'&'"),
		IN,
		EQUAL("'=='"),
		UNEQUAL("'!='"),
		LESS("'<'"),
		GREATER("'>'"),
		LESS_EQUAL("'<='"),
		GREATER_EQUAL("'>='"),
		SHIFT_LEFT("'<<'"),
		SIGNED_SHIFT_RIGHT("'>>'"),
		UNSIGNED_SHIFT_RIGHT("'>>>'"),
		PLUS("'+'"),
		MINUS("'-'"),
		MULTIPLY("'*'"),
		DIVIDE("'/'"),
		MODULO("'%'"),
		INCREMENT("'++'"),
		DECREMENT("'--'"),
		BITWISE_NOT("'~'"),
		LOGICAL_NOT("'!'"),
		NOT,
		REQUIRE,
		AT("'@'"),
		LEFT_SQUARE("'['"),
		RIGHT_SQUARE("']'"),
		PERCENT_LEFT_CURLY("'%{'"),
		NEW,
		EXTENDS,
		ON,
		STATIC;

		private final boolean keyword;

		private final String description;

		private Type() {
			keyword = true;
			description = name().toLowerCase();
		}

		private Type(String description) {
			keyword = false;
			this.description = description;
		}

		public boolean isKeyword() {
			return keyword;
		}

		public String getDescription() {
			return description;
		}

	}

	private static final Map<String, Type> KEYWORDS;

	static {
		KEYWORDS = new HashMap<>();
		for(Type type : Type.values()) {
			if(type.isKeyword())
				KEYWORDS.put(type.name().toLowerCase(), type);
		}
	}

	private final Location location;

	private final Type type;

	private final String rawText;

	private final String cookedText;

	public Token(Location location, Type type, String rawText, String cookedText) {
		this.location = location;
		this.type = type;
		this.rawText = rawText;
		this.cookedText = cookedText;
	}

	public Location getLocation() {
		return location;
	}

	public Type getType() {
		return type;
	}

	public String getRawText() {
		return rawText;
	}

	public String getCookedText() {
		return cookedText;
	}

	public String reproduce() {
		switch(type) {
			case NAME:
				return '\'' + rawText + '\'';
			case BYTE:
				return '\'' + rawText + "y'";
			case SHORT:
				return '\'' + rawText + "s'";
			case INT:
				return '\'' + rawText + '\'';
			case LONG:
				return '\'' + rawText + "l'";
			case FLOAT:
				return '\'' + rawText + "t'";
			case DOUBLE:
				return '\'' + rawText + "o'";
			case CHAR:
				return '"' + rawText + '"';
			case STRING:
				return '\'' + rawText + '\'';
			default:
				return type.getDescription();
		}
	}

	public static Type typeFromName(String text) {
		Type type = Token.KEYWORDS.get(text);
		return type == null ? Type.NAME : type;
	}

}
