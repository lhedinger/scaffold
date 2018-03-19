package org.hedinger.scaffold.markers;

import static org.hedinger.scaffold.markers.Status.CLOSED;
import static org.hedinger.scaffold.markers.Status.FAILED;
import static org.hedinger.scaffold.markers.Status.FORK;
import static org.hedinger.scaffold.markers.Status.OPEN;

import java.util.ArrayList;
import java.util.List;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.BranchNode;
import org.hedinger.scaffold.node.LeafNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class StringBranch extends StringNode {

    private BranchNode branchTemplate;
    private ArrayList<StringNode> children = new ArrayList<>();

    private StringBounds maxRange;

    private final boolean optional;
    private final int maxRepetitions;
    private final int modulo;

    private int repetition = 0;
    private int childIterator = -2;

    private boolean done = false;
    private boolean skipReq = false;

    public StringBranch(AbstractNode template, SmartBuffer input) {
        super(template, input);
        branchTemplate = (BranchNode) template;
        optional = branchTemplate.isOptional();
        maxRepetitions = branchTemplate.getMaxRepetitions();
        modulo = branchTemplate.getChildren().size();
        maxRange = new StringBounds(0, 0);
    }

    public StringBranch(AbstractNode template, StringNode parent, int start, int end) {
        super(template, parent, start, end);
        branchTemplate = (BranchNode) template;
        optional = branchTemplate.isOptional();
        maxRepetitions = branchTemplate.getMaxRepetitions();
        modulo = branchTemplate.getChildren().size();
        maxRange = new StringBounds(start, start);
    }

    @Override
    public Status grow(int signal) throws Exception {

        if (childIterator == -1) {
            if (optional || repetition > 0) {
                if (signal % 2 == 0) {
                    childIterator = -9;
                    return CLOSED;
                }
            }
            childIterator = 0;
        }
        if (childIterator == -2) {
            if (optional || repetition > 0) {
                childIterator = -1;
                return FORK;
            }
            childIterator = 0;
        }

        StringNode currentChild = nextChild();

        Status status = currentChild.grow(signal);
        updateMaxRange(currentChild.getRange());

        if (status == OPEN || status == FORK || status == FAILED) {
            return status;
        }

        if (status == CLOSED) {
            childIterator++;
            if (childIterator >= modulo) {
                childIterator = -2;
                repetition++;
            }
        }



        if (repetition >= maxRepetitions && childIterator == -1 && maxRepetitions >= 0) {
            return CLOSED;
        }

        return OPEN;
    }

    private void updateMaxRange(StringBounds childRange) {
        if (childRange == null) {
            return;
        }
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

    private StringNode nextChild() throws Exception {
        int absoluteIndex = childIterator + (repetition) * modulo;
        if (absoluteIndex < children.size()) {
            return children.get(absoluteIndex);
        }
        StringNode nodeChild;
        AbstractNode templateChild = branchTemplate.getChildren().get(childIterator);
        if (templateChild instanceof BranchNode) {
            nodeChild = new StringBranch(templateChild, this, maxRange.end, StringBounds.UNDEF);
        } else if (templateChild instanceof LeafNode) {
            nodeChild = new StringLeaf(templateChild, this, maxRange.end, StringBounds.UNDEF);
        } else {
            throw new Exception("template is not valid type");
        }
        children.add(nodeChild);
        return nodeChild;
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
    public StringNode deepClone() {
        StringBranch branch;

        if (parent == null) {
            branch = new StringBranch(template, input);
        } else {
            branch = new StringBranch(template, parent, maxRange.start, maxRange.end);
        }
        branch.childIterator = this.childIterator;
        branch.done = this.done;
        branch.repetition = this.repetition;
        branch.maxRange = new StringBounds(maxRange.start, maxRange.end);

        for (StringNode child : children) {
            branch.children.add(child.deepClone());
        }

        return branch;
    }

    @Override
    public String toString() {
        return "range:" + String.valueOf(maxRange) + "  "
                + template.toString() + "\t\t" + (done ? "x" : "") + " ci=" + childIterator;
    }
}
