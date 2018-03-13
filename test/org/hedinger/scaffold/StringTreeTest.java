package org.hedinger.scaffold;

import org.hedinger.scaffold.markers.StringForest;
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
    public void testTree_basic() throws Exception {

        StringForest tree = new StringForest(template, new SmartBuffer("xAxBCZx"));
        tree.run();
    }

    @Test
    public void testTree_optionals() throws Exception {

        StringForest tree = new StringForest(template, new SmartBuffer("AxBCZABxBCBZZCZx"));
        tree.run();
    }

    @Test
    public void testTree_repeating() throws Exception {

        StringForest tree = new StringForest(template, new SmartBuffer("AxBCZABxBCBZZCZx"));
        tree.run();
    }

    @Test
    public void testTree_noise() throws Exception {

        StringForest tree = new StringForest(template, new SmartBuffer("AxBCZABxBCBZZCZx"));
        tree.run();
    }
    
    @Test
    public void testForest_Basic() throws Exception {

        StringForest tree = new StringForest(template, new SmartBuffer("ABCZABBCBZ"));
        tree.run();
    }

    private static void print(String output, float coverage) {
        System.out.println(output + "  " + String.valueOf(coverage * 100) + "%");
    }

}
