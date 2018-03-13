package org.hedinger.scaffold.markers;

import java.util.List;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.LeafNode;
import org.hedinger.scaffold.utils.StringBounds;

public class StringLeaf extends StringNode {

    private LeafNode leafTemplate;
    private StringBounds matchedRange;

    private String foundValue;

    public StringLeaf(AbstractNode template, StringNode parent, int start, int end) {
        super(template, parent, start, end);
        leafTemplate = (LeafNode) template;
    }

    @Override
    public int grow(int step) throws Exception {

        if (matchedRange != null) {
            throw new Exception("this leaf has already grown");
        }

        matchedRange = leafTemplate.matches(input, allowedRange);
        foundValue = input.substring(matchedRange);
        
       if (matchedRange == null) {
           return -1;
       }
       return 1;
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
        return foundValue;
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
    public StringNode deepClone() {
        StringLeaf leaf = new StringLeaf(template, parent, allowedRange.start, allowedRange.end);
        leaf.matchedRange = new StringBounds(matchedRange.start, matchedRange.end);
        leaf.foundValue = this.foundValue;
        return leaf;
    }
    
    @Override
    public String toString() {
        return "range:" + String.valueOf(matchedRange) + "  value:" + foundValue + "    " + template.toString();
    }
}
