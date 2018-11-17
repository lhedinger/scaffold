package org.hedinger.scaffold.node;

import java.util.ArrayList;
import java.util.List;

public class BranchNode extends AbstractNode {
	private static final int INFINITY = -1;
	protected final int repMin;
	protected final int repMax;

	private ArrayList<AbstractNode> children = new ArrayList<AbstractNode>();
	private int childCrawler = 0;

	public BranchNode(String name) {
		super(name);
		repMin = 1;
		repMax = 1;
	}

	public BranchNode(String name, int min) {
		super(name);
		repMin = (min < 0 ? 0 : min);
		repMax = INFINITY;
	}

	public BranchNode(String name, int min, int max) {
		super(name);
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

	public void addNode(AbstractNode node) {
		children.add(node);
	}

	public AbstractNode getFirst() {
		childCrawler = 1;
		return children.get(0);
	}

	public AbstractNode getNext() {
		if (childCrawler >= children.size()) {
			return null;
		}

		return children.get(childCrawler++);
	}

	public List<AbstractNode> getChildren() {
		return children;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public String toString() {
		return "BranchNode_" + id + "  [" + repMin + "-" + (repMax == INFINITY ? "INF" : repMax) + "]";
	}

}
