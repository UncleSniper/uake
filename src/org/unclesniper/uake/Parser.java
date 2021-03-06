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
import org.unclesniper.uake.syntax.IntLiteral;
import org.unclesniper.uake.syntax.MapLiteral;
import org.unclesniper.uake.syntax.IfConstruct;
import org.unclesniper.uake.syntax.ByteLiteral;
import org.unclesniper.uake.syntax.LongLiteral;
import org.unclesniper.uake.syntax.CharLiteral;
import org.unclesniper.uake.syntax.ListLiteral;
import org.unclesniper.uake.syntax.ModuleImport;
import org.unclesniper.uake.syntax.ForConstruct;
import org.unclesniper.uake.syntax.UseConstruct;
import org.unclesniper.uake.syntax.MemberAccess;
import org.unclesniper.uake.syntax.ShortLiteral;
import org.unclesniper.uake.syntax.FloatLiteral;
import org.unclesniper.uake.syntax.NewConstruct;
import org.unclesniper.uake.syntax.QualifiedName;
import org.unclesniper.uake.syntax.TypeSpecifier;
import org.unclesniper.uake.syntax.Parameterized;
import org.unclesniper.uake.syntax.Juxtaposition;
import org.unclesniper.uake.syntax.NameReference;
import org.unclesniper.uake.syntax.DoubleLiteral;
import org.unclesniper.uake.syntax.StringLiteral;
import org.unclesniper.uake.syntax.ClassReference;
import org.unclesniper.uake.syntax.TypeDefinition;
import org.unclesniper.uake.syntax.WhileConstruct;
import org.unclesniper.uake.syntax.UnuseConstruct;
import org.unclesniper.uake.syntax.UsingConstruct;
import org.unclesniper.uake.syntax.BreakConstruct;
import org.unclesniper.uake.syntax.UnaryOperation;
import org.unclesniper.uake.syntax.PropertyTrigger;
import org.unclesniper.uake.syntax.BinaryClassName;
import org.unclesniper.uake.syntax.LambdaConstruct;
import org.unclesniper.uake.syntax.ReturnConstruct;
import org.unclesniper.uake.syntax.BinaryOperation;
import org.unclesniper.uake.syntax.ModuleDefinition;
import org.unclesniper.uake.syntax.ForeachConstruct;
import org.unclesniper.uake.syntax.TemplateParameter;
import org.unclesniper.uake.syntax.FunctionDefinition;
import org.unclesniper.uake.syntax.VariableDefinition;
import org.unclesniper.uake.syntax.TemplateInvocation;
import org.unclesniper.uake.syntax.QualifiedClassName;
import org.unclesniper.uake.syntax.PropertyDefinition;
import org.unclesniper.uake.syntax.ProvisionDefinition;
import org.unclesniper.uake.syntax.InstanceofOperation;

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
				return parseProvision();
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
					boolean staticMethod;
					if(token == null)
						unexpected(Token.Type.STATIC, Token.Type.STRING, Token.Type.NAME);
					if(token.getType() == Token.Type.STATIC) {
						staticMethod = true;
						next();
						if(token == null)
							unexpected(Token.Type.STRING, Token.Type.NAME);
					}
					else
						staticMethod = false;
					String methodName;
					switch(token.getType()) {
						case STRING:
							methodName = token.getCookedText();
							break;
						case NAME:
							methodName = token.getRawText();
							break;
						default:
							if(staticMethod)
								unexpected(Token.Type.STRING, Token.Type.NAME);
							else
								unexpected(Token.Type.STATIC, Token.Type.STRING, Token.Type.NAME);
							return null;
					}
					function.setBody(new FunctionDefinition.NativeBody(bodyInitiator, classReference,
							staticMethod, methodName, token.getLocation()));
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
		PropertyTrigger trigger = new PropertyTrigger(initiator, event, parseExpression());
		consume(Token.Type.SEMICOLON);
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
				next();
				property.setBottom(parseExpression());
				expect(Token.Type.SEMICOLON);
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

	private ProvisionDefinition parseProvision() {
		Location initiator = consume(Token.Type.PROVIDE);
		TypeSpecifier returnType = parseType();
		expect(Token.Type.NAME);
		ProvisionDefinition provision = new ProvisionDefinition(initiator, returnType,
				token.getRawText(), token.getLocation());
		next();
		if(token == null)
			unexpected(Token.Type.LESS, Token.Type.LEFT_ROUND, Token.Type.IF, Token.Type.ASSIGN,
					Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		switch(token.getType()) {
			case LESS:
				parseTemplateParameters(provision);
				break;
			case LEFT_ROUND:
			case IF:
			case ASSIGN:
			case LEFT_CURLY:
			case RETURN:
			case USING:
				break;
			default:
				unexpected(Token.Type.LESS, Token.Type.LEFT_ROUND, Token.Type.IF, Token.Type.ASSIGN,
						Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		}
		if(token == null)
			unexpected(Token.Type.LEFT_ROUND, Token.Type.IF, Token.Type.ASSIGN,
					Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		switch(token.getType()) {
			case LEFT_ROUND:
				parseParameters(provision);
				break;
			case IF:
			case ASSIGN:
			case LEFT_CURLY:
			case RETURN:
			case USING:
				break;
			default:
				unexpected(Token.Type.LEFT_ROUND, Token.Type.IF, Token.Type.ASSIGN,
						Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		}
		if(token == null)
			unexpected(Token.Type.IF, Token.Type.ASSIGN, Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		switch(token.getType()) {
			case IF:
				{
					Location triggerInitiator = token.getLocation();
					next();
					provision.setTrigger(new ProvisionDefinition.ConditionTrigger(triggerInitiator,
							parseExpression()));
					consume(Token.Type.RIGHT_ARROW);
				}
				break;
			case ASSIGN:
				{
					Location triggerInitiator = token.getLocation();
					next();
					provision.setTrigger(new ProvisionDefinition.EquationTrigger(triggerInitiator,
							parseExpression()));
					consume(Token.Type.RIGHT_ARROW);
				}
				break;
			case LEFT_CURLY:
			case RETURN:
			case USING:
				break;
			default:
				unexpected(Token.Type.IF, Token.Type.ASSIGN,
						Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		}
		if(token == null)
			unexpected(Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		switch(token.getType()) {
			case LEFT_CURLY:
				provision.setBody(new ProvisionDefinition.BlockBody(parseBlock()));
				break;
			case RETURN:
				{
					Location bodyInitiator = token.getLocation();
					next();
					provision.setBody(new ProvisionDefinition.ExpressionBody(bodyInitiator, parseExpression()));
					consume(Token.Type.SEMICOLON);
				}
				break;
			case USING:
				{
					Location bodyInitiator = token.getLocation();
					next();
					provision.setBody(new ProvisionDefinition.CallBody(bodyInitiator, parseExpression()));
					consume(Token.Type.SEMICOLON);
				}
				break;
			default:
				unexpected(Token.Type.LEFT_CURLY, Token.Type.RETURN, Token.Type.USING);
		}
		return provision;
	}

	private Statement parseStatement() {
		Expression expression = parseExpression();
		consume(Token.Type.SEMICOLON);
		return new Statement(expression);
	}

	private Expression parseExpression() {
		if(token == null)
			unexpected("expression");
		switch(token.getType()) {
			case IF:
			case UNLESS:
				return parseIf();
			case FOR:
				return parseFor();
			case FOREACH:
				return parseForeach();
			case WHILE:
			case UNTIL:
				return parseWhile();
			case USE:
				return parseUse();
			case UNUSE:
				return parseUnuse();
			case USING:
				return parseUsing();
			case LAMBDA:
				return parseLambda();
			case BREAK:
			case CONTINUE:
				return parseBreak();
			case RETURN:
				return parseReturn();
			default:
				if(Parser.startsPIExpression(token.getType()))
					return parsePIExpression();
				else {
					unexpected("expression");
					return null;
				}
		}
	}

	private IfConstruct parseIf() {
		if(token == null)
			unexpected(Token.Type.IF, Token.Type.UNLESS);
		Location initiator = token.getLocation();
		boolean negated;
		switch(token.getType()) {
			case IF:
				negated = false;
				break;
			case UNLESS:
				negated = true;
				break;
			default:
				unexpected(Token.Type.IF, Token.Type.UNLESS);
				return null;
		}
		next();
		Expression condition = parseExpression();
		consume(Token.Type.THEN);
		IfConstruct ifc = new IfConstruct(initiator, negated, condition, parseStatement());
		for(;;) {
			if(token == null)
				return ifc;
			switch(token.getType()) {
				case ELIF:
					negated = false;
					break;
				case ELUNLESS:
					negated = true;
					break;
				case ELSE:
					ifc.setElseLocation(token.getLocation());
					next();
					ifc.setElseBranch(parseStatement());
					return ifc;
				default:
					return ifc;
			}
			initiator = token.getLocation();
			next();
			condition = parseExpression();
			consume(Token.Type.THEN);
			ifc.addElseIf(new IfConstruct.ElseIf(initiator, negated, condition, parseStatement()));
		}
	}

	private ForConstruct parseFor() {
		Location initiator = consume(Token.Type.FOR);
		ForConstruct.Initialization initialization;
		if(token == null)
			unexpected("'var', 'const', statement or ';'");
		switch(token.getType()) {
			case VAR:
			case CONST:
				initialization = new ForConstruct.VariableInitialization(parseVariable());
				break;
			case SEMICOLON:
				initialization = null;
				next();
				break;
			default:
				if(!Parser.startsExpression(token.getType()))
					unexpected("'var', 'const', statement or ';'");
				initialization = new ForConstruct.StatementInitialization(parseStatement());
				break;
		}
		Expression condition;
		if(token == null)
			unexpected("expression or ';'");
		if(token.getType() == Token.Type.SEMICOLON) {
			condition = null;
			next();
		}
		else if(Parser.startsExpression(token.getType())) {
			condition = parseExpression();
			consume(Token.Type.SEMICOLON);
		}
		else {
			unexpected("expression or ';'");
			return null;
		}
		Expression update;
		if(token == null)
			unexpected("expression or 'do'");
		if(token.getType() == Token.Type.DO) {
			update = null;
			next();
		}
		else if(Parser.startsExpression(token.getType())) {
			update = parseExpression();
			consume(Token.Type.DO);
		}
		else {
			unexpected("expression or 'do'");
			return null;
		}
		return new ForConstruct(initiator, initialization, condition, update, parseStatement());
	}

	private ForeachConstruct parseForeach() {
		Location initiator = consume(Token.Type.FOREACH);
		TypeSpecifier elementType = parseType();
		expect(Token.Type.NAME);
		String bindName = token.getRawText();
		Location bindLocation = token.getLocation();
		next();
		consume(Token.Type.IN);
		Expression collection = parseExpression();
		consume(Token.Type.DO);
		return new ForeachConstruct(initiator, elementType, bindName, bindLocation, collection, parseStatement());
	}

	private WhileConstruct parseWhile() {
		if(token == null)
			unexpected(Token.Type.WHILE, Token.Type.UNTIL);
		boolean negated;
		switch(token.getType()) {
			case WHILE:
				negated = false;
				break;
			case UNTIL:
				negated = true;
				break;
			default:
				unexpected(Token.Type.WHILE, Token.Type.UNTIL);
				return null;
		}
		Location initiator = token.getLocation();
		next();
		Expression condition = parseExpression();
		consume(Token.Type.DO);
		return new WhileConstruct(initiator, negated, condition, parseStatement());
	}

	private UseConstruct parseUse() {
		Location initiator = consume(Token.Type.USE);
		Expression property = parseSubscript();
		Expression implementation = parseSubscript();
		consume(Token.Type.SEMICOLON);
		return new UseConstruct(initiator, property, implementation);
	}

	private UnuseConstruct parseUnuse() {
		Location initiator = consume(Token.Type.UNUSE);
		Expression property = parseExpression();
		consume(Token.Type.SEMICOLON);
		return new UnuseConstruct(initiator, property);
	}

	private UsingConstruct parseUsing() {
		Location initiator = consume(Token.Type.USING);
		Expression property = parseSubscript();
		Expression implementation = parseSubscript();
		consume(Token.Type.IN);
		return new UsingConstruct(initiator, property, implementation, parseStatement());
	}

	private LambdaConstruct parseLambda() {
		Location initiator = consume(Token.Type.LAMBDA);
		TypeSpecifier returnType = parseType();
		LambdaConstruct lambda = new LambdaConstruct(initiator, returnType);
		if(token == null)
			unexpected(Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.RIGHT_ARROW);
		switch(token.getType()) {
			case LEFT_ROUND:
				parseParameters(lambda);
				break;
			case ASSIGN:
			case RETURN:
			case RIGHT_ARROW:
				break;
			default:
				unexpected(Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.RIGHT_ARROW);
				return null;
		}
		if(token == null)
			unexpected(Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.RIGHT_ARROW);
		switch(token.getType()) {
			case ASSIGN:
			case RETURN:
			case RIGHT_ARROW:
				next();
				lambda.setBody(parseExpression());
				break;
			default:
				unexpected(Token.Type.LEFT_ROUND, Token.Type.ASSIGN, Token.Type.RETURN, Token.Type.RIGHT_ARROW);
				return null;
		}
		return lambda;
	}

	private BreakConstruct parseBreak() {
		if(token == null)
			unexpected(Token.Type.BREAK, Token.Type.CONTINUE);
		BreakConstruct.Semantics semantics;
		switch(token.getType()) {
			case BREAK:
				semantics = BreakConstruct.Semantics.BREAK;
				break;
			case CONTINUE:
				semantics = BreakConstruct.Semantics.CONTINUE;
				break;
			default:
				unexpected(Token.Type.BREAK, Token.Type.CONTINUE);
				return null;
		}
		Location initiator = token.getLocation();
		next();
		if(token == null || token.getType() != Token.Type.INT)
			return new BreakConstruct(initiator, semantics, null, null, null);
		String levelSpec = token.getRawText();
		Location levelLocation = token.getLocation();
		next();
		if(token == null || !Parser.startsExpression(token.getType()))
			return new BreakConstruct(initiator, semantics, levelSpec, levelLocation, null);
		return new BreakConstruct(initiator, semantics, levelSpec, levelLocation, parseExpression());
	}

	private ReturnConstruct parseReturn() {
		Location initiator = consume(Token.Type.RETURN);
		Expression returnValue;
		if(token != null && Parser.startsExpression(token.getType()))
			returnValue = parseExpression();
		else
			returnValue = null;
		return new ReturnConstruct(initiator, returnValue);
	}

	private Expression parsePIExpression() {
		return parseAssignment();
	}

	private Expression parseAssignment() {
		Expression leftOperand = parseLogicalOr();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			switch(token.getType()) {
				case ASSIGN:
					function = BinaryOperation.Function.ASSIGN;
					break;
				case PLUS_ASSIGN:
					function = BinaryOperation.Function.PLUS_ASSIGN;
					break;
				case MINUS_ASSIGN:
					function = BinaryOperation.Function.MINUS_ASSIGN;
					break;
				case MULTIPLY_ASSIGN:
					function = BinaryOperation.Function.MULTIPLY_ASSIGN;
					break;
				case DIVIDE_ASSIGN:
					function = BinaryOperation.Function.DIVIDE_ASSIGN;
					break;
				case MODULO_ASSIGN:
					function = BinaryOperation.Function.MODULO_ASSIGN;
					break;
				case AND_ASSIGN:
					function = BinaryOperation.Function.AND_ASSIGN;
					break;
				case XOR_ASSIGN:
					function = BinaryOperation.Function.XOR_ASSIGN;
					break;
				case OR_ASSIGN:
					function = BinaryOperation.Function.OR_ASSIGN;
					break;
				case SHIFT_LEFT_ASSIGN:
					function = BinaryOperation.Function.SHIFT_LEFT_ASSIGN;
					break;
				case SIGNED_SHIFT_RIGHT_ASSIGN:
					function = BinaryOperation.Function.SIGNED_SHIFT_RIGHT_ASSIGN;
					break;
				case UNSIGNED_SHIFT_RIGHT_ASSIGN:
					function = BinaryOperation.Function.UNSIGNED_SHIFT_RIGHT_ASSIGN;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseLogicalOr();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseLogicalOr() {
		Expression leftOperand = parseLogicalAnd();
		for(;;) {
			if(token == null || token.getType() != Token.Type.LOGICAL_OR)
				return leftOperand;
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseLogicalAnd();
			leftOperand = new BinaryOperation(location,
					leftOperand, BinaryOperation.Function.LOGICAL_OR, rightOperand);
		}
	}

	private Expression parseLogicalAnd() {
		Expression leftOperand = parseInstanceof();
		for(;;) {
			if(token == null || token.getType() != Token.Type.LOGICAL_AND)
				return leftOperand;
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseInstanceof();
			leftOperand = new BinaryOperation(location,
					leftOperand, BinaryOperation.Function.LOGICAL_AND, rightOperand);
		}
	}

	private Expression parseInstanceof() {
		Expression leftOperand = parseBitwiseOr();
		for(;;) {
			if(token == null)
				return leftOperand;
			InstanceofOperation.Function function;
			switch(token.getType()) {
				case IS:
					function = InstanceofOperation.Function.IS;
					break;
				case AS:
					function = InstanceofOperation.Function.AS;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			TypeSpecifier rightOperand = parseType();
			leftOperand = new InstanceofOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseBitwiseOr() {
		Expression leftOperand = parseBitwiseXor();
		for(;;) {
			if(token == null || token.getType() != Token.Type.BITWISE_OR)
				return leftOperand;
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseBitwiseXor();
			leftOperand = new BinaryOperation(location,
					leftOperand, BinaryOperation.Function.BITWISE_OR, rightOperand);
		}
	}

	private Expression parseBitwiseXor() {
		Expression leftOperand = parseBitwiseAnd();
		for(;;) {
			if(token == null || token.getType() != Token.Type.BITWISE_XOR)
				return leftOperand;
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseBitwiseAnd();
			leftOperand = new BinaryOperation(location,
					leftOperand, BinaryOperation.Function.BITWISE_XOR, rightOperand);
		}
	}

	private Expression parseBitwiseAnd() {
		Expression leftOperand = parseContainment();
		for(;;) {
			if(token == null || token.getType() != Token.Type.BITWISE_AND)
				return leftOperand;
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseContainment();
			leftOperand = new BinaryOperation(location,
					leftOperand, BinaryOperation.Function.BITWISE_AND, rightOperand);
		}
	}

	private Expression parseContainment() {
		Expression leftOperand = parseEquality();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			Location location = token.getLocation();
			switch(token.getType()) {
				case IN:
					function = BinaryOperation.Function.IN;
					break;
				case NOT:
					function = BinaryOperation.Function.NOT_IN;
					next();
					expect(Token.Type.IN);
					break;
				default:
					return leftOperand;
			}
			next();
			Expression rightOperand = parseEquality();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseEquality() {
		Expression leftOperand = parseRelational();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			switch(token.getType()) {
				case EQUAL:
					function = BinaryOperation.Function.EQUAL;
					break;
				case UNEQUAL:
					function = BinaryOperation.Function.UNEQUAL;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseRelational();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseRelational() {
		Expression leftOperand = parseShift();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			switch(token.getType()) {
				case LESS:
					function = BinaryOperation.Function.LESS;
					break;
				case LESS_EQUAL:
					function = BinaryOperation.Function.LESS_EQUAL;
					break;
				case GREATER:
					function = BinaryOperation.Function.GREATER;
					break;
				case GREATER_EQUAL:
					function = BinaryOperation.Function.GREATER_EQUAL;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseShift();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseShift() {
		Expression leftOperand = parseAdditive();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			switch(token.getType()) {
				case SHIFT_LEFT:
					function = BinaryOperation.Function.SHIFT_LEFT;
					break;
				case SIGNED_SHIFT_RIGHT:
					function = BinaryOperation.Function.SIGNED_SHIFT_RIGHT;
					break;
				case UNSIGNED_SHIFT_RIGHT:
					function = BinaryOperation.Function.UNSIGNED_SHIFT_RIGHT;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseAdditive();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseAdditive() {
		Expression leftOperand = parseMultiplicative();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			switch(token.getType()) {
				case PLUS:
					function = BinaryOperation.Function.PLUS;
					break;
				case MINUS:
					function = BinaryOperation.Function.MINUS;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			Expression rightOperand = parseMultiplicative();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parseMultiplicative() {
		Expression leftOperand = parsePrefix();
		for(;;) {
			if(token == null)
				return leftOperand;
			BinaryOperation.Function function;
			switch(token.getType()) {
				case MULTIPLY:
					function = BinaryOperation.Function.MULTIPLY;
					break;
				case DIVIDE:
					function = BinaryOperation.Function.DIVIDE;
					break;
				case MODULO:
					function = BinaryOperation.Function.MODULO;
					break;
				default:
					return leftOperand;
			}
			Location location = token.getLocation();
			next();
			Expression rightOperand = parsePrefix();
			leftOperand = new BinaryOperation(location, leftOperand, function, rightOperand);
		}
	}

	private Expression parsePrefix() {
		if(token == null)
			return parseJuxtaposition();
		UnaryOperation.Function function;
		switch(token.getType()) {
			case INCREMENT:
				function = UnaryOperation.Function.PRE_INCREMENT;
				break;
			case DECREMENT:
				function = UnaryOperation.Function.PRE_DECREMENT;
				break;
			case PLUS:
				function = UnaryOperation.Function.POSITIVE;
				break;
			case MINUS:
				function = UnaryOperation.Function.NEGATIVE;
				break;
			case BITWISE_NOT:
				function = UnaryOperation.Function.BITWISE_NOT;
				break;
			case LOGICAL_NOT:
			case NOT:
				function = UnaryOperation.Function.LOGICAL_NOT;
				break;
			default:
				return parseJuxtaposition();
		}
		Location location = token.getLocation();
		next();
		return new UnaryOperation(location, function, parsePrefix());
	}

	private Expression parseJuxtaposition() {
		Juxtaposition juxta;
		if(token != null && token.getType() == Token.Type.REQUIRE) {
			juxta = new Juxtaposition(token.getLocation(), token.getLocation());
			next();
			juxta.addPiece(parseSubscript());
		}
		else {
			Expression piece = parseSubscript();
			if(token == null || !Parser.startsSubscript(token.getType()))
				return piece;
			juxta = new Juxtaposition(piece.getLocation(), null);
			juxta.addPiece(piece);
		}
		while(token != null && Parser.startsSubscript(token.getType()))
			juxta.addPiece(parseSubscript());
		return juxta;
	}

	private Expression parseSubscript() {
		Expression leftOperand = parsePostfix();
		for(;;) {
			if(token == null || token.getType() != Token.Type.AT)
				return leftOperand;
			Location location = token.getLocation();
			next();
			Expression rightOperand = parsePostfix();
			leftOperand = new BinaryOperation(location,
					leftOperand, BinaryOperation.Function.SUBSCRIPT, rightOperand);
		}
	}

	private Expression parsePostfix() {
		Expression operand = parsePrimary();
		while(token != null) {
			switch(token.getType()) {
				case INCREMENT:
					operand = new UnaryOperation(token.getLocation(),
							UnaryOperation.Function.POST_INCREMENT, operand);
					next();
					break;
				case DECREMENT:
					operand = new UnaryOperation(token.getLocation(),
							UnaryOperation.Function.POST_DECREMENT, operand);
					next();
					break;
				case DOT:
					operand = parseMemberAccess(operand, MemberAccess.Semantics.FIELD);
					break;
				case COLON:
					operand = parseMemberAccess(operand, MemberAccess.Semantics.MODULE_MEMBER);
					break;
				default:
					return operand;
			}
		}
		return operand;
	}

	private MemberAccess parseMemberAccess(Expression operand, MemberAccess.Semantics semantics) {
		Location location = token.getLocation();
		next();
		expect(Token.Type.NAME);
		MemberAccess access = new MemberAccess(location, operand, semantics,
				token.getRawText(), token.getLocation());
		next();
		return access;
	}

	private Expression parsePrimary() {
		if(token == null)
			unexpected("primary expression");
		switch(token.getType()) {
			case NAME:
				{
					NameReference name = new NameReference(token.getLocation(), token.getRawText());
					next();
					return name;
				}
			case BYTE:
				{
					ByteLiteral literal = new ByteLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case SHORT:
				{
					ShortLiteral literal = new ShortLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case INT:
				{
					IntLiteral literal = new IntLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case LONG:
				{
					LongLiteral literal = new LongLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case FLOAT:
				{
					FloatLiteral literal = new FloatLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case DOUBLE:
				{
					DoubleLiteral literal = new DoubleLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case CHAR:
				{
					CharLiteral literal = new CharLiteral(token.getLocation(), token.getRawText().charAt(0));
					next();
					return literal;
				}
			case STRING:
				{
					StringLiteral literal = new StringLiteral(token.getLocation(), token.getRawText());
					next();
					return literal;
				}
			case LEFT_ROUND:
				{
					next();
					Expression expression = parseExpression();
					consume(Token.Type.RIGHT_ROUND);
					return expression;
				}
			case LEFT_CURLY:
				return parseBlock();
			case LEFT_SQUARE:
				{
					ListLiteral list = new ListLiteral(token.getLocation());
					next();
					if(token == null)
						unexpected("expression or ']'");
					if(token.getType() != Token.Type.RIGHT_SQUARE) {
						if(!Parser.startsExpression(token.getType()))
							unexpected("expression or ']'");
						for(;;) {
							list.addElement(parseExpression());
							if(token == null)
								unexpected("',' or ']'");
							if(token.getType() == Token.Type.RIGHT_SQUARE)
								break;
							if(token.getType() != Token.Type.COMMA)
								unexpected("',' or ']'");
							next();
						}
					}
					next();
					return list;
				}
			case PERCENT_LEFT_CURLY:
				{
					MapLiteral map = new MapLiteral(token.getLocation());
					next();
					if(token == null)
						unexpected("expression or '}'");
					if(token.getType() != Token.Type.RIGHT_CURLY) {
						if(!Parser.startsExpression(token.getType()))
							unexpected("expression or '}'");
						for(;;) {
							map.addBinding(parseMapBinding());
							if(token == null)
								unexpected("',' or '}'");
							if(token.getType() == Token.Type.RIGHT_CURLY)
								break;
							if(token.getType() != Token.Type.COMMA)
								unexpected("',' or '}'");
							next();
						}
					}
					next();
					return map;
				}
			case NEW:
				return parseNew();
			default:
				unexpected("primary expression");
				return null;
		}
	}

	private MapLiteral.Binding parseMapBinding() {
		Expression key = parseExpression();
		Location location = consume(Token.Type.RIGHT_ARROW);
		Expression value = parseExpression();
		return new MapLiteral.Binding(key, location, value);
	}

	private NewConstruct parseNew() {
		Location initiator = consume(Token.Type.NEW);
		NewConstruct newc = new NewConstruct(initiator, parseType());
		if(token == null)
			unexpected("'(' or '{'");
		switch(token.getType()) {
			case LEFT_ROUND:
				next();
				if(token == null)
					unexpected("expression or ')'");
				if(token.getType() != Token.Type.RIGHT_ROUND) {
					if(!Parser.startsExpression(token.getType()))
						unexpected("expression or ')'");
					for(;;) {
						newc.addArgument(parseExpression());
						if(token == null)
							unexpected("',' or ')'");
						if(token.getType() == Token.Type.RIGHT_ROUND)
							break;
						if(token.getType() != Token.Type.COMMA)
							unexpected("',' or ')'");
						next();
					}
				}
				next();
				expect(Token.Type.LEFT_CURLY);
				break;
			case LEFT_CURLY:
				break;
			default:
				unexpected("'(' or '{'");
				return null;
		}
		next();
		if(token == null)
			unexpected("field initializer or '}'");
		if(token.getType() != Token.Type.RIGHT_CURLY) {
			if(token.getType() != Token.Type.NAME)
				unexpected("field initializer or '}'");
			for(;;) {
				Location fieldLocation = token.getLocation();
				String fieldName = token.getRawText();
				next();
				consume(Token.Type.ASSIGN);
				newc.addField(new NewConstruct.FieldInitializer(fieldLocation, fieldName, parseExpression()));
				if(token == null)
					unexpected("',' or '}'");
				if(token.getType() == Token.Type.RIGHT_CURLY)
					break;
				if(token.getType() != Token.Type.COMMA)
					unexpected("',' or '}'");
				next();
			}
		}
		next();
		return newc;
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

	private static boolean startsPIExpression(Token.Type type) {
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
			case NAME:
			case PLUS:
			case LEFT_ROUND:
			case LOGICAL_NOT:
			case BITWISE_NOT:
			case INCREMENT:
			case DECREMENT:
			case LEFT_CURLY:
			case LEFT_SQUARE:
			case REQUIRE:
			case NEW:
			case PERCENT_LEFT_CURLY:
				return true;
			default:
				return false;
		}
	}

	private static boolean startsSubscript(Token.Type type) {
		switch(type) {
			case NAME:
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
			case FLOAT:
			case DOUBLE:
			case CHAR:
			case STRING:
			case LEFT_ROUND:
			case LEFT_SQUARE:
			case LEFT_CURLY:
			case PERCENT_LEFT_CURLY:
			case NEW:
				return true;
			default:
				return false;
		}
	}

}
