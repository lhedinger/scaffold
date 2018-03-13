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
    private ArrayList<StringNode> children = new ArrayList<>();

    private StringBounds maxRange;

    private final boolean optional;
    private final int maxRepetitions;
    private final int modulo;

    private int repetition = 0;
    private int childIterator = -1;

    private boolean done = false;

    public StringBranch(AbstractNode template, SmartBuffer input) {
        super(template, input);
        branchTemplate = (BranchNode) template;
        optional = branchTemplate.isOptional();
        maxRepetitions = branchTemplate.getRepetitions();
        modulo = branchTemplate.getChildren().size();
        maxRange = new StringBounds(0, 0);
    }

    public StringBranch(AbstractNode template, StringNode parent, int start, int end) {
        super(template, parent, start, end);
        branchTemplate = (BranchNode) template;
        optional = branchTemplate.isOptional();
        maxRepetitions = branchTemplate.getRepetitions();
        modulo = branchTemplate.getChildren().size();
        maxRange = new StringBounds(start, start);
    }

    @Override
    public int grow(int signal) throws Exception {

        //FIXME top level node forking
        if (optional && childIterator == -1 && signal % 2 == 0) {
            done = true;
        } else if (repetition > 0 && childIterator == 0 && signal % 2 == 0) {
            done = true;
        }

        if (done) {
            return 0;
        }

        StringNode currentChild;

        if (childIterator == -1) {
            childIterator = 0;
        }

        currentChild = nextChild();

        int good = currentChild.grow(signal);

        if (good == -1) {
            return -1;
        }

        updateMaxRange(currentChild.getRange());

        if (currentChild.isDone()) {
            childIterator++;
            if (childIterator >= modulo) {
                childIterator = 0;
                repetition++;
            }

            if (childIterator == 0) {
                // means we are done with a set
                if (repetition == maxRepetitions || maxRepetitions == 0) {
                    done = true;
                    return 0;
                }
            }
        }

        return 0;
    }

    private void dropIncompleteChildSet() {
        for (int i = 0; i <= childIterator; i++) {
            children.remove(children.size() - 1);
        }
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
