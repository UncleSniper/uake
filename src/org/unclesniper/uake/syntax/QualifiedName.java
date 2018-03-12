package org.unclesniper.uake.syntax;

import java.util.List;
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

	private final List<Segment> segments = new LinkedList<Segment>();

	public QualifiedName() {}

	public Iterable<Segment> getSegments() {
		return segments;
	}

	public void addSegment(Segment segment) {
		if(segment != null)
			segments.add(segment);
	}

	public Location getLocation() {
		return segments.isEmpty() ? null : segments.get(0).location;
	}

}
