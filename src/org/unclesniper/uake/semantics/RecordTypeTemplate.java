package org.unclesniper.uake.semantics;

import java.util.Map;
import java.util.HashMap;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class RecordTypeTemplate extends AbstractTypeTemplate {

	public static class Field {

		private final String name;

		private final UakeTypeEmitter type;

		public Field(String name, UakeTypeEmitter type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public UakeTypeEmitter getType() {
			return type;
		}

	}

	public static class Instance extends AbstractTypeTemplateInstance {

		private final RecordTypeTemplate backingTemplate;

		public Instance(RecordTypeTemplate backingTemplate, Location emissionLocation) {
			super(backingTemplate.getQualifiedName(), backingTemplate.getDefinitionLocation(), emissionLocation);
			this.backingTemplate = backingTemplate;
		}

		public RecordTypeTemplate getBackingTemplate() {
			return backingTemplate;
		}

		public Class<?> getBackingClass() {
			return null;
		}

	}

	private final Map<String, UakeModule.Group<Field>> fields = new HashMap<String, UakeModule.Group<Field>>();

	public RecordTypeTemplate(QualifiedName qualifiedName, Location definitionLocation) {
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

	public UakeType emitType(UakeType[] templateArguments, Location emissionLocation) {
		TypeUtils.checkTemplateArgumentsAgainstArity(this, templateArguments, emissionLocation);
		Instance instance = new Instance(this, emissionLocation);
		for(int i = 0; i < templateArguments.length; ++i)
			instance.addTemplateArgument(templateArguments[i]);
		return instance;
	}

}
