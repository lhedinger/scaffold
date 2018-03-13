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
        signal = 0;

        if (out < 0) {
            coverage = -1; // failed
        }
        if (out > 0) {
            finished = true;
        }
        return out;
    }

    public StringTree deepClone() {
        StringNode clonedRoot = root.deepClone();
        StringTree tree = new StringTree(forest, clonedRoot);
        tree.signal = 1;
        return tree;
    }

    public boolean finished() {
        return finished;
    }

    public String getOutput() {
        return root.generateOutput();
    }

    public int getCoverage() {
        return coverage;
    }

    @Override
    public String toString() {
        return "\n(" + coverage + ")   " + TreePrinter.print(root);
    }

}
