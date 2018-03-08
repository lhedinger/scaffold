package org.hedinger.scaffold;

import org.hedinger.scaffold.markers.MarkerTree;
import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.TemplateParser;
import org.hedinger.scaffold.utils.TreePrinter;
import org.junit.Before;
import org.junit.Test;

public class TemplateTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testBasic() throws Exception
    {
        TemplateParser parser = new TemplateParser();
        
        parser.parseTemplate("test/templ1");
        
        AbstractNode root = parser.getRoot();
        
        System.out.println(TreePrinter.print(root));
        
        MarkerTree tree = new MarkerTree(root, new SmartBuffer("test/templ1_input1"));
        
        //tree.run();
    }

}
