package ast;

import visitor.IVisitor;

public class NodePrint extends NodeStm {
    private NodeId id;

    public NodePrint(NodeId id) {
        super();
        this.id = id;
    }

    public NodeId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[Print:" + id.toString() + "]"; 
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}