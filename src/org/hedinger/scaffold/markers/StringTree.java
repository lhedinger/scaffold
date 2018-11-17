package org.hedinger.scaffold.markers;

import java.util.ArrayList;
import java.util.List;

import org.hedinger.scaffold.utils.TreePrinter;

public class StringTree {

	private static int INCREMENTER = 0;

	private StringForest forest;
	private StringNode root;
	private int coverage = 0;

	private int signal = 0;

	private boolean finished = false;

	private final int id;

	private String history = "";

	public StringTree(StringForest forest) {
		this.forest = forest;
		root = new StringBranch(forest.getTemplate().getRoot(), forest.getBody());
		this.id = INCREMENTER++;
		history += toString();
	}

	public StringTree(StringForest forest, StringNode root) {
		this.forest = forest;
		this.root = root;
		this.id = INCREMENTER++;
		history += toString();
	}

	public Status grow() throws Exception {
		Status out = root.grow(signal);
		coverage = root.calcCoverage();

		switch (out) {
		case FAILED:
			coverage = -1;
			break;
		case OPEN:
			break;
		case FORK:
			break;
		case CLOSED:
			finished = true;
			break;
		}
		if (root.isDone()) {
			finished = true;
		}

		history += toString();
		return out;
	}

	public StringTree deepClone() {
		StringNode clonedRoot = root.deepClone();
		StringTree tree = new StringTree(forest, clonedRoot);
		tree.coverage = coverage;
		tree.signal = 1;
		signal = 0;
		tree.history = history;
		return tree;
	}

	public String generateOutput() {
		return root.generateOutput();
	}

	public String generateAnnotatedOutput() {
		return root.generateAnnotatedOutput();
	}

	public List<String> find(String query) {
		List<String> result = new ArrayList<>();
		root.find(result, query);
		return result;
	}

	public boolean finished() {
		return finished;
	}

	public boolean failed() {
		return (coverage == -1);
	}

	public String getOutput() {
		return root.generateOutput();
	}

	public int getCoverage() {
		return coverage;
	}

	@Override
	public String toString() {
		return "\n(" + coverage + ")  s" + signal + "  id=" + id + " " + TreePrinter.print(root);
	}

}
