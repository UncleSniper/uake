package org.unclesniper.uake;

import org.unclesniper.uake.syntax.Header;
import org.unclesniper.uake.syntax.TopLevel;
import org.unclesniper.uake.syntax.Utterance;

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
				//TODO
			case NAME:
				//TODO
			default:
				unexpected(Token.Type.MOD, Token.Type.NAME);
				return null;
		}
	}

	private TopLevel parseTopLevel() {
		//TODO
		return null;
	}

	private static boolean startsHeader(Token.Type type) {
		return type == Token.Type.IMPORT;
	}

	private static boolean startsTopLevel(Token.Type type) {
		//TODO
		return false;
	}

}
