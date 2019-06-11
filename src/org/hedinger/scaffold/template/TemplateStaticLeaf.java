package org.hedinger.scaffold.template;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class TemplateStaticLeaf extends TemplateLeafNode {

	private final int length;

	public TemplateStaticLeaf(String value) {
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
