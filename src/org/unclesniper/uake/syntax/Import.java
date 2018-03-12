package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class Import extends Header {

	public static abstract class Tail {

		public Tail() {}

	}

	public static class AsTail extends Tail {

		private QualifiedName.Segment alias;

		public AsTail(QualifiedName.Segment alias) {
			this.alias = alias;
		}

		public QualifiedName.Segment getAlias() {
			return alias;
		}

		public void setAlias(QualifiedName.Segment alias) {
			this.alias = alias;
		}

	}

	public static class EndTail extends Tail {

		private QualifiedName.Segment segment;

		private Tail tail;

		public EndTail(QualifiedName.Segment segment, Tail tail) {
			this.segment = segment;
			this.tail = tail;
		}

		public QualifiedName.Segment getSegment() {
			return segment;
		}

		public void setSegment(QualifiedName.Segment segment) {
			this.segment = segment;
		}

		public Tail getTail() {
			return tail;
		}

		public void setTail(Tail tail) {
			this.tail = tail;
		}

	}

	public static class SplitTail extends Tail {

		private final List<EndTail> tails = new LinkedList<EndTail>();

		public SplitTail() {}

		public Iterable<EndTail> getTails() {
			return tails;
		}

		public void addTail(EndTail tail) {
			if(tail != null)
				tails.add(tail);
		}

	}

	private QualifiedName.Segment head;

	private Tail tail;

	public Import(Location initiator, QualifiedName.Segment head, Tail tail) {
		super(initiator);
		this.head = head;
		this.tail = tail;
	}

	public QualifiedName.Segment getHead() {
		return head;
	}

	public void setHead(QualifiedName.Segment head) {
		this.head = head;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}

}
