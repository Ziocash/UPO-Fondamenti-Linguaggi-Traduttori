package visitor;

import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeConst;
import ast.NodeConvert;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeExpr;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import ast.TypeDescriptor;
import symboltable.Attributes;
import symboltable.SymbolTable;

/**
 * <p>
 * Visitor class based on {@link IVisitor} interface.
 * </p>
 */
public class TypeCheckingVisitor implements IVisitor {

    /**
     * <p>
     * Internal logger based on {@link StringBuilder} class.
     * </p>
     * 
     * <p>
     * Used to log all errors found during visit.
     * </p>
     */
    private StringBuilder logger = new StringBuilder();

    /**
     * Returns {@code true} if the visitor found errors in visited AST.
     * 
     * @return {@code true} if has errors in AST, otherwise {@code false}.
     */
    public boolean hasErrors() {
        return !logger.toString().isEmpty();
    }

    /**
     * Returns a string representing all errors found in the AST.
     * 
     * @return a string representing all errors found in the AST.
     */
    public String getLoggerString() {
        return logger.toString();
    }

    /**
     * <p>
     * NodeProgram visitor.
     * </p>
     * 
     * <p>
     * Visits all the abstract syntax tree (AST) nodes.
     * </p>
     * 
     * @param node the whole program node.
     */
    @Override
    public void visit(NodeProgram node) {
        SymbolTable.init();
        for (NodeDecSt nodeDecSt : node) {
            nodeDecSt.accept(this);
        }
        if (hasErrors())
            node.setResType(TypeDescriptor.ERROR);
        else
            node.setResType(TypeDescriptor.VOID);
    }

    /**
     * <p>
     * NodeProgram visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodeId} node and checks if it already declared in
     * {@link SymbolTable}.
     * </p>
     * <p>
     * Applies a {@link TypeDescriptor} to the node.
     * </p>
     * 
     * @param node the variable id node.
     * 
     */
    @Override
    public void visit(NodeId node) {
        if (SymbolTable.lookup(node.getName()) != null) {
            Attributes attr = SymbolTable.lookup(node.getName());
            node.setResType(TypeDescriptor.valueOf(attr.getType().toString()));
            node.setDefinition(attr);
        } else {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format("Variable: variable \'%s\' is not declared.%n", node.getName()));
        }
    }

    /**
     * <p>
     * NodeDecl visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodeDecl} node and checks if it's declared in
     * {@link SymbolTable}.
     * </p>
     * <p>
     * If it's already declared sets the node descriptor to {@code ERROR}, otherwise
     * adds it to {@link SymbolTable} and visits the node.
     * </p>
     * 
     * @param node the declaration node.
     */
    @Override
    public void visit(NodeDecl node) {
        if (SymbolTable.lookup(node.getNodeId().getName()) != null) {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(
                    String.format("Declaration: variable \'%s\' already declared.%n", node.getNodeId().getName()));
        } else {
            Attributes attr = new Attributes(node.getType());
            SymbolTable.enter(node.getNodeId().getName(), attr);
            node.getNodeId().accept(this);
        }
    }

    /**
     * <p>
     * NodeBinOp visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodeBinOp} node, visits both expressions and check if types
     * are correct.
     * </p>
     * <p>
     * Visit checks also if expressions contain FLOAT -> INT operations.
     * </p>
     * 
     * @param node the binary operation node.
     */
    @Override
    public void visit(NodeBinOp node) {
        node.getLeftOp().accept(this);
        node.getRightOp().accept(this);
        if (node.getLeftOp().getResType() == TypeDescriptor.ERROR
                || node.getRightOp().getResType() == TypeDescriptor.ERROR) {
            node.setResType(TypeDescriptor.ERROR);
        } else if (node.getLeftOp().getResType() == node.getRightOp().getResType()) {
            node.setResType(node.getLeftOp().getResType());
        } else if (isCompatible(node.getLeftOp().getResType(), node.getRightOp().getResType())) {
            NodeExpr rightOp = convert(node.getRightOp());
            node.setRightOp(rightOp);
            node.setResType(node.getLeftOp().getResType());
        } else {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format(
                    "BinaryOperation: Expected type FLOAT and INT for expressions, but was \'%s\' and \'%s\'.%n",
                    node.getLeftOp().getResType(), node.getRightOp().getResType()));
        }
    }

    /**
     * <p>
     * NodeDeref visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodeId} node and sets node {@link TypeDescriptor} to the id
     * type.
     * </p>
     * 
     * @param node the dereferencing node.
     */
    @Override
    public void visit(NodeDeref node) {
        node.getId().accept(this);
        node.setResType(node.getId().getResType());
    }

    /**
     * <p>
     * NodeConst visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodeConst} node and sets node {@link TypeDescriptor} to match
     * constant type.
     * </p>
     * 
     * @param node the constant node.
     */
    @Override
    public void visit(NodeConst node) {
        var type = TypeDescriptor.valueOf(node.getType().toString());
        if (type != null)
            node.setResType(type);
        else {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format("Constant: Unexpected type \'%s\' for constant value \'%s\'", node.getType(),
                    node.getValue()));
        }

    }

    /**
     * <p>
     * NodeAssign visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodeAssign} node and sets node {@link TypeDescriptor} to
     * match
     * variable type.
     * </p>
     * 
     * @param node the assignment node.
     */
    @Override
    public void visit(NodeAssign node) {
        node.getId().accept(this);
        node.getExpr().accept(this);
        if (node.getExpr().getResType() == TypeDescriptor.ERROR
                || !isCompatible(node.getId().getResType(), node.getExpr().getResType())) {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format("Assignment: Cannot assign \'%s\' type to \'%s\' type.%n",
                    node.getExpr().getResType(), node.getId().getResType()));
        } else {
            NodeExpr n = convert(node.getExpr());
            node.setExpr(n);
            node.setResType(n.getResType());
        }
    }

    /**
     * <p>
     * Empty method due to automated conversion.
     * </p>
     * 
     * <p>
     * This body empty just to mark node as visited.
     * </p>
     * 
     * @param node the converted node.
     */
    @Override
    public void visit(NodeConvert node) {
        // Empty method
    }

    /**
     * <p>
     * NodePrint visitor.
     * </p>
     * 
     * <p>
     * Visits a {@link NodePrint} node and sets node {@link TypeDescriptor} to match
     * variable type.
     * </p>
     * 
     * @param node the assignment node.
     */
    @Override
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (node.getId().getResType() != TypeDescriptor.ERROR && SymbolTable.lookup(node.getId().getName()) != null) {
            node.setResType(node.getId().getResType());
        } else
            node.setResType(TypeDescriptor.ERROR);
    }

    /**
     * <p>
     * Checks if two {@link TypeDescriptor}s are compatible.
     * </p>
     * <p>
     * Return {@code true} if types are compatible, otherwise {@code false}.
     * </p>
     * 
     * @param t1 the variable type that will receive expression value.
     * @param t2 the expression type that will be assigned to variable.
     * @return {@code true} if types are compatible, otherwise {@code false}.
     */
    private boolean isCompatible(TypeDescriptor t1, TypeDescriptor t2) {
        return ((t1.equals(TypeDescriptor.FLOAT) && t2.equals(TypeDescriptor.INT)) || t1.equals(t2))
                && t1 != TypeDescriptor.ERROR && t2 != TypeDescriptor.ERROR;
    }

    /**
     * <p>
     * Converts an {@code INT} expression to a {@code FLOAT} one.
     * </p>
     * <p>
     * Returns the converted node as {@link NodeExpr}.
     * </p>
     * 
     * @param node
     * @return the converted node as {@link NodeExpr}.
     */
    private NodeExpr convert(NodeExpr node) {
        if (node.getResType().equals(TypeDescriptor.FLOAT))
            return node;
        else {
            NodeConvert nodeConvert = new NodeConvert(node);
            nodeConvert.setResType(TypeDescriptor.FLOAT);
            return nodeConvert;
        }
    }
}
