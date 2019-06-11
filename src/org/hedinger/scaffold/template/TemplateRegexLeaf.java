package org.hedinger.scaffold.template;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class TemplateRegexLeaf extends TemplateLeafNode {

	public TemplateRegexLeaf(String value) {
		super(value);
	}

	@Override
	public StringBounds matches(SmartBuffer body, StringBounds allowedRange) throws Exception {
		StringBounds output = body.firstMatch(value, allowedRange.start);

		if (output == null || !output.subsetOf(allowedRange)) {
			return null;
		}

		return output;
	}

}
