package org.hedinger.scaffold.node;

public class NewlineLeaf extends RegexLeaf {
    public NewlineLeaf() {
        super("[ \t\r\n]*");
    }
}
