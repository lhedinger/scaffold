package org.hedinger.scaffold.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TemplateBranchNode extends TemplateNode {
	private static final int INFINITY = -1;
	protected final int repMin;
	protected final int repMax;

	private ArrayList<TemplateNode> children = new ArrayList<TemplateNode>();
	private int childCrawler = 0;

	public TemplateBranchNode(String name, Set<Flag> flags) {
		super(name, flags);
		repMin = 1;
		repMax = 1;
	}

	public TemplateBranchNode(String name, int min, Set<Flag> flags) {
		super(name, flags);
		repMin = (min < 0 ? 0 : min);
		repMax = INFINITY;
	}

	public TemplateBranchNode(String name, int min, int max, Set<Flag> flags) {
		super(name, flags);
		repMin = (min < 0 ? 0 : min);
		repMax = (max < 0 ? INFINITY : max);
	}

	public int getRepetitions() {
		if (repMax == INFINITY) {
			return INFINITY;
		}

		return repMax - repMin;
	}

	public int getMaxRepetitions() {
		return repMax;
	}

	public boolean isOptional() {
		return (repMin == 0);
	}

	@Override
	public boolean isStrict() {
		return hasFlag(Flag.STRICT);
	}

	public void addNode(TemplateNode node) {
		children.add(node);
	}

	public TemplateNode getFirst() {
		childCrawler = 1;
		return children.get(0);
	}

	public TemplateNode getNext() {
		if (childCrawler >= children.size()) {
			return null;
		}

		return children.get(childCrawler++);
	}

	public List<TemplateNode> getChildren() {
		return children;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + "  {" + repMin + "," + (repMax == INFINITY ? "INF" : repMax) + "}";
	}

}
