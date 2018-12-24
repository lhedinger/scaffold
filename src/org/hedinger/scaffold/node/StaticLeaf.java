package org.hedinger.scaffold.node;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class StaticLeaf extends LeafNode {

	private final int length;

	public StaticLeaf(String value) {
		super(value);
		length = value.length();
	}

	@Override
	public StringBounds matches(SmartBuffer body, StringBounds allowedRange) throws Exception {

		int i = body.indexOf(value, allowedRange.start);

		if (i == -1) {
			return null;
		}

		StringBounds output = new StringBounds(i, i + length);

		if (!output.subsetOf(allowedRange)) {
			return null;
		}

		return output;
	}
}
