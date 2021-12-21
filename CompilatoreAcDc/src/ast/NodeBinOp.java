package ast;

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

    @Override
    public String toString() {
        return "[BinOp:" + leftOp.toString() + "," +  op.toString() + "," + rightOp.toString() + "]";
    }
}