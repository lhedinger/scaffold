package org.hedinger.scaffold.node;

public abstract class AbstractNode {
	private static long ID_INC = 0L;
	protected final long id;
	private final String name;

	public AbstractNode(String name) {
		id = ID_INC++;
		this.name = name;
	}

	public abstract boolean isLeaf();

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " id=" + id + " name=" + name;
	}
}
