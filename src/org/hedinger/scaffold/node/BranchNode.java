package org.hedinger.scaffold.node;

import java.util.ArrayList;

public class BranchNode extends AbstractNode
{
    private static final int INFINITY = -1;
    protected final int repMin;
    protected final int repMax;

    private ArrayList<AbstractNode> children = new ArrayList<AbstractNode>();
    private int childCrawler = 0;

    public BranchNode()
    {
        repMin = 1;
        repMax = 1;
    }

    public BranchNode(int min)
    {
        repMin = (min < 0 ? 0 : min);
        repMax = INFINITY;
    }

    public BranchNode(int min, int max)
    {
        repMin = (min < 0 ? 0 : min);
        repMax = (max < 0 ? INFINITY : max);
    }

    public int getRepetitions()
    {
        if (repMax == INFINITY)
        {
            return INFINITY;
        }

        return repMax - repMin;
    }

    public boolean isOptional()
    {
        return (repMin == 0);
    }

    public void addNode(AbstractNode node)
    {
        children.add(node);
    }

    public AbstractNode getFirst()
    {
        childCrawler = 1;
        return children.get(0);
    }

    public AbstractNode getNext()
    {
        if (childCrawler >= children.size()) return null;

        return children.get(childCrawler++);
    }

    @Override
    public boolean isLeaf()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return "BranchNode_" + id + "  [" + repMin + "-" + (repMax == INFINITY ? "INF" : repMax) + "]";
    }

}
