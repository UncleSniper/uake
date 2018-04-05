package org.unclesniper.uake.semantics;

public class TemplateArityFilter<TemplateT extends UakeTemplate> implements Filter<TemplateT> {

	private int requiredArity;

	public TemplateArityFilter(int requiredArity) {
		this.requiredArity = requiredArity;
	}

	public int getRequiredArity() {
		return requiredArity;
	}

	public void setRequiredArity(int requiredArity) {
		this.requiredArity = requiredArity;
	}

	public boolean accept(TemplateT template) {
		if(template.getMinTemplateArity() > requiredArity)
			return false;
		int maxArity = template.getMaxTemplateArity();
		if(maxArity >= 0 && maxArity < requiredArity)
			return false;
		return true;
	}

}
