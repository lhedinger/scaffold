package org.hedinger.scaffold.markers;

import java.util.Comparator;
import java.util.TreeSet;

import org.hedinger.scaffold.template.TemplateTree;
import org.hedinger.scaffold.utils.SmartBuffer;

public class StringForest {
	private static final int MAX_STEPS = 99999;
	private static final int MAX_TREES = 99999;

	private TreeSet<StringTree> trees = new TreeSet<StringTree>(new TreeComparator());
	private TreeSet<StringTree> finishedTrees = new TreeSet<StringTree>(new TreeComparator());

	private TemplateTree template;
	private SmartBuffer body;

	public StringForest(TemplateTree template, SmartBuffer body) {
		this.template = template;
		this.body = body;
	}

	public void run() throws Exception {

		StringTree initial = new StringTree(this);

		trees.add(initial);

		StringTree tree = initial;

		int c = 0;
		while (!tree.finished()) {

			Status out = tree.grow();

			if (out == Status.FORK) {
				StringTree newTree = tree.deepClone();
				trees.add(newTree);
			}

			sortTree();

			if (trees.size() == 0) {
				return;
			}
			tree = trees.first();

			if (c > MAX_STEPS) {
				throw new Exception("number of maximum grow steps exceeded");
			}
			if (trees.size() > MAX_TREES) {
				throw new Exception("number of maximum trees exceeded");
			}
		}
	}

	public StringTree getBest() {
		if (finishedTrees.isEmpty()) {
			return null;
		}
		return finishedTrees.first();
	}

	public void sortTree() {
		TreeSet<StringTree> newTrees = new TreeSet<StringTree>(new TreeComparator());
		for (StringTree tree : trees) {
			if (tree.finished()) {
				finishedTrees.add(tree);
			} else if (!tree.failed()) {
				newTrees.add(tree);
			}
			if (tree.failed()) {
				// System.out.println("\n\nFAILED" + tree.toString());
			}

		}
		trees = newTrees;
	}

	public SmartBuffer getBody() {
		return body;
	}

	public TemplateTree getTemplate() {
		return template;
	}

	private static class TreeComparator implements Comparator<StringTree> {

		@Override
		public int compare(StringTree o1, StringTree o2) {
			if (o1.getCoverage() < 0) {
				return 1; // move to end
			}
			if (o2.getCoverage() < 0) {
				return -1; // move to end
			}
			if (o1.getCoverage() > o2.getCoverage()) {
				return -1;
			}
			if (o1.getCoverage() < o2.getCoverage()) {
				return 1;
			}
			return 1;
		}

	}

}
