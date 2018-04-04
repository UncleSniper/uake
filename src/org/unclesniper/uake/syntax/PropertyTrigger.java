package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class PropertyTrigger extends Syntax {

	public enum Event {
		USE,
		UNUSE
	}

	private Event event;

	private Expression callback;

	public PropertyTrigger(Location initiator, Event event, Expression callback) {
		super(initiator);
		this.event = event;
		this.callback = callback;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Expression getCallback() {
		return callback;
	}

	public void setCallback(Expression callback) {
		this.callback = callback;
	}

}
