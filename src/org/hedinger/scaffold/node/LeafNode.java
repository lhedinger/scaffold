package org.hedinger.scaffold.node;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public abstract class LeafNode extends AbstractNode {
    public static final String VALUE_NULL = "NULL";

    protected final String value;

    public LeafNode(String value) {
        this.value = (value == null || value.isEmpty() ? VALUE_NULL : value);
    }

    public String getValue() {
        return value;
    }
    
    public abstract StringBounds matches(SmartBuffer body, StringBounds allowedRange) throws Exception;

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " value='" + value + "'";
    }
}
