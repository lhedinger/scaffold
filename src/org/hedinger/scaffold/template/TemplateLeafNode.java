package org.hedinger.scaffold.template;

import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public abstract class TemplateLeafNode extends TemplateNode {
	public static final String VALUE_NULL = "NULL";

	protected final String value;

	public TemplateLeafNode(String value) {
		super("");
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
	public boolean isStrict() {
		return parent.isStrict();
	}

	@Override
	public String toString() {
		return super.toString() + " value='" + value + "'";
	}
}
