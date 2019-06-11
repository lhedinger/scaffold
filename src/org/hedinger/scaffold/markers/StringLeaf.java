package org.hedinger.scaffold.markers;

import static org.hedinger.scaffold.markers.Status.CLOSED;
import static org.hedinger.scaffold.markers.Status.FAILED;

import java.util.List;

import org.hedinger.scaffold.template.TemplateLeafNode;
import org.hedinger.scaffold.template.TemplateNode;
import org.hedinger.scaffold.utils.StringBounds;

public class StringLeaf extends StringNode {

	private TemplateLeafNode leafTemplate;
	private StringBounds matchedRange;

	private String foundValue;

	public StringLeaf(TemplateNode template, StringNode parent, int start, int end) {
		super(template, parent, start, end);
		leafTemplate = (TemplateLeafNode) template;
	}

	@Override
	public Status grow(int step) throws Exception {

		if (matchedRange != null) {
			throw new Exception("this leaf has already grown");
		}

		matchedRange = leafTemplate.matches(input, allowedRange);
		foundValue = input.substring(matchedRange);

		if (matchedRange == null) {
			return FAILED;
		}

		return CLOSED;
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
	public String generateAnnotatedOutput() {
		return foundValue;
	}

	@Override
	public void find(List<String> result, String query) {
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
		return "range:" + String.valueOf(matchedRange) + " max" + String.valueOf(allowedRange) + "  value:" + foundValue
				+ "    " + template.toString();
	}
}
