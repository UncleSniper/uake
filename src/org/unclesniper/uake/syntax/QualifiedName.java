package org.unclesniper.uake.syntax;

import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class QualifiedName {

	public static class Segment {

		private Location location;

		private String name;

		public Segment(Location location, String name) {
			this.location = location;
			this.name = name;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	private final LinkedList<Segment> segments = new LinkedList<Segment>();

	public QualifiedName() {}

	public QualifiedName(QualifiedName head, String tail, Location location) {
		if(head != null) {
			for(Segment segment : head.getSegments())
				segments.add(new Segment(segment.getLocation(), segment.getName()));
		}
		if(tail != null)
			segments.add(new Segment(location == null ? Location.UNKNOWN : location, tail));
	}

	public Iterable<Segment> getSegments() {
		return segments;
	}

	public void addSegment(Segment segment) {
		if(segment != null)
			segments.add(segment);
	}

	public Segment getTail() {
		return segments.isEmpty() ? null : segments.getLast();
	}

	public Location getLocation() {
		return segments.isEmpty() ? null : segments.get(0).location;
	}

	public String toString(char delimiter) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Segment segment : segments) {
			if(first)
				first = false;
			else
				builder.append(delimiter);
			builder.append(segment.getName());
		}
		return builder.toString();
	}

}
