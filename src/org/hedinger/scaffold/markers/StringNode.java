package org.hedinger.scaffold.markers;

import java.util.List;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public abstract class StringNode {
    protected final AbstractNode template;
    protected final StringNode parent;
    protected final SmartBuffer input;
    protected StringBounds allowedRange;

    public StringNode(AbstractNode template, SmartBuffer input) {
        this.template = template;
        this.input = input;
        this.parent = null;
        allowedRange = new StringBounds(0, StringBounds.UNDEF);
    }

    public StringNode(AbstractNode template, StringNode parent, int start, int end) {
        this.template = template;
        this.input = parent.input;
        this.parent = parent;
        allowedRange = new StringBounds(start, end);
    }

    public boolean growOne() throws Exception {
        return grow(1);
    }

    public abstract boolean grow(int step) throws Exception;

    public abstract boolean isDone();

    public abstract List<StringNode> getChildren();

    public abstract StringBounds getRange();

    public abstract String generateOutput();

    public abstract int calcCoverage();
    
}
