package ast;

import visitor.IVisitor;

public class NodeBinOp extends NodeExpr {

    private NodeExpr leftOp;
    private NodeExpr rightOp;
    private LangOper op;

    public NodeBinOp(NodeExpr leftOp, NodeExpr rightOp, LangOper op) {
        super();
        this.leftOp = leftOp;
        this.rightOp = rightOp;
        this.op = op;
    }

    public NodeExpr getLeftOp() {
        return leftOp;
    }

    public NodeExpr getRightOp() {
        return rightOp;
    }

    public LangOper getOp() {
        return op;
    }

    public void setLeftOp(NodeExpr leftOp) {
        this.leftOp = leftOp;
    }

    public void setRightOp(NodeExpr rightOp) {
        this.rightOp = rightOp;
    }

    @Override
    public String toString() {
        return "[BinOp:" + leftOp.toString() + "," +  op.toString() + "," + rightOp.toString() + "]";
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}