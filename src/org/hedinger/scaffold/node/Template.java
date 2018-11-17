package org.hedinger.scaffold.node;

import org.hedinger.scaffold.utils.TreePrinter;

public class Template {
    private AbstractNode root;

    public Template() {
    }

    public AbstractNode getRoot() {
        return root;
    }

    public void setRoot(AbstractNode root) {
        this.root = root;
    }

    public int getScore() {
        return 0;
    }

    @Override
    public String toString() {
        return TreePrinter.print(root);
    }

}
