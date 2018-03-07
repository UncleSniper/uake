package org.unclesniper.uake;

import java.io.Reader;
import java.util.Deque;
import java.io.IOException;
import java.util.LinkedList;

public class PullLexer implements Source<Token>, LocationTracker {

	private final PushLexer lexer;

	private final Deque<Token> tokens;

	private Reader inputReader;

	private final char[] buffer = new char[256];

	public PullLexer(Reader inputReader, String file) {
		this.inputReader = inputReader;
		tokens = new LinkedList<Token>();
		lexer = new PushLexer(new CollectionSink<Token>(tokens), file);
	}

	public PushLexer getLexer() {
		return lexer;
	}

	public Reader getInputReader() {
		return inputReader;
	}

	public void setInputReader(Reader inputReader) {
		this.inputReader = inputReader;
	}

	public Token next() {
		try {
			while(tokens.isEmpty()) {
				int count = inputReader.read(buffer);
				if(count < 0) {
					lexer.endUnit();
					if(tokens.isEmpty())
						return null;
					break;
				}
				lexer.pushSource(buffer, 0, count);
			}
		}
		catch(IOException ioe) {
			throw new SourceReadIOException(lexer.getLocation(), ioe);
		}
		return tokens.removeFirst();
	}

	public Location getLocation() {
		return lexer.getLocation();
	}

}
