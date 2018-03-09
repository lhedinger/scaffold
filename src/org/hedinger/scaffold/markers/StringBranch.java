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
    private int childIndex = 0;
    private StringNode currentChild;

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
    public boolean grow(int step) throws Exception {

        if (done) {
            return true;
        }

        if (currentChild == null) {
            childIndex = 0;
            currentChild = nextChild();
        }

        boolean good = currentChild.grow(step);

        if (!good) {
            // we over-reached; the leaf failed
            if(optional || repetition >= 1) {
                dropIncompleteChildSet();
                done = true;
                return true;
            }
            return false;
        }

        updateMaxRange(currentChild.getRange());

        if (currentChild.isDone()) {
            childIndex++;
            if (childIndex >= modulo) {
                childIndex = 0;
                repetition++;
            }

            if (childIndex == 0) {
                // means we are done with a set
                if (repetition == maxRepetitions || maxRepetitions == 0) {
                    done = true;
                    return true;
                }
            }

            currentChild = nextChild();
        }

        return true;
    }

    private void dropIncompleteChildSet() {
        for (int i = 0; i <= childIndex; i++) {
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
        StringNode nodeChild;
        AbstractNode templateChild = branchTemplate.getChildren().get(childIndex);
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
    public String toString() {
        return "range:" + String.valueOf(maxRange) + "  " + template.toString();
    }
}
