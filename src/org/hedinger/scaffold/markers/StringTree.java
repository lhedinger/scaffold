package org.hedinger.scaffold.markers;

import org.hedinger.scaffold.utils.TreePrinter;

public class StringTree {

    private StringForest forest;
    private StringNode root;
    private int coverage = 0;

    private int signal = 0;

    private boolean finished = false;

    public StringTree(StringForest forest) {
        this.forest = forest;
        root = new StringBranch(forest.getTemplate(), forest.getBody());
    }

    public StringTree(StringForest forest, StringNode root) {
        this.forest = forest;
        this.root = root;
    }

    public int grow() throws Exception {
        int out = root.grow(signal);
        coverage = root.calcCoverage();

        if (out < 0) {
            coverage = -1; // failed
        }
        if (out > 0 || root.isDone()) {
            finished = true;
        }
        return out;
    }

    public StringTree deepClone() {
        StringNode clonedRoot = root.deepClone();
        StringTree tree = new StringTree(forest, clonedRoot);
        tree.coverage = coverage;
        tree.signal = 1;
        signal = 0;
        return tree;
    }

    public boolean finished() {
        return root.isDone();
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
        return "\n(" + coverage + ")  s" + signal + "  " + TreePrinter.print(root);
    }

}
