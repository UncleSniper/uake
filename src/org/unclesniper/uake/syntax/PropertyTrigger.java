package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class PropertyTrigger extends Syntax implements TemplateInvocation {

	public enum Event {
		USE,
		UNUSE
	}

	private Event event;

	private QualifiedName callbackName;

	private final List<TypeSpecifier> templateArguments = new LinkedList<TypeSpecifier>();

	public PropertyTrigger(Location initiator, Event event, QualifiedName callbackName) {
		super(initiator);
		this.event = event;
		this.callbackName = callbackName;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public QualifiedName getCallbackName() {
		return callbackName;
	}

	public void setCallbackName(QualifiedName callbackName) {
		this.callbackName = callbackName;
	}

	public Iterable<TypeSpecifier> getTemplateArguments() {
		return templateArguments;
	}

	public void addTemplateArgument(TypeSpecifier argument) {
		if(argument != null)
			templateArguments.add(argument);
	}

}
