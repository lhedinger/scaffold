package org.hedinger.scaffold.markers;

import org.hedinger.scaffold.node.AbstractNode;
import org.hedinger.scaffold.utils.SmartBuffer;
import org.hedinger.scaffold.utils.TreePrinter;

public class MarkerTree
{
    private StringTree root;
    private StringTree current;

    private int offset = 0;

    private SmartBuffer body;

    public MarkerTree(AbstractNode template, SmartBuffer body)
    {
        this.root = new StringTree(template, body.length());
        this.current = this.root;
        this.body = body;
    }

    public void run() throws Exception
    {
        StringTree prev = null;
        boolean done = false;
        
        
        while(!done)
        {
            root.refreshTree();
            
            System.out.println(TreePrinter.print(root));
            
            if(current.isLeaf())
            {
                current.match(body, offset)
            } else {
                current.grow();
            }
            
            prev = current;
            current = root.calcNext();
            
            if(prev == current) throw new Exception("Stuck on: "+current.toString());
            
            done = root.isDone();
        }
        
        System.out.println("Coverage = "+root.calcCoverate()+"%");
    }
}
