package org.hedinger.scaffold.template;

import java.util.HashSet;
import java.util.Set;

public abstract class TemplateNode {
	private static long ID_INC = 0L;

	protected final long id;
	protected TemplateNode parent = null;

	private final String name;
	private final Set<Flag> flags;

	public TemplateNode(String name) {
		this.id = ID_INC++;
		this.name = name;
		this.flags = new HashSet<>();
	}

	public TemplateNode(String name, Set<Flag> flags) {
		this.id = ID_INC++;
		this.name = name;
		this.flags = flags;
	}

	public void setParent(TemplateNode node) throws Exception {
		if (parent != null) {
			throw new Exception("Cannot set parent twice");
		}
		parent = node;
	}

	public TemplateNode getParent() {
		return parent;
	}

	public abstract boolean isLeaf();

	public abstract boolean isStrict();

	public boolean hasFlag(Flag flag) {
		return flags.contains(flag);
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "_" + id + "(" + name + ")";
	}

	public enum Flag {
		STRICT, OPTIONAL, LIST
	}
}
