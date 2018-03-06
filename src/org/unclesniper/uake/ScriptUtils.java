package org.unclesniper.uake;

public class ScriptUtils {

	private static final String HEX_DIGITS = "0123456789ABCDEF";

	private ScriptUtils() {}

	public static String escapeChar(char c, boolean quote) {
		StringBuilder builder = new StringBuilder();
		ScriptUtils.escapeChar(c, quote, builder);
		return builder.toString();
	}

	public static void escapeChar(char c, boolean quote, StringBuilder builder) {
		if(quote)
			builder.append('\'');
		ScriptUtils.escapeSingleChar(c, builder, '\'');
		if(quote)
			builder.append('\'');
	}

	public static String escapeString(String s, boolean quote) {
		StringBuilder builder = new StringBuilder();
		ScriptUtils.escapeString(s, quote, builder);
		return builder.toString();
	}

	public static void escapeString(String s, boolean quote, StringBuilder builder) {
		if(quote)
			builder.append('"');
		int length = s.length();
		for(int i = 0; i < length; ++i)
			ScriptUtils.escapeSingleChar(s.charAt(i), builder, '"');
		if(quote)
			builder.append('"');
	}

	private static void escapeSingleChar(char c, StringBuilder builder, char quote) {
		switch(c) {
			case '\t':
				builder.append("\\t");
				break;
			case '\b':
				builder.append("\\b");
				break;
			case '\n':
				builder.append("\\n");
				break;
			case '\r':
				builder.append("\\r");
				break;
			case '\f':
				builder.append("\\f");
				break;
			case '\\':
				builder.append("\\\\");
				break;
			default:
				if(c == quote) {
					builder.append('\\');
					builder.append(c);
				}
				else if(c >= ' ' && c <= '~')
					builder.append(c);
				else {
					builder.append("\\u");
					int code = (int)c;
					for(int i = 3; i >= 0; --i)
						builder.append(ScriptUtils.HEX_DIGITS.charAt((code >> (i * 4)) & 0xF));
				}
				break;
		}
	}

}
