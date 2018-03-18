package org.unclesniper.uake.syntax;

import java.util.List;
import java.util.LinkedList;
import org.unclesniper.uake.Location;

public class TypeDefinition extends AbstractTemplate {

	public static abstract class Body extends Syntax {

		public Body(Location location) {
			super(location);
		}

	}

	private String name;

	private Location nameLocation;

	private final List<TypeSpecifier> supertypes = new LinkedList<TypeSpecifier>();

	private Body body;

	public TypeDefinition(Location initiator, String name, Location nameLocation, Body body) {
		super(initiator);
		this.name = name;
		this.nameLocation = nameLocation;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getNameLocation() {
		return nameLocation;
	}

	public void setNameLocation(Location nameLocation) {
		this.nameLocation = nameLocation;
	}

	public Iterable<TypeSpecifier> getSupertypes() {
		return supertypes;
	}

	public void addSupertype(TypeSpecifier supertype) {
		if(supertype != null)
			supertypes.add(supertype);
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

}
