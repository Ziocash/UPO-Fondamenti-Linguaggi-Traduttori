package ast;

import visitor.IVisitor;

public abstract class NodeAST {
    private TypeDescriptor resType;

    public TypeDescriptor getResType() {
        return resType;
    }

    public void setResType(TypeDescriptor resType) {
        this.resType = resType;
    }

    public abstract void accept(IVisitor visitor);
}