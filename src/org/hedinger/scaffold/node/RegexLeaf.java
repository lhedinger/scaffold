package org.hedinger.scaffold.node;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class RegexLeaf extends LeafNode {

	public RegexLeaf(String value) {
		super(value);
	}

	@Override
	public StringBounds matches(SmartBuffer body, StringBounds allowedRange) throws Exception {
		StringBounds output = body.firstMatch(value, allowedRange.start);

		if (!output.subsetOf(allowedRange)) {
			return null;
		}

		return output;
	}

}
