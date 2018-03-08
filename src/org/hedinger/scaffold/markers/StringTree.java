package org.hedinger.scaffold.markers;

import java.util.ArrayList;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.BranchNode;
import org.hedinger.scaffold.utils.StringBounds;

public class StringTree
{
    public final AbstractNode template;

    private final StringTree parent;
    private ArrayList<StringTree> children = new ArrayList<StringTree>();

    public StringBounds rangeMatch, rangeLimit;

    private boolean leaf, done, grown, matched, optional;
    private int repetitions;

    public StringTree(AbstractNode template, int limit)
    {
        this.template = template;
        this.parent = null;
        this.rangeMatch = new StringBounds(0, StringBounds.UNDEF);
        this.rangeLimit = new StringBounds(0, limit);
        setAttributes();
    }
    
    public StringTree(AbstractNode template, StringTree parent, int start, int end)
    {
        this.template = template;
        this.parent = parent;
        this.rangeLimit = new StringBounds(start, end);
        this.rangeMatch = new StringBounds(rangeLimit.start, StringBounds.UNDEF);
        setAttributes();
    }

    private void setAttributes()
    {
        done = false;
        grown = false;
        matched = false;
        optional = false;
        leaf = template.isLeaf();

        if (!(template instanceof BranchNode)) return;

        BranchNode branch = (BranchNode) template;

        if (branch.isOptional()) optional = true;

        repetitions = branch.getRepetitions();
    }
}