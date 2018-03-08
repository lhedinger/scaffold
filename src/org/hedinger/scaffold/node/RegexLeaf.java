package org.hedinger.scaffold.node;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class RegexLeaf extends LeafNode
{

    public RegexLeaf(String value)
    {
        super(value);
    }

    @Override
    public StringBounds matches(SmartBuffer body, int offset) throws Exception
    {
        body.skip(offset);

        return body.firstMatch(value);
    }
    
}
