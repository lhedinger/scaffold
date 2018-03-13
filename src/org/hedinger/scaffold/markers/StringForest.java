package org.hedinger.scaffold.markers;

import java.util.Comparator;
import java.util.TreeSet;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;

public class StringForest {
    private static final int MAX_STEPS = 99999;
    private static final int MAX_TREES = 99999;

    private TreeSet<StringTree> trees = new TreeSet<StringTree>(new Comparator<StringTree>() {
        @Override
        public int compare(StringTree o1, StringTree o2) {
            if (o1.getCoverage() < 0) {
                return 1;
            }
            if (o2.getCoverage() < 0) {
                return -1;
            }
            if (o1.getCoverage() > o2.getCoverage()) {
                return 1;
            }
            if (o1.getCoverage() < o2.getCoverage()) {
                return -1;
            }
            return 1;
        }
    });

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

            trees.remove(tree);
            trees.add(tree);
            
            if (out == -1) {
                // TODO trees.remove(tree);
            } else {
                StringTree newTree = tree.deepClone();
                trees.add(newTree);
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

    public SmartBuffer getBody() {
        return body;
    }

    public AbstractNode getTemplate() {
        return template;
    }

}
