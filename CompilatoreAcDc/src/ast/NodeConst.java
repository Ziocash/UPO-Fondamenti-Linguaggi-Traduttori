package ast;

import visitor.IVisitor;

public class NodeConst extends NodeExpr {
    private String value;
    private LangType type;

    public NodeConst(String value, LangType type) {
        super();
        this.value = value;
        this.type = type;
    }

    public LangType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[Const:"+ type.toString() +"," + value + "]";
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
