package org.unclesniper.uake;

import org.unclesniper.uake.syntax.Header;
import org.unclesniper.uake.syntax.Import;
import org.unclesniper.uake.syntax.TopLevel;
import org.unclesniper.uake.syntax.Utterance;
import org.unclesniper.uake.syntax.ModuleImport;
import org.unclesniper.uake.syntax.QualifiedName;

public class Parser {

	private Source<Token> lexer;

	private LocationTracker locationTracker;

	private Token token;

	public Parser(Source<Token> lexer, LocationTracker locationTracker) {
		this.lexer = lexer;
		this.locationTracker = locationTracker;
	}

	public Source<Token> getLexer() {
		return lexer;
	}

	public void setLexer(Source<Token> lexer) {
		this.lexer = lexer;
	}

	public LocationTracker getLocationTracker() {
		return locationTracker;
	}

	public void setLocationTracker(LocationTracker locationTracker) {
		this.locationTracker = locationTracker;
	}

	private void next() {
		token = lexer.next();
	}

	private void unexpected(String expected) {
		if(token == null)
			throw new UnexpectedEndOfCompilationUnitException(locationTracker.getLocation(), expected);
		else
			throw new UnexpectedTokenException(token, expected);
	}

	private void unexpected(Token.Type... expected) {
		if(token == null)
			throw new UnexpectedEndOfCompilationUnitException(locationTracker.getLocation(), expected);
		else
			throw new UnexpectedTokenException(token, expected);
	}

	private void expect(Token.Type expected) {
		if(token == null || token.getType() != expected)
			unexpected(expected);
	}

	private Location consume(Token.Type expected) {
		if(token == null || token.getType() != expected)
			unexpected(expected);
		Location location = token.getLocation();
		token = lexer.next();
		return location;
	}

	public Utterance parseUtterance() {
		Utterance utterance = new Utterance(locationTracker.getLocation());
		next();
		Location location = null;
		boolean haveTopLevel = false;
		while(token != null) {
			if(location == null)
				location = token.getLocation();
			Token.Type type = token.getType();
			if(!haveTopLevel && Parser.startsHeader(type))
				utterance.addHeader(parseHeader());
			else if(Parser.startsTopLevel(type)) {
				utterance.addTopLevel(parseTopLevel());
				haveTopLevel = true;
			}
			else
				unexpected(haveTopLevel ? "definition or statement" : "header, definition or statement");
		}
		if(location != null)
			utterance.setLocation(location);
		return utterance;
	}

	private Header parseHeader() {
		Location initiator = consume(Token.Type.IMPORT);
		if(token == null)
			unexpected(Token.Type.MOD, Token.Type.NAME);
		switch(token.getType()) {
			case MOD:
				return parseModuleImport(initiator);
			case NAME:
				{
					QualifiedName.Segment head = new QualifiedName.Segment(token.getLocation(), token.getRawText());
					next();
					return new Import(initiator, head, parseImportTail());
				}
			default:
				unexpected(Token.Type.MOD, Token.Type.NAME);
				return null;
		}
	}

	private ModuleImport parseModuleImport(Location initiator) {
		next();
		return new ModuleImport(initiator, parseJQName());
	}

	private Import.Tail parseImportTail() {
		if(token == null)
			unexpected(Token.Type.AS, Token.Type.COLON);
		switch(token.getType()) {
			case AS:
				{
					next();
					expect(Token.Type.NAME);
					Import.AsTail tail = new Import.AsTail(new QualifiedName.Segment(token.getLocation(),
							token.getRawText()));
					next();
					return tail;
				}
			case COLON:
				next();
				if(token == null)
					unexpected(Token.Type.NAME, Token.Type.LEFT_CURLY);
				switch(token.getType()) {
					case NAME:
						return parseImportEndTail();
					case LEFT_CURLY:
						{
							Import.SplitTail split = new Import.SplitTail();
							for(;;) {
								expect(Token.Type.NAME);
								split.addTail(parseImportEndTail());
								if(token == null)
									unexpected(Token.Type.COMMA, Token.Type.RIGHT_CURLY);
								switch(token.getType()) {
									case COMMA:
										next();
										break;
									case RIGHT_CURLY:
										next();
										return split;
									default:
										unexpected(Token.Type.COMMA, Token.Type.RIGHT_CURLY);
								}
							}
						}
					default:
						unexpected(Token.Type.NAME, Token.Type.LEFT_CURLY);
						return null;
				}
			default:
				unexpected(Token.Type.AS, Token.Type.COLON);
				return null;
		}
	}

	private Import.EndTail parseImportEndTail() {
		QualifiedName.Segment segment = new QualifiedName.Segment(token.getLocation(), token.getRawText());
		next();
		Import.Tail tail;
		if(token == null)
			tail = null;
		else {
			switch(token.getType()) {
				case AS:
				case COLON:
					tail = parseImportTail();
					break;
				default:
					tail = null;
					break;
			}
		}
		return new Import.EndTail(segment, tail);
	}

	private QualifiedName parseJQName() {
		QualifiedName qname = new QualifiedName();
		expect(Token.Type.NAME);
		qname.addSegment(new QualifiedName.Segment(token.getLocation(), token.getRawText()));
		next();
		while(token != null && token.getType() == Token.Type.DOT) {
			next();
			expect(Token.Type.NAME);
			qname.addSegment(new QualifiedName.Segment(token.getLocation(), token.getRawText()));
			next();
		}
		return qname;
	}

	private TopLevel parseTopLevel() {
		//TODO
		return null;
	}

	private static boolean startsHeader(Token.Type type) {
		return type == Token.Type.IMPORT;
	}

	private static boolean startsTopLevel(Token.Type type) {
		switch(type) {
			case MINUS:
			case NOT:
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
			case FLOAT:
			case DOUBLE:
			case CHAR:
			case STRING:
			case PLUS:
			case LOGICAL_NOT:
			case TYPE:
			case UNTIL:
			case BITWISE_NOT:
			case INCREMENT:
			case DECREMENT:
			case LEFT_SQUARE:
			case MOD:
			case CONST:
			case VAR:
			case PROVIDE:
			case LEFT_ROUND:
			case BREAK:
			case NAME:
			case FOR:
			case IF:
			case LAMBDA:
			case UNLESS:
			case WHILE:
			case LEFT_CURLY:
			case PROPERTY:
			case USING:
			case RETURN:
			case REQUIRE:
			case NEW:
			case PERCENT_LEFT_CURLY:
			case FOREACH:
			case FUN:
			case USE:
			case CONTINUE:
			case UNUSE:
				return true;
			default:
				return false;
		}
	}

}
