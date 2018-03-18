package org.unclesniper.uake;

import org.unclesniper.uake.syntax.Block;
import org.unclesniper.uake.syntax.Header;
import org.unclesniper.uake.syntax.Import;
import org.unclesniper.uake.syntax.TopLevel;
import org.unclesniper.uake.syntax.Template;
import org.unclesniper.uake.syntax.Utterance;
import org.unclesniper.uake.syntax.Statement;
import org.unclesniper.uake.syntax.Parameter;
import org.unclesniper.uake.syntax.Definition;
import org.unclesniper.uake.syntax.Expression;
import org.unclesniper.uake.syntax.ModuleImport;
import org.unclesniper.uake.syntax.QualifiedName;
import org.unclesniper.uake.syntax.TypeSpecifier;
import org.unclesniper.uake.syntax.Parameterized;
import org.unclesniper.uake.syntax.ClassReference;
import org.unclesniper.uake.syntax.TypeDefinition;
import org.unclesniper.uake.syntax.PropertyTrigger;
import org.unclesniper.uake.syntax.BinaryClassName;
import org.unclesniper.uake.syntax.ModuleDefinition;
import org.unclesniper.uake.syntax.TemplateParameter;
import org.unclesniper.uake.syntax.FunctionDefinition;
import org.unclesniper.uake.syntax.VariableDefinition;
import org.unclesniper.uake.syntax.TemplateInvocation;
import org.unclesniper.uake.syntax.QualifiedClassName;
import org.unclesniper.uake.syntax.PropertyDefinition;

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

	private QualifiedName parseQName() {
		QualifiedName qname = new QualifiedName();
		expect(Token.Type.NAME);
		qname.addSegment(new QualifiedName.Segment(token.getLocation(), token.getRawText()));
		next();
		while(token != null && token.getType() == Token.Type.COLON) {
			next();
			expect(Token.Type.NAME);
			qname.addSegment(new QualifiedName.Segment(token.getLocation(), token.getRawText()));
			next();
		}
		return qname;
	}

	private TopLevel parseTopLevel() {
		if(token == null)
			unexpected("definition or statement");
		switch(token.getType()) {
			case MOD:
			case FUN:
			case TYPE:
			case PROPERTY:
			case PROVIDE:
			case VAR:
			case CONST:
				return parseDefinition();
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
			case UNTIL:
			case BITWISE_NOT:
			case INCREMENT:
			case DECREMENT:
			case LEFT_SQUARE:
			case LEFT_ROUND:
			case BREAK:
			case NAME:
			case FOR:
			case IF:
			case LAMBDA:
			case UNLESS:
			case WHILE:
			case LEFT_CURLY:
			case USING:
			case RETURN:
			case REQUIRE:
			case NEW:
			case PERCENT_LEFT_CURLY:
			case FOREACH:
			case USE:
			case CONTINUE:
			case UNUSE:
				return parseStatement();
			default:
				unexpected("definition or statement");
				return null;
		}
	}

	private Definition parseDefinition() {
		if(token == null)
			unexpected("definition");
		switch(token.getType()) {
			case MOD:
				return parseModule();
			case FUN:
				return parseFunction();
			case TYPE:
				return parseRecordType();
			case PROPERTY:
				return parseProperty();
			case PROVIDE:
				//TODO
			case VAR:
			case CONST:
				return parseVariable();
			default:
				unexpected("definition");
				return null;
		}
	}

	private ModuleDefinition parseModule() {
		Location initiator = consume(Token.Type.MOD);
		ModuleDefinition module = new ModuleDefinition(initiator, parseQName());
		consume(Token.Type.LEFT_CURLY);
		for(;;) {
			if(token == null)
				unexpected("definition, statement or '}'");
			if(token.getType() == Token.Type.RIGHT_CURLY) {
				next();
				return module;
			}
			if(!Parser.startsTopLevel(token.getType()))
				unexpected("definition, statement or '}'");
			module.addChild(parseTopLevel());
		}
	}

	private TypeSpecifier parseType() {
		TypeSpecifier type = new TypeSpecifier(parseQName());
		if(token == null || token.getType() != Token.Type.LESS)
			return type;
		next();
		for(;;) {
			type.addTemplateArgument(parseType());
			if(token == null)
				unexpected(Token.Type.COMMA, Token.Type.GREATER);
			switch(token.getType()) {
				case COMMA:
					next();
					break;
				case GREATER:
					next();
					return type;
				default:
					unexpected(Token.Type.COMMA, Token.Type.GREATER);
			}
		}
	}

	private FunctionDefinition parseFunction() {
		Location initiator = consume(Token.Type.FUN);
		TypeSpecifier returnType = parseType();
		expect(Token.Type.NAME);
		FunctionDefinition function = new FunctionDefinition(initiator, returnType,
				token.getRawText(), token.getLocation());
		next();
		if(token == null)
			unexpected(Token.Type.LESS, Token.Type.LEFT_ROUND, Token.Type.LEFT_CURLY,
					Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.NATIVE);
		switch(token.getType()) {
			case LESS:
				parseTemplateParameters(function);
				break;
			case LEFT_ROUND:
			case LEFT_CURLY:
			case ASSIGN:
			case RETURN:
			case NATIVE:
				break;
			default:
				unexpected(Token.Type.LESS, Token.Type.LEFT_ROUND, Token.Type.LEFT_CURLY,
						Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.NATIVE);
		}
		if(token == null)
			unexpected(Token.Type.LEFT_ROUND, Token.Type.LEFT_CURLY,
					Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.NATIVE);
		switch(token.getType()) {
			case LEFT_ROUND:
				parseParameters(function);
				break;
			case LEFT_CURLY:
			case ASSIGN:
			case RETURN:
			case NATIVE:
				break;
			default:
				unexpected(Token.Type.LEFT_ROUND, Token.Type.LEFT_CURLY,
						Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.NATIVE);
		}
		if(token == null)
			unexpected("function body");
		FunctionDefinition.ScriptBody scriptBody = null;
		switch(token.getType()) {
			case LEFT_CURLY:
				scriptBody = new FunctionDefinition.BlockBody(parseBlock());
				function.setBody(scriptBody);
				break;
			case ASSIGN:
			case RETURN:
				{
					Location bodyInitiator = token.getLocation();
					next();
					scriptBody = new FunctionDefinition.ExpressionBody(bodyInitiator, parseExpression());
					function.setBody(scriptBody);
					consume(Token.Type.SEMICOLON);
				}
				break;
			case NATIVE:
				{
					Location bodyInitiator = token.getLocation();
					next();
					ClassReference classReference = parseClassReference();
					consume(Token.Type.COLON);
					if(token == null)
						unexpected(Token.Type.STRING, Token.Type.NAME);
					String methodName;
					switch(token.getType()) {
						case STRING:
							methodName = token.getCookedText();
							break;
						case NAME:
							methodName = token.getRawText();
							break;
						default:
							unexpected(Token.Type.STRING, Token.Type.NAME);
							return null;
					}
					function.setBody(new FunctionDefinition.NativeBody(bodyInitiator, classReference,
							methodName, token.getLocation()));
					next();
				}
				break;
			default:
				unexpected("function body");
		}
		if(scriptBody != null) {
			while(token != null && token.getType() == Token.Type.ON)
				scriptBody.addTrigger(parsePropertyTrigger());
		}
		return function;
	}

	private void parseTemplateParameters(Template sink) {
		consume(Token.Type.LESS);
		for(;;) {
			expect(Token.Type.NAME);
			String name = token.getRawText();
			Location location = token.getLocation();
			next();
			boolean elliptic = token != null && token.getType() == Token.Type.ELLIPSIS;
			if(elliptic)
				next();
			sink.addTemplateParameter(new TemplateParameter(location, name, elliptic));
			if(token == null) {
				if(elliptic)
					unexpected(Token.Type.GREATER);
				else
					unexpected(Token.Type.GREATER, Token.Type.COMMA, Token.Type.ELLIPSIS);
			}
			switch(token.getType()) {
				case GREATER:
					next();
					return;
				case COMMA:
					if(elliptic)
						unexpected(Token.Type.GREATER);
					next();
					break;
				default:
					if(elliptic)
						unexpected(Token.Type.GREATER);
					else
						unexpected(Token.Type.GREATER, Token.Type.COMMA, Token.Type.ELLIPSIS);
			}
		}
	}

	private void parseParameters(Parameterized sink) {
		consume(Token.Type.LEFT_ROUND);
		if(token == null)
			unexpected(Token.Type.NAME, Token.Type.RIGHT_ROUND);
		switch(token.getType()) {
			case NAME:
				break;
			case RIGHT_ROUND:
				next();
				return;
			default:
				unexpected(Token.Type.NAME, Token.Type.RIGHT_ROUND);
		}
		for(;;) {
			Parameter parameter = parseParameter();
			boolean elliptic = parameter.isElliptic();
			sink.addParameter(parameter);
			if(token == null) {
				if(elliptic)
					unexpected(Token.Type.RIGHT_ROUND);
				else
					unexpected(Token.Type.COMMA, Token.Type.RIGHT_ROUND);
			}
			switch(token.getType()) {
				case RIGHT_ROUND:
					next();
					return;
				case COMMA:
					next();
					break;
				default:
					if(elliptic)
						unexpected(Token.Type.RIGHT_ROUND);
					else
						unexpected(Token.Type.COMMA, Token.Type.RIGHT_ROUND);
			}
		}
	}

	private Parameter parseParameter() {
		TypeSpecifier type = parseType();
		boolean elliptic;
		if(token == null)
			unexpected(Token.Type.ELLIPSIS, Token.Type.NAME);
		switch(token.getType()) {
			case ELLIPSIS:
				elliptic = true;
				next();
				expect(Token.Type.NAME);
				break;
			case NAME:
				elliptic = false;
				break;
			default:
				unexpected(Token.Type.ELLIPSIS, Token.Type.NAME);
				return null;
		}
		Parameter parameter = new Parameter(type, token.getLocation(), token.getRawText(), elliptic);
		next();
		return parameter;
	}

	private ClassReference parseClassReference() {
		if(token == null)
			unexpected(Token.Type.STRING, Token.Type.NAME);
		ClassReference classReference;
		switch(token.getType()) {
			case STRING:
				classReference = new BinaryClassName(token.getLocation(), token.getCookedText());
				next();
				break;
			case NAME:
				classReference = new QualifiedClassName(parseJQName());
				break;
			default:
				unexpected(Token.Type.STRING, Token.Type.NAME);
				return null;
		}
		return classReference;
	}

	private VariableDefinition parseVariable() {
		if(token == null)
			unexpected(Token.Type.VAR, Token.Type.CONST);
		boolean constant;
		switch(token.getType()) {
			case VAR:
				constant = false;
				break;
			case CONST:
				constant = true;
				break;
			default:
				unexpected(Token.Type.VAR, Token.Type.CONST);
				return null;
		}
		Location initiator = token.getLocation();
		next();
		TypeSpecifier type = parseType();
		expect(Token.Type.NAME);
		String name = token.getRawText();
		Location nameLocation = token.getLocation();
		Expression initializer;
		if(token == null) {
			if(constant)
				unexpected(Token.Type.ASSIGN);
			else
				unexpected(Token.Type.ASSIGN, Token.Type.SEMICOLON);
		}
		if(constant)
			expect(Token.Type.ASSIGN);
		if(token.getType() == Token.Type.ASSIGN) {
			next();
			initializer = parseExpression();
			if(token == null)
				unexpected(Token.Type.SEMICOLON);
		}
		else
			initializer = null;
		if(token.getType() != Token.Type.SEMICOLON) {
			if(initializer == null)
				unexpected(Token.Type.ASSIGN, Token.Type.SEMICOLON);
			else
				unexpected(Token.Type.SEMICOLON);
		}
		next();
		return new VariableDefinition(initiator, constant, type, name, nameLocation, initializer);
	}

	private PropertyTrigger parsePropertyTrigger() {
		Location initiator = consume(Token.Type.ON);
		next();
		if(token == null)
			unexpected(Token.Type.USE, Token.Type.UNUSE);
		PropertyTrigger.Event event;
		switch(token.getType()) {
			case USE:
				event = PropertyTrigger.Event.USE;
				break;
			case UNUSE:
				event = PropertyTrigger.Event.UNUSE;
				break;
			default:
				unexpected(Token.Type.USE, Token.Type.UNUSE);
				return null;
		}
		next();
		PropertyTrigger trigger = new PropertyTrigger(initiator, event, parseQName());
		if(token != null && token.getType() == Token.Type.LESS)
			parseTemplateArguments(trigger);
		return trigger;
	}

	private void parseTemplateArguments(TemplateInvocation sink) {
		consume(Token.Type.LESS);
		for(;;) {
			sink.addTemplateArgument(parseType());
			if(token == null)
				unexpected(Token.Type.COMMA, Token.Type.GREATER);
			switch(token.getType()) {
				case COMMA:
					next();
					break;
				case GREATER:
					next();
					return;
				default:
					unexpected(Token.Type.COMMA, Token.Type.GREATER);
			}
		}
	}

	private Block parseBlock() {
		Location initiator = consume(Token.Type.LEFT_CURLY);
		next();
		Block block = new Block(initiator);
		for(;;) {
			if(token == null)
				unexpected("statement, variable/constant definition or '}'");
			switch(token.getType()) {
				case VAR:
				case CONST:
					block.addItem(new Block.VariableItem(parseVariable()));
					break;
				case RIGHT_CURLY:
					next();
					return block;
				default:
					if(!Parser.startsExpression(token.getType()))
						unexpected("statement, variable/constant definition or '}'");
					block.addItem(new Block.StatementItem(parseStatement()));
					break;
			}
		}
	}

	private TypeDefinition parseRecordType() {
		Location initiator = consume(Token.Type.TYPE);
		expect(Token.Type.NAME);
		String name = token.getRawText();
		Location nameLocation = token.getLocation();
		next();
		if(token == null)
			unexpected(Token.Type.LESS, Token.Type.EXTENDS, Token.Type.LEFT_CURLY, Token.Type.NATIVE);
		TypeDefinition type = new TypeDefinition(initiator, name, nameLocation, null);
		switch(token.getType()) {
			case LESS:
				parseTemplateParameters(type);
				break;
			case EXTENDS:
			case LEFT_CURLY:
			case NATIVE:
				break;
			default:
				unexpected(Token.Type.LESS, Token.Type.EXTENDS, Token.Type.LEFT_CURLY, Token.Type.NATIVE);
		}
		if(token == null)
			unexpected(Token.Type.EXTENDS, Token.Type.LEFT_CURLY, Token.Type.NATIVE);
		switch(token.getType()) {
			case EXTENDS:
				next();
				for(;;) {
					type.addSupertype(parseType());
					if(token != null && token.getType() == Token.Type.COMMA)
						next();
					else
						break;
				}
				break;
			case LEFT_CURLY:
			case NATIVE:
				break;
			default:
				unexpected(Token.Type.EXTENDS, Token.Type.LEFT_CURLY, Token.Type.NATIVE);
		}
		if(token == null)
			unexpected(Token.Type.LEFT_CURLY, Token.Type.NATIVE);
		switch(token.getType()) {
			case LEFT_CURLY:
				{
					TypeDefinition.FieldsBody body = new TypeDefinition.FieldsBody(token.getLocation());
					next();
					for(;;) {
						if(token == null)
							unexpected(Token.Type.NAME, Token.Type.RIGHT_CURLY);
						if(token.getType() == Token.Type.RIGHT_CURLY) {
							next();
							break;
						}
						if(token.getType() != Token.Type.NAME)
							unexpected(Token.Type.NAME, Token.Type.RIGHT_CURLY);
						TypeSpecifier fieldType = parseType();
						expect(Token.Type.NAME);
						body.addField(new TypeDefinition.FieldsBody.Field(fieldType,
								token.getLocation(), token.getRawText()));
						next();
						consume(Token.Type.SEMICOLON);
					}
					type.setBody(body);
				}
				break;
			case NATIVE:
				{
					Location bodyLocation = token.getLocation();
					next();
					type.setBody(new TypeDefinition.NativeBody(bodyLocation, parseClassReference()));
					consume(Token.Type.SEMICOLON);
				}
				break;
			default:
				unexpected(Token.Type.LEFT_CURLY, Token.Type.NATIVE);
		}
		return type;
	}

	private PropertyDefinition parseProperty() {
		Location initiator = token.getLocation();
		next();
		TypeSpecifier returnType = parseType();
		expect(Token.Type.NAME);
		PropertyDefinition property = new PropertyDefinition(initiator, returnType,
				token.getRawText(), token.getLocation());
		next();
		if(token == null)
			unexpected(Token.Type.LESS, Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.SEMICOLON);
		switch(token.getType()) {
			case LESS:
				parseTemplateParameters(property);
				break;
			case LEFT_ROUND:
			case ASSIGN:
			case SEMICOLON:
				break;
			default:
				unexpected(Token.Type.LESS, Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.SEMICOLON);
		}
		if(token == null)
			unexpected(Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.SEMICOLON);
		switch(token.getType()) {
			case LEFT_ROUND:
				parseParameters(property);
				break;
			case ASSIGN:
			case SEMICOLON:
				break;
			default:
				unexpected(Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.SEMICOLON);
		}
		if(token == null)
			unexpected(Token.Type.ASSIGN, Token.Type.SEMICOLON);
		switch(token.getType()) {
			case ASSIGN:
				{
					Location bottomInitiator = token.getLocation();
					next();
					PropertyDefinition.Bottom bottom = new PropertyDefinition.Bottom(initiator, parseQName());
					if(token == null)
						unexpected(Token.Type.LESS, Token.Type.SEMICOLON);
					switch(token.getType()) {
						case LESS:
							parseTemplateArguments(bottom);
							expect(Token.Type.SEMICOLON);
							break;
						case SEMICOLON:
							break;
						default:
							unexpected(Token.Type.LESS, Token.Type.SEMICOLON);
					}
					property.setBottom(bottom);
				}
				break;
			case SEMICOLON:
				break;
			default:
				unexpected(Token.Type.ASSIGN, Token.Type.SEMICOLON);
		}
		next();
		while(token != null && token.getType() == Token.Type.ON)
			property.addTrigger(parsePropertyTrigger());
		return property;
	}

	private Statement parseStatement() {
		Expression expression = parseExpression();
		consume(Token.Type.SEMICOLON);
		return new Statement(expression);
	}

	private Expression parseExpression() {
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

	private static boolean startsExpression(Token.Type type) {
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
			case UNTIL:
			case BITWISE_NOT:
			case INCREMENT:
			case DECREMENT:
			case LEFT_SQUARE:
			case LEFT_ROUND:
			case BREAK:
			case NAME:
			case FOR:
			case IF:
			case LAMBDA:
			case UNLESS:
			case WHILE:
			case LEFT_CURLY:
			case USING:
			case RETURN:
			case REQUIRE:
			case NEW:
			case PERCENT_LEFT_CURLY:
			case FOREACH:
			case USE:
			case CONTINUE:
			case UNUSE:
				return true;
			default:
				return false;
		}
	}

}
