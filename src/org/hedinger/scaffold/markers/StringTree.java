package org.hedinger.scaffold.markers;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.TreePrinter;

public class StringTree {
    private StringNode root;

    private SmartBuffer body;

    public StringTree(AbstractNode template, SmartBuffer body) {
        this.root = new StringBranch(template, body);
        this.body = body;
    }

    public void run() throws Exception {
        while (!root.isDone()) {
            root.growOne();
            System.out.println(TreePrinter.print(root));
        }
    }

    public String getOutput() {
        return root.generateOutput();
    }

    public float getCoverage() {
        float count = root.calcCoverage();
        count = count / body.length();
        return count;
    }
}
