package org.hedinger.scaffold.node;

public abstract class AbstractNode
{
    private static long ID_INC = 0L;
    protected final long id;

    public AbstractNode()
    {
        id = ID_INC++;
    }

    public abstract boolean isLeaf();

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " id=" + id;
    }
}
