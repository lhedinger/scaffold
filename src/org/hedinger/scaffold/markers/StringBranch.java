package org.hedinger.scaffold.markers;

import java.util.ArrayList;
import java.util.List;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.BranchNode;
import org.hedinger.scaffold.node.LeafNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class StringBranch extends StringNode {

    private BranchNode branchTemplate;
    private ArrayList<StringNode> children;

    private StringBounds maxRange;

    private final boolean optional;
    private final int repetitions;

    private boolean done = false;

    public StringBranch(AbstractNode template, SmartBuffer input) {
        super(template, input);
        branchTemplate = (BranchNode) template;
        optional = branchTemplate.isOptional();
        repetitions = branchTemplate.getRepetitions();
    }

    public StringBranch(AbstractNode template, StringNode parent, int start, int end) {
        super(template, parent, start, end);
        branchTemplate = (BranchNode) template;
        optional = branchTemplate.isOptional();
        repetitions = branchTemplate.getRepetitions();
    }

    @Override
    public void grow(int step) throws Exception {

        if (done) {
            return;
        }

        BranchNode n = (BranchNode) template;
        List<AbstractNode> templateChildren = n.getChildren();

        if (children == null) {
            children = new ArrayList<>();
            for (AbstractNode template : templateChildren) {
                StringNode c = null;
                if (template instanceof BranchNode) {
                    c = new StringBranch(template, this, 0, StringBounds.UNDEF);
                } else if (template instanceof LeafNode) {
                    c = new StringLeaf(template, this, 0, StringBounds.UNDEF);
                } else {
                    throw new Exception("template is not valid type");
                }

                children.add(c);
            }
        }

        boolean alldone = true;
        for (StringNode child : children) {
            if (!child.isDone()) {
                alldone = false;
                child.grow(1);

                if (child.isDone()) {
                    StringBounds childRange = child.getRange();

                    if (maxRange == null) {
                        maxRange = new StringBounds(childRange.start, childRange.end);
                    } else {
                        int start = maxRange.start;
                        int end = maxRange.end;

                        if (end < childRange.end) {
                            end = childRange.end;
                        }
                        maxRange = new StringBounds(start, end);
                    }
                }

                return;
            }
        }

        if (alldone) {
            done = true;
        }

    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public List<StringNode> getChildren() {
        return children;
    }

    @Override
    public StringBounds getRange() {
        return maxRange;
    }

    @Override
    public String generateOutput() {
        StringBuilder sb = new StringBuilder();
        for (StringNode child : children) {
            sb.append(child.generateOutput());
        }
        return sb.toString();
    }

    @Override
    public int calcCoverage() {
        int sum = 0;
        for (StringNode child : children) {
            sum += child.calcCoverage();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "range:" + String.valueOf(maxRange) + "  " + template.toString();
    }
}
