package org.unclesniper.uake.semantics;

import java.util.List;
import java.util.LinkedList;

public class TypeUtils {

	private static final UakeType[] UAKE_TYPE_ARRAY_TEMPLATE = new UakeType[0];

	private TypeUtils() {}

	public static UakeModule.Group<UakeType> getTransitiveSupertypes(UakeType type) {
		UakeModule.Group<UakeType> selection = new UakeModule.Group<UakeType>();
		UakeModule.Group<UakeType> current = new UakeModule.Group<UakeType>();
		current.addMember(type);
		do {
			UakeModule.Group<UakeType> next = new UakeModule.Group<UakeType>();
			for(UakeType ctype : current)
				ctype.getDirectSupertypes().mergeInto(next);
			next.mergeInto(selection);
			current = next;
		} while(!current.isEmpty());
		return selection;
	}

	public static UakeType[] getTemplateArgumentsAsArray(UakeMember member) {
		List<UakeMember> arguments = new LinkedList<UakeMember>();
		for(UakeType argument : member.getTemplateArguments())
			arguments.add(argument);
		return arguments.toArray(TypeUtils.UAKE_TYPE_ARRAY_TEMPLATE);
	}

}
