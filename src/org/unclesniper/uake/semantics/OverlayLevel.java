package org.unclesniper.uake.semantics;

import java.util.Map;
import java.util.HashMap;
import org.unclesniper.uake.syntax.QualifiedName;

public class OverlayLevel implements UakeScope {

	private final Map<String, UakeModule.Group<UakeMember>> groups
			= new HashMap<String, UakeModule.Group<UakeMember>>();

	public OverlayLevel() {}

	public void put(UakeMember member) {
		put(member, null);
	}

	public void put(UakeMember member, String name) {
		UakeModule.putInto(groups, member, name);
	}

	public void resolve(String name, UakeModule.Group<UakeScoped> sink) {
		UakeModule.Group<UakeMember> selection = groups.get(name);
		if(selection != null)
			selection.map(UakeModule.MEMBER_TO_SCOPED, sink);
	}

}
