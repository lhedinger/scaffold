package org.hedinger.scaffold;

import org.hedinger.scaffold.markers.StringTree;
import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.TemplateParser;
import org.junit.Before;
import org.junit.Test;

public class StringTreeTest {

    private AbstractNode template;

    @Before
    public void setUp() throws Exception {
        TemplateParser parser = new TemplateParser();
        parser.parseTemplate("test/templ1");
        template = parser.getRoot();
        //System.out.println(TreePrinter.print(template));
    }

    @Test
    public void testSetup() {

    }

    @Test
    public void testBasic() throws Exception {

        StringTree tree = new StringTree(template, new SmartBuffer("xAxBCZx"));
        tree.run();
        String output = tree.getOutput();
        float coverage = tree.getCoverage();
        print(output, coverage);
    }

    @Test
    public void testWithOptionals() throws Exception {

        StringTree tree = new StringTree(template, new SmartBuffer("AxBCZABxBCBZZCZx"));
        tree.run();
        String output = tree.getOutput();
        float coverage = tree.getCoverage();
        print(output, coverage);
    }

    @Test
    public void testWithRepeating() throws Exception {

        StringTree tree = new StringTree(template, new SmartBuffer("AxBCZABxBCBZZCZx"));
        tree.run();
        String output = tree.getOutput();
        float coverage = tree.getCoverage();
        print(output, coverage);
    }

    @Test
    public void testWithNoise() throws Exception {

        StringTree tree = new StringTree(template, new SmartBuffer("AxBCZABxBCBZZCZx"));
        tree.run();
        String output = tree.getOutput();
        float coverage = tree.getCoverage();
        print(output, coverage);
    }

    private static void print(String output, float coverage) {
        System.out.println(output + "  " + String.valueOf(coverage * 100) + "%");
    }

}
