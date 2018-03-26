package org.unclesniper.uake.semantics;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.IdentityHashMap;
import org.unclesniper.uake.Location;
import org.unclesniper.uake.syntax.QualifiedName;

public class UakeModule extends AbstractMember {

	public static class Group<MemberT> implements Iterable<MemberT> {

		private final LinkedList<MemberT> members = new LinkedList<MemberT>();

		private final IdentityHashMap<MemberT, MemberT> known = new IdentityHashMap<MemberT, MemberT>();

		public Group() {}

		public Group(Iterable<MemberT> members) {
			for(MemberT member : members)
				addMember(member);
		}

		public Iterator<MemberT> iterator() {
			return members.iterator();
		}

		public Iterable<MemberT> getMembers() {
			return members;
		}

		public void addMember(MemberT member) {
			if(member != null && !known.containsKey(member)) {
				members.add(member);
				known.put(member, member);
			}
		}

		public boolean isEmpty() {
			return members.isEmpty();
		}

		public int getSize() {
			return members.size();
		}

		public boolean isAmbiguous() {
			return members.size() > 1;
		}

		public MemberT getFirst() {
			return members.isEmpty() ? null : members.getFirst();
		}

		public Group<MemberT> select(Filter<MemberT> filter) {
			Group<MemberT> selection = new Group<MemberT>();
			for(MemberT member : members) {
				if(filter == null || filter.accept(member))
					selection.addMember(member);
			}
			return selection;
		}

		public void select(Filter<MemberT> filter, Group<MemberT> selection) {
			for(MemberT member : members) {
				if(filter == null || filter.accept(member))
					selection.addMember(member);
			}
		}

		public <SpecialT> Group<SpecialT> map(Transform<MemberT, ? extends SpecialT> transform) {
			Group<SpecialT> selection = new Group<SpecialT>();
			if(transform != null) {
				for(MemberT member : members) {
					SpecialT special = transform.transform(member);
					if(special != null)
						selection.addMember(special);
				}
			}
			return selection;
		}

		public <SpecialT> void map(Transform<MemberT, ? extends SpecialT> transform, Group<SpecialT> selection) {
			if(transform != null) {
				for(MemberT member : members) {
					SpecialT special = transform.transform(member);
					if(special != null)
						selection.addMember(special);
				}
			}
		}

		public void mergeInto(Group<? super MemberT> group) {
			for(MemberT member : members)
				group.addMember(member);
		}

	}

	public static final Filter<UakeMember> MODULE_FILTER = new InstanceofFilter<UakeMember>(UakeModule.class);

	public static final Transform<UakeMember, UakeModule> MODULE_TRANSFORM
			= new InstanceofTransform<UakeMember, UakeModule>(UakeModule.class);

	private final Map<String, Group<UakeMember>> groups = new HashMap<String, Group<UakeMember>>();

	private UakeModule parent;

	public UakeModule(QualifiedName qualifiedName, UakeModule parent) {
		super(qualifiedName);
		this.parent = parent;
	}

	public UakeModule getParent() {
		return parent;
	}

	public void setParent(UakeModule parent) {
		this.parent = parent;
	}

	public void put(UakeMember member) {
		put(member, null);
	}

	public void put(UakeMember member, String name) {
		if(member == null)
			return;
		if(name == null) {
			name = member.getUnqualifiedName();
			if(name == null)
				return;
		}
		Group<UakeMember> group = groups.get(name);
		if(group == null) {
			group = new Group<UakeMember>();
			groups.put(name, group);
		}
		group.addMember(member);
	}

	public void putrec(UakeMember member) {
		putrec(member, null);
	}

	public void putrec(UakeMember member, QualifiedName name) {
		if(member == null)
			return;
		if(name == null) {
			name = member.getQualifiedName();
			if(name == null)
				return;
		}
		Group<UakeModule> modules = new Group<UakeModule>();
		modules.addMember(this);
		String prevName = null;
		Location prevLocation = null;
		for(QualifiedName.Segment segment : name.getSegments()) {
			String uqname = segment.getName();
			if(prevName != null) {
				Group<UakeModule> nextModules = new Group<UakeModule>();
				for(UakeModule module : modules)
					module.gete(prevName).map(UakeModule.MODULE_TRANSFORM, nextModules);
				if(nextModules.isEmpty()) {
					UakeModule first = modules.getFirst();
					QualifiedName qname = new QualifiedName(first.getQualifiedName(), prevName, null);
					UakeModule inner = new UakeModule(qname, first);
					first.put(inner);
					nextModules.addMember(inner);
				}
				modules = nextModules;
			}
			prevName = uqname;
			prevLocation = segment.getLocation();
		}
		if(prevName != null) {
			for(UakeModule module : modules)
				module.put(member, prevName);
		}
	}

	public Group<UakeMember> get(String name) {
		return groups.get(name);
	}

	public Group<UakeMember> gete(String name) {
		Group<UakeMember> group = groups.get(name);
		return group == null ? new Group<UakeMember>() : group;
	}

	public Group<UakeMember> get(QualifiedName name) {
		Group<UakeMember> selection = new Group<UakeMember>();
		selection.addMember(this);
		for(QualifiedName.Segment segment : name.getSegments()) {
			Group<UakeMember> nextSelection = new Group<UakeMember>();
			for(UakeModule module : selection.map(UakeModule.MODULE_TRANSFORM))
				module.gete(segment.getName()).mergeInto(nextSelection);
			selection = nextSelection;
			if(selection.isEmpty())
				break;
		}
		return selection;
	}

}
