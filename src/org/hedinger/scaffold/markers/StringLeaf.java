package org.hedinger.scaffold.markers;

import java.util.List;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.LeafNode;
import org.hedinger.scaffold.utils.StringBounds;

public class StringLeaf extends StringNode {

    private LeafNode leafTemplate;
    private StringBounds matchedRange;

    private String output;

    public StringLeaf(AbstractNode template, StringNode parent, int start, int end) {
        super(template, parent, start, end);
        leafTemplate = (LeafNode) template;
    }

    @Override
    public void grow(int step) throws Exception {

        if (matchedRange != null) {
            return;
        }

        matchedRange = leafTemplate.matches(input, allowedRange);
        output = input.substring(matchedRange);
    }

    @Override
    public boolean isDone() {
        return matchedRange != null;
    }

    @Override
    public List<StringNode> getChildren() {
        return null;
    }

    @Override
    public String generateOutput() {
        return output;
    }

    @Override
    public StringBounds getRange() {
        return matchedRange;
    }

    @Override
    public int calcCoverage() {
        if (matchedRange == null) {
            return 0;
        }
        if (!matchedRange.isFinite()) {
            return 0;
        }
        return (matchedRange.end - matchedRange.start);
    }

    @Override
    public String toString() {
        return "range:" + String.valueOf(matchedRange) + "  value:" + output + "    " + template.toString();
    }
}
