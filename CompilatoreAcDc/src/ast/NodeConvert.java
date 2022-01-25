package ast;

import visitor.IVisitor;

public class NodeConvert extends NodeExpr {
    private NodeExpr expr;
    
    public NodeConvert(NodeExpr expr){
        this.expr = expr;
    }

    public NodeExpr getExpr() {
        return expr;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
