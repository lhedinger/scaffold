package org.hedinger.scaffold.markers;

import static org.hedinger.scaffold.markers.Status.CLOSED;
import static org.hedinger.scaffold.markers.Status.FAILED;
import static org.hedinger.scaffold.markers.Status.FORK;
import static org.hedinger.scaffold.markers.Status.OPEN;

import java.util.ArrayList;
import java.util.List;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.node.BranchNode;
import org.hedinger.scaffold.node.LeafNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.StringBounds;

public class StringBranch extends StringNode {

	private BranchNode branchTemplate;
	private ArrayList<StringNode> children = new ArrayList<>();

	private StringBounds maxRange;
	private StringBounds totalRange;

	private final boolean optional;
	private final int maxRepetitions;
	private final int modulo;

	private int repetition = 0;
	private int childIterator = -2;

	private boolean done = false;

	public StringBranch(AbstractNode template, SmartBuffer input) {
		super(template, input);
		branchTemplate = (BranchNode) template;
		optional = branchTemplate.isOptional();
		maxRepetitions = branchTemplate.getMaxRepetitions();
		modulo = branchTemplate.getChildren().size();
		maxRange = new StringBounds(0, input.length());
		totalRange = new StringBounds(0, 0);
	}

	public StringBranch(AbstractNode template, StringNode parent, int start, int end) {
		super(template, parent, start, end);
		branchTemplate = (BranchNode) template;
		optional = branchTemplate.isOptional();
		maxRepetitions = branchTemplate.getMaxRepetitions();
		modulo = branchTemplate.getChildren().size();
		maxRange = new StringBounds(start, end);
		totalRange = null;
	}

	@Override
	public Status grow(int signal) throws Exception {

		if (childIterator == -1) {
			if (optional || repetition > 0) {
				if (signal % 2 == 0) {
					childIterator = -9;
					return CLOSED;
				}
			}
			childIterator = 0;
		}
		if (childIterator == -2) {
			if (optional || repetition > 0) {
				childIterator = -1;
				return FORK;
			}
			childIterator = 0;
		}

		if (repetition >= maxRepetitions && childIterator == 0 && maxRepetitions >= 0) {
			return CLOSED;
		}

		StringNode currentChild = nextChild();

		Status status = currentChild.grow(signal);

		if (status == FAILED) {
			return FAILED;
		}

		updateRange(currentChild.getRange());

		if (status == OPEN || status == FORK) {
			return status;
		}

		if (status == CLOSED) {

			childIterator++;
			if (childIterator >= modulo) {
				childIterator = -2;
				repetition++;
			}
		}

		if (isStrict() && detectGaps()) {
			return FAILED;
		}

		if (repetition >= maxRepetitions && childIterator < 0 && maxRepetitions >= 0) {
			return CLOSED;
		}

		return OPEN;
	}

	private boolean detectGaps() {
		int i = -1;
		for (StringNode child : children) {
			if (i == -1) {
				i = child.getRange().end;
			} else if (i == child.getRange().start) {
				i = child.getRange().end;
			} else {
				return true;
			}
		}
		return false;
	}

	private void updateRange(StringBounds childRange) {
		if (childRange == null) {
			return;
		}
		if (totalRange == null) {
			totalRange = new StringBounds(childRange.start, childRange.end);
		} else {
			int start = totalRange.start;
			int end = totalRange.end;

			if (end < childRange.end) {
				end = childRange.end;
			}
			totalRange = new StringBounds(start, end);
		}
	}

	private StringNode nextChild() throws Exception {
		int absoluteIndex = childIterator + (repetition) * modulo;
		if (absoluteIndex < children.size()) {
			return children.get(absoluteIndex);
		}
		if (repetition >= maxRepetitions && maxRepetitions > 0) {
			return null;
		}

		StringNode nodeChild;
		AbstractNode templateChild = branchTemplate.getChildren().get(childIterator);
		if (templateChild instanceof BranchNode) {
			nodeChild = new StringBranch(templateChild, this, (totalRange == null ? maxRange.start : totalRange.end),
					maxRange.end);
		} else if (templateChild instanceof LeafNode) {
			nodeChild = new StringLeaf(templateChild, this, (totalRange == null ? maxRange.start : totalRange.end),
					maxRange.end);
		} else {
			throw new Exception("template is not valid type");
		}
		children.add(nodeChild);
		return nodeChild;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public List<StringNode> getChildren() {
		return children;
	}

	@Override
	public StringBounds getRange() {
		if (totalRange == null) {
			return new StringBounds(maxRange.start, maxRange.start);
		}
		return totalRange;
	}

	@Override
	public String generateOutput() {
		StringBuilder sb = new StringBuilder();
		for (StringNode child : children) {
			sb.append(child.generateOutput());
		}
		return sb.toString();
	}

	@Override
	public String generateAnnotatedOutput() {
		StringBuilder sb = new StringBuilder();
		for (StringNode child : children) {
			sb.append(" ");
			sb.append(branchTemplate.getName());
			sb.append("[");
			sb.append(child.generateAnnotatedOutput());
			sb.append(" ]");
		}
		return sb.toString();
	}

	@Override
	public void find(List<String> result, String query) {
		if (query.isEmpty()) {
			// result.add(this.generateOutput());
		}

		String[] path = query.split("\\.", 2);

		if (!template.getName().equals(path[0])) {
			return;
		}

		if (path.length == 1) {
			for (StringNode child : children) {
				result.add(child.generateOutput());
			}
			return;
		}

		for (StringNode child : children) {
			child.find(result, (path.length > 1 ? path[1] : ""));
		}

	}

	@Override
	public int calcCoverage() {
		int sum = 0;
		for (StringNode child : children) {
			sum += child.calcCoverage();
		}
		return sum;
	}

	@Override
	public StringNode deepClone() {
		StringBranch branch;

		if (parent == null) {
			branch = new StringBranch(template, input);
		} else {
			if (totalRange == null) {
				branch = new StringBranch(template, parent, maxRange.start, maxRange.end);
			} else {
				branch = new StringBranch(template, parent, totalRange.end, maxRange.end);
			}
		}
		branch.childIterator = this.childIterator;
		branch.done = this.done;
		branch.repetition = this.repetition;
		if (totalRange != null) {
			branch.totalRange = new StringBounds(totalRange.start, totalRange.end);
		}

		for (StringNode child : children) {
			branch.children.add(child.deepClone());
		}

		return branch;
	}

	@Override
	public String toString() {
		return "range:" + String.valueOf(totalRange) + " max:" + String.valueOf(maxRange) + "  " + template.toString()
		+ "\t\t" + (done ? "x" : "") + " ci=" + childIterator;
	}
}
