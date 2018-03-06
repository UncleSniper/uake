package org.unclesniper.uake;

public class PushLexer {

	private enum State {
		NONE,
		PLUS,
		MINUS,
		ASTERISK,
		SLASH,
		PERCENT,
		AMPERSAND,
		CARET,
		PIPE,
		LESS,
		DOUBLE_LESS,
		GREATER,
		DOUBLE_GREATER,
		TRIPLE_GREATER,
		DOT,
		EQUAL,
		NAME,
		ZERO,
		ZERO_X,
		DEC_INT,
		OCT_INT,
		BROKEN_OCT_INT,
		HEX_INT,
		INT_TYPE,
		DECIMAL_POINT,
		FLOAT,
		EXPONENT_E,
		EXPONENT_SIGN,
		EXPONENT_DIGITS,
		FLOAT_TYPE,
		STRING,
		STRING_ESCAPE,
		STRING_U,
		STRING_DIGITS,
		EMPTY_CHAR,
		CHAR_ESCAPE,
		CHAR_U,
		CHAR_DIGITS,
		FULL_CHAR
	}

	private Sink<Token> tokenSink;

	private String file;

	private int line = 1;

	private int column = 1;

	private int start;

	private State state = State.NONE;

	private StringBuilder rawBuffer;

	private StringBuilder cookedBuffer;

	private int digitCount;

	private int charCode;

	public PushLexer(Sink<Token> tokenSink, String file) {
		this.tokenSink = tokenSink;
		this.file = file;
	}

	public Sink<Token> getTokenSink() {
		return tokenSink;
	}

	public void setTokenSink(Sink<Token> tokenSink) {
		this.tokenSink = tokenSink;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	private void unexpected(char c) {
		throw new UnexpectedCharacterException(new Location(file, line, column), c);
	}

	private void emit(Token.Type type, String rawText) {
		tokenSink.sink(new Token(new Location(file, line, start), type, rawText, null));
	}

	private void emit(Token.Type type) {
		String rawText, cookedText;
		if(rawBuffer == null)
			rawText = null;
		else {
			rawText = rawBuffer.toString();
			rawBuffer = null;
		}
		if(cookedBuffer == null)
			cookedText = null;
		else {
			cookedText = cookedBuffer.toString();
			cookedBuffer = null;
		}
		if(type == Token.Type.NAME)
			type = Token.typeFromName(rawText);
		tokenSink.sink(new Token(new Location(file, line, start), type, rawText, cookedText));
	}

	private boolean checkIntFinish(char c, boolean allowFloat) {
		switch(c) {
			case 'e':
			case 'E':
				if(!allowFloat)
					return false;
				rawBuffer.append(c);
				state = State.EXPONENT_E;
				return true;
			case 'y':
			case 'Y':
				emit(Token.Type.BYTE);
				state = State.INT_TYPE;
				return true;
			case 's':
			case 'S':
				emit(Token.Type.SHORT);
				state = State.INT_TYPE;
				return true;
			case 'i':
			case 'I':
				emit(Token.Type.INT);
				state = State.INT_TYPE;
				return true;
			case 'l':
			case 'L':
				emit(Token.Type.LONG);
				state = State.INT_TYPE;
				return true;
			case 't':
			case 'T':
				if(!allowFloat)
					return false;
				emit(Token.Type.FLOAT);
				state = State.FLOAT_TYPE;
				return true;
			case 'o':
			case 'O':
				if(!allowFloat)
					return false;
				emit(Token.Type.DOUBLE);
				state = State.FLOAT_TYPE;
				return true;
			default:
				return false;
		}
	}

	public void pushSource(char[] chars, int offset, int length) {
		int end = offset + length;
		for(int i = offset; i < end; ++i) {
			char c = chars[i];
			switch(state) {
				case NONE:
					start = column;
					switch(c) {
						case ' ':
						case '\t':
						case '\n':
						case '\r':
						case '\f':
							break;
						case ':':
							emit(Token.Type.COLON, ":");
							break;
						case '{':
							emit(Token.Type.LEFT_CURLY, "{");
							break;
						case '}':
							emit(Token.Type.RIGHT_CURLY, "}");
							break;
						case ',':
							emit(Token.Type.COMMA, ",");
							break;
						case ';':
							emit(Token.Type.SEMICOLON, ";");
							break;
						case '+':
							state = State.PLUS;
							break;
						case '-':
							state = State.MINUS;
							break;
						case '*':
							state = State.ASTERISK;
							break;
						case '/':
							state = State.SLASH;
							break;
						case '%':
							state = State.PERCENT;
							break;
						case '&':
							state = State.AMPERSAND;
							break;
						case '^':
							state = State.CARET;
							break;
						case '|':
							state = State.PIPE;
							break;
						case '<':
							state = State.LESS;
							break;
						case '>':
							state = State.GREATER;
							break;
						case '.':
							state = State.DOT;
							break;
						case '(':
							emit(Token.Type.LEFT_ROUND, "(");
							break;
						case ')':
							emit(Token.Type.RIGHT_ROUND, ")");
							break;
						case '\\':
							emit(Token.Type.LAMBDA, "\\");
							break;
						case '=':
							state = State.EQUAL;
							break;
						case '@':
							emit(Token.Type.AT, "@");
							break;
						case '[':
							emit(Token.Type.LEFT_SQUARE, "[");
							break;
						case ']':
							emit(Token.Type.RIGHT_SQUARE, "]");
							break;
						case '_':
							rawBuffer = new StringBuilder(c);
							state = State.NAME;
							break;
						case '0':
							rawBuffer = new StringBuilder(c);
							state = State.ZERO;
							break;
						case '"':
							rawBuffer = new StringBuilder(c);
							cookedBuffer = new StringBuilder();
							state = State.STRING;
							break;
						case '\'':
							rawBuffer = new StringBuilder(c);
							cookedBuffer = new StringBuilder();
							state = State.EMPTY_CHAR;
							break;
						default:
							if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
								rawBuffer = new StringBuilder(c);
								state = State.NAME;
							}
							else if(c >= '1' && c <= '9') {
								rawBuffer = new StringBuilder(c);
								state = State.DEC_INT;
							}
							else
								unexpected(c);
							break;
					}
					break;
				case PLUS:
					switch(c) {
						case '+':
							emit(Token.Type.INCREMENT, "++");
							state = State.NONE;
							break;
						case '=':
							emit(Token.Type.PLUS_ASSIGN, "+=");
							state = State.NONE;
							break;
						case '0':
							rawBuffer = new StringBuilder("+0");
							state = State.ZERO;
							break;
						default:
							if(c >= '1' && c <= '9') {
								rawBuffer = new StringBuilder('+');
								rawBuffer.append(c);
								state = State.DEC_INT;
							}
							else {
								emit(Token.Type.PLUS, "+");
								state = State.NONE;
								--i;
								continue;
							}
							break;
					}
					break;
				case MINUS:
					switch(c) {
						case '-':
							emit(Token.Type.DECREMENT, "--");
							state = State.NONE;
							break;
						case '=':
							emit(Token.Type.MINUS_ASSIGN, "-=");
							state = State.NONE;
							break;
						case '>':
							emit(Token.Type.RIGHT_ARROW, "->");
							state = State.NONE;
							break;
						case '0':
							rawBuffer = new StringBuilder("-0");
							state = State.ZERO;
							break;
						default:
							if(c >= '1' && c <= '9') {
								rawBuffer = new StringBuilder('-');
								rawBuffer.append(c);
								state = State.DEC_INT;
							}
							else {
								emit(Token.Type.MINUS, "-");
								state = State.NONE;
								--i;
								continue;
							}
							break;
					}
					break;
				case ASTERISK:
					if(c == '=') {
						emit(Token.Type.MULTIPLY_ASSIGN, "*=");
						state = State.NONE;
					}
					else {
						emit(Token.Type.MULTIPLY, "*");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case SLASH:
					if(c == '=') {
						emit(Token.Type.DIVIDE_ASSIGN, "/=");
						state = State.NONE;
					}
					else {
						emit(Token.Type.DIVIDE, "/");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case PERCENT:
					switch(c) {
						case '=':
							emit(Token.Type.MODULO_ASSIGN, "%=");
							break;
						case '{':
							emit(Token.Type.PERCENT_LEFT_CURLY, "%{");
							break;
						default:
							emit(Token.Type.MODULO, "%");
							state = State.NONE;
							--i;
							continue;
					}
					state = State.NONE;
					break;
				case AMPERSAND:
					switch(c) {
						case '=':
							emit(Token.Type.AND_ASSIGN, "&=");
							break;
						case '&':
							emit(Token.Type.LOGICAL_AND, "&&");
							break;
						default:
							emit(Token.Type.BITWISE_AND, "&");
							state = State.NONE;
							--i;
							continue;
					}
					state = State.NONE;
					break;
				case CARET:
					if(c == '=') {
						emit(Token.Type.XOR_ASSIGN, "^=");
						state = State.NONE;
					}
					else {
						emit(Token.Type.BITWISE_XOR, "^");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case PIPE:
					switch(c) {
						case '=':
							emit(Token.Type.OR_ASSIGN, "|=");
							break;
						case '|':
							emit(Token.Type.LOGICAL_OR, "||");
							break;
						default:
							emit(Token.Type.BITWISE_OR, "|");
							state = State.NONE;
							--i;
							continue;
					}
					state = State.NONE;
					break;
				case LESS:
					switch(c) {
						case '=':
							emit(Token.Type.LESS_EQUAL, "<=");
							state = State.NONE;
							break;
						case '<':
							state = State.DOUBLE_LESS;
							break;
						default:
							emit(Token.Type.LESS, "<");
							state = State.NONE;
							--i;
							continue;
					}
					break;
				case DOUBLE_LESS:
					if(c == '=') {
						emit(Token.Type.SHIFT_LEFT_ASSIGN, "<<=");
						state = State.NONE;
					}
					else {
						emit(Token.Type.SHIFT_LEFT, "<<");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case GREATER:
					switch(c) {
						case '=':
							emit(Token.Type.GREATER_EQUAL, ">=");
							state = State.NONE;
							break;
						case '>':
							state = State.DOUBLE_GREATER;
							break;
						default:
							emit(Token.Type.GREATER, ">");
							state = State.NONE;
							--i;
							continue;
					}
					break;
				case DOUBLE_GREATER:
					switch(c) {
						case '=':
							emit(Token.Type.SIGNED_SHIFT_RIGHT_ASSIGN, ">>=");
							state = State.NONE;
							break;
						case '>':
							state = State.TRIPLE_GREATER;
							break;
						default:
							emit(Token.Type.SIGNED_SHIFT_RIGHT, ">>");
							state = State.NONE;
							--i;
							continue;
					}
					break;
				case TRIPLE_GREATER:
					if(c == '=') {
						emit(Token.Type.UNSIGNED_SHIFT_RIGHT_ASSIGN, ">>>=");
						state = State.NONE;
					}
					else {
						emit(Token.Type.UNSIGNED_SHIFT_RIGHT, ">>>");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case DOT:
					if(c == '.') {
						emit(Token.Type.ELLIPSIS, "..");
						state = State.NONE;
					}
					else {
						emit(Token.Type.DOT, ".");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case EQUAL:
					if(c == '=') {
						emit(Token.Type.EQUAL, "==");
						state = State.NONE;
					}
					else {
						emit(Token.Type.ASSIGN, "=");
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case NAME:
					if(c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
						rawBuffer.append(c);
					else {
						emit(Token.Type.NAME);
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case ZERO:
					switch(c) {
						case 'x':
						case 'X':
							rawBuffer.append(c);
							state = State.ZERO_X;
							break;
						case '.':
							rawBuffer.append(c);
							state = State.DECIMAL_POINT;
							break;
						case '8':
						case '9':
							unexpected(c);
						default:
							if(checkIntFinish(c, true))
								break;
							if(c >= '0' && c <= '7') {
								rawBuffer.append(c);
								state = State.OCT_INT;
							}
							else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
								unexpected(c);
							else {
								emit(Token.Type.INT);
								state = State.NONE;
								--i;
								continue;
							}
							break;
					}
					break;
				case ZERO_X:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
						rawBuffer.append(c);
						state = State.HEX_INT;
					}
					else
						unexpected(c);
					break;
				case DEC_INT:
					if(c >= '0' && c <= '9')
						rawBuffer.append(c);
					else if(checkIntFinish(c, true))
						;
					else if(c == '.') {
						rawBuffer.append(c);
						state = State.DECIMAL_POINT;
					}
					else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
						unexpected(c);
					else {
						emit(Token.Type.INT);
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case OCT_INT:
					switch(c) {
						case '8':
						case '9':
							rawBuffer.append(c);
							state = State.BROKEN_OCT_INT;
							break;
						case '.':
							rawBuffer.append(c);
							state = State.DECIMAL_POINT;
							break;
						default:
							if(c >= '0' && c <= '7')
								rawBuffer.append(c);
							else if(checkIntFinish(c, true))
								;
							else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
								unexpected(c);
							else {
								emit(Token.Type.INT);
								state = State.NONE;
								--i;
								continue;
							}
							break;
					}
					break;
				case BROKEN_OCT_INT:
					switch(c) {
						case 'e':
						case 'E':
							rawBuffer.append(c);
							state = State.EXPONENT_E;
							break;
						case 't':
						case 'T':
							emit(Token.Type.FLOAT);
							state = State.FLOAT_TYPE;
							break;
						case 'o':
						case 'O':
							emit(Token.Type.DOUBLE);
							state = State.FLOAT_TYPE;
							break;
						case '.':
							rawBuffer.append(c);
							state = State.DECIMAL_POINT;
							break;
						default:
							if(c >= '0' && c <= '9')
								rawBuffer.append(c);
							else
								unexpected(c);
							break;
					}
					break;
				case HEX_INT:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))
						rawBuffer.append(c);
					else if(checkIntFinish(c, false))
						;
					else if((c > 'f' && c <= 'z') || (c > 'F' && c <= 'Z'))
						unexpected(c);
					else {
						emit(Token.Type.INT);
						state = State.NONE;
						--i;
						continue;
					}
					break;
				case INT_TYPE:
				case FLOAT_TYPE:
					if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
						unexpected(c);
					state = State.NONE;
					--i;
					continue;
				case DECIMAL_POINT:
					if(c >= '0' && c <= '9') {
						rawBuffer.append(c);
						state = State.FLOAT;
					}
					else
						unexpected(c);
					break;
				case FLOAT:
					switch(c) {
						case 'e':
						case 'E':
							rawBuffer.append(c);
							state = State.EXPONENT_E;
							break;
						case 't':
						case 'T':
							emit(Token.Type.FLOAT);
							state = State.FLOAT_TYPE;
							break;
						case 'o':
						case 'O':
							emit(Token.Type.DOUBLE);
							state = State.FLOAT_TYPE;
							break;
						default:
							if(c >= '0' && c <= '9')
								rawBuffer.append(c);
							else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
								unexpected(c);
							else {
								emit(Token.Type.DOUBLE);
								state = State.NONE;
								--i;
								continue;
							}
							break;
					}
					break;
				case EXPONENT_E:
					switch(c) {
						case '+':
						case '-':
							rawBuffer.append(c);
							state = State.EXPONENT_SIGN;
							break;
						default:
							if(c >= '0' && c <= '9') {
								rawBuffer.append(c);
								state = State.EXPONENT_DIGITS;
							}
							else
								unexpected(c);
							break;
					}
					break;
				case EXPONENT_SIGN:
					if(c >= '0' && c <= '9') {
						rawBuffer.append(c);
						state = State.EXPONENT_DIGITS;
					}
					else
						unexpected(c);
					break;
				case EXPONENT_DIGITS:
					switch(c) {
						case 't':
						case 'T':
							emit(Token.Type.FLOAT);
							state = State.FLOAT_TYPE;
							break;
						case 'o':
						case 'O':
							emit(Token.Type.DOUBLE);
							state = State.FLOAT_TYPE;
							break;
						default:
							if(c >= '0' && c <= '9')
								rawBuffer.append(c);
							else if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
								unexpected(c);
							else {
								emit(Token.Type.DOUBLE);
								state = State.NONE;
								--i;
								continue;
							}
							break;
					}
					break;
				case STRING:
					switch(c) {
						case '\n':
						case '\r':
							unexpected(c);
						case '"':
							rawBuffer.append(c);
							emit(Token.Type.STRING);
							state = State.NONE;
							break;
						case '\\':
							rawBuffer.append(c);
							state = State.STRING_ESCAPE;
							break;
						default:
							rawBuffer.append(c);
							cookedBuffer.append(c);
							break;
					}
					break;
				case STRING_ESCAPE:
					switch(c) {
						case 'n':
							cookedBuffer.append('\n');
							state = State.STRING;
							break;
						case 'r':
							cookedBuffer.append('\r');
							state = State.STRING;
							break;
						case 't':
							cookedBuffer.append('\t');
							state = State.STRING;
							break;
						case 'b':
							cookedBuffer.append('\b');
							state = State.STRING;
							break;
						case 'f':
							cookedBuffer.append('\f');
							state = State.STRING;
							break;
						case '"':
						case '\'':
						case '\\':
							cookedBuffer.append(c);
							state = State.STRING;
							break;
						case 'u':
							state = State.STRING_U;
							break;
						default:
							unexpected(c);
					}
					rawBuffer.append(c);
					break;
				case STRING_U:
					if(c >= '0' && c <= '9')
						charCode = c - '0';
					else if(c >= 'a' && c <= 'f')
						charCode = c - 'a' + 10;
					else if(c >= 'A' && c <= 'F')
						charCode = c - 'A' + 10;
					else
						unexpected(c);
					digitCount = 1;
					rawBuffer.append(c);
					state = State.STRING_DIGITS;
					break;
				case STRING_DIGITS:
					{
						int digit;
						if(c >= '0' && c <= '9')
							digit = c - '0';
						else if(c >= 'a' && c <= 'f')
							digit = c - 'a' + 10;
						else if(c >= 'A' && c <= 'F')
							digit = c - 'A' + 10;
						else {
							cookedBuffer.append((char)charCode);
							state = State.STRING;
							--i;
							continue;
						}
						charCode = charCode * 16 + digit;
						rawBuffer.append(c);
						if(++digitCount >= 4) {
							cookedBuffer.append((char)charCode);
							state = State.STRING;
						}
					}
					break;
				case EMPTY_CHAR:
					switch(c) {
						case '\n':
						case '\r':
						case '\'':
							unexpected(c);
						case '\\':
							rawBuffer.append(c);
							state = State.CHAR_ESCAPE;
							break;
						default:
							rawBuffer.append(c);
							cookedBuffer.append(c);
							state = State.FULL_CHAR;
							break;
					}
					break;
				case CHAR_ESCAPE:
					switch(c) {
						case 'n':
							cookedBuffer.append('\n');
							state = State.FULL_CHAR;
							break;
						case 'r':
							cookedBuffer.append('\r');
							state = State.FULL_CHAR;
							break;
						case 't':
							cookedBuffer.append('\t');
							state = State.FULL_CHAR;
							break;
						case 'b':
							cookedBuffer.append('\b');
							state = State.FULL_CHAR;
							break;
						case 'f':
							cookedBuffer.append('\f');
							state = State.FULL_CHAR;
							break;
						case '"':
						case '\'':
						case '\\':
							cookedBuffer.append(c);
							state = State.FULL_CHAR;
							break;
						case 'u':
							state = State.CHAR_U;
							break;
						default:
							unexpected(c);
					}
					rawBuffer.append(c);
					break;
				case CHAR_U:
					if(c >= '0' && c <= '9')
						charCode = c - '0';
					else if(c >= 'a' && c <= 'f')
						charCode = c - 'a' + 10;
					else if(c >= 'A' && c <= 'F')
						charCode = c - 'A' + 10;
					else
						unexpected(c);
					digitCount = 1;
					rawBuffer.append(c);
					state = State.CHAR_DIGITS;
					break;
				case CHAR_DIGITS:
					{
						int digit;
						if(c >= '0' && c <= '9')
							digit = c - '0';
						else if(c >= 'a' && c <= 'f')
							digit = c - 'a' + 10;
						else if(c >= 'A' && c <= 'F')
							digit = c - 'A' + 10;
						else {
							cookedBuffer.append((char)charCode);
							state = State.FULL_CHAR;
							--i;
							continue;
						}
						charCode = charCode * 16 + digit;
						rawBuffer.append(c);
						if(++digitCount >= 4) {
							cookedBuffer.append((char)charCode);
							state = State.FULL_CHAR;
						}
					}
					break;
				case FULL_CHAR:
					if(c == '\'') {
						rawBuffer.append(c);
						emit(Token.Type.CHAR);
						state = State.NONE;
					}
					else
						unexpected(c);
					break;
				default:
					throw new Doom("Unrecognized state: " + state.name());
			}
			if(c == '\n') {
				++line;
				column = 1;
			}
			else
				++column;
		}
	}

}
