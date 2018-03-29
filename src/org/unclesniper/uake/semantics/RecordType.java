package org.unclesniper.uake.semantics;

import java.util.Map;
import java.util.HashMap;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class RecordType extends AbstractType {

	public static class Field {

		private final String name;

		private final UakeType type;

		public Field(String name, UakeType type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public UakeType getType() {
			return type;
		}

	}

	private final Map<String, UakeModule.Group<Field>> fields = new HashMap<String, UakeModule.Group<Field>>();

	public RecordType(QualifiedName qualifiedName, Location definitionLocation) {
		super(qualifiedName, definitionLocation);
	}

	public void addField(Field field) {
		if(field == null)
			return;
		String name = field.getName();
		UakeModule.Group<Field> group = fields.get(name);
		if(group == null) {
			group = new UakeModule.Group<Field>();
			fields.put(name, group);
		}
		group.addMember(field);
	}

	public Class<?> getBackingClass() {
		return null;
	}

}
