package ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import visitor.IVisitor;

public class NodeProgram extends NodeAST implements Iterable<NodeDecSt> {

    private ArrayList<NodeDecSt> decSts;
    
    public NodeProgram(List<NodeDecSt> decSts) {
        super();
        this.decSts = new ArrayList<>(decSts);
    }

    @Override
    public Iterator<NodeDecSt> iterator() {
       return decSts.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(NodeAST node : decSts)
        {
            builder.append(node.toString());
        }
        return "[Program:" + builder.toString() + "]";
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

}
