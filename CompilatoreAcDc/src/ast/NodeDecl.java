package ast;

import visitor.IVisitor;

public class NodeDecl extends NodeDecSt {
    private NodeId id;
    private LangType type;

    public NodeDecl(LangType type, String name) {
        super();
        this.type = type;
        id = new NodeId(name);
    }

    public LangType getType() {
        return type;
    }

    public NodeId getNodeId() {
        return id;
    }

    @Override
    public String toString() {
        return "[Decl:" + type.toString() + "," + id.toString() + "]" ;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}