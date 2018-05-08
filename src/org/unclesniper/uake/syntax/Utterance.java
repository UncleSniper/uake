package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.CompilationContext;

public class Utterance extends Syntax {

	private final List<Header> headers = new LinkedList<Header>();

	private final List<TopLevel> topLevels = new LinkedList<TopLevel>();

	public Utterance(Location location) {
		super(location);
	}

	public void addHeader(Header header) {
		if(header != null)
			headers.add(header);
	}

	public void addTopLevel(TopLevel topLevel) {
		if(topLevel != null)
			topLevels.add(topLevel);
	}

	public void compile(CompilationContext cctx) {
		//TODO: grok headers
		for(TopLevel topLevel : topLevels)
			topLevel.createElements(cctx);
		for(TopLevel topLevel : topLevels)
			topLevel.bindTypes(cctx);
	}

}
