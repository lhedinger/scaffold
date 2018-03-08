package org.hedinger.scaffold.node;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class StaticLeaf extends LeafNode
{
    public StaticLeaf(String value)
    {
        super(value);
    }

    @Override
    public StringBounds matches(SmartBuffer body, int offset) throws Exception
    {
        body.skip(offset);

        int len = value.length();

        int i = body.indexOf(value);

        if (i == -1) return null;

        return new StringBounds(i, i + len);
    }
}
