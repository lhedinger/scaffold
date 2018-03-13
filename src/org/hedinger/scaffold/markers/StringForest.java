package org.hedinger.scaffold.markers;

import java.util.Comparator;
import java.util.TreeSet;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;

public class StringForest {
    private static final int MAX_STEPS = 99999;
    private static final int MAX_TREES = 99999;

    private TreeSet<StringTree> trees = new TreeSet<StringTree>(new TreeComparator());
    private TreeSet<StringTree> finishedTrees = new TreeSet<StringTree>(new TreeComparator());

    private AbstractNode template;
    private SmartBuffer body;

    public StringForest(AbstractNode template, SmartBuffer body) {
        this.template = template;
        this.body = body;
    }

    public void run() throws Exception {

        StringTree initial = new StringTree(this);

        trees.add(initial);

        StringTree tree = initial;

        int c = 0;
        while (!tree.finished()) {

            int out = tree.grow();

            if (out == -1) {
                // TODO trees.remove(tree);
            } else {
                StringTree newTree = tree.deepClone();
                trees.add(newTree);
            }

            sortTree();
            tree = trees.first();

            if (c > MAX_STEPS) {
                throw new Exception("number of maximum grow steps exceeded");
            }
            if (trees.size() > MAX_TREES) {
                throw new Exception("number of maximum trees exceeded");
            }
        }
    }

    public void sortTree() {
        TreeSet<StringTree> newTrees = new TreeSet<StringTree>(new TreeComparator());
        for (StringTree tree : trees) {
            if (tree.finished()) {
                finishedTrees.add(tree);
            } else {
                newTrees.add(tree);
            }
        }
        trees = newTrees;
    }

    public SmartBuffer getBody() {
        return body;
    }

    public AbstractNode getTemplate() {
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
                return 1;
            }
            if (o1.getCoverage() < o2.getCoverage()) {
                return -1;
            }
            return 1;
        }

    }

}
