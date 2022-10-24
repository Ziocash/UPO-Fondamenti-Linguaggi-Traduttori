package visitor;

import ast.NodeAST;
import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeConst;
import ast.NodeConvert;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import symboltable.SymbolTable;

/**
 * Represents a visitor that builds dc (desktop caluclator) language program
 * code.
 */
public class CodeGeneratorVisitor implements IVisitor {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private StringBuilder code;
    private static char[] register = ALPHABET.toCharArray();
    private static int registerIndex = 0;

    /**
     * Class constructor.
     */
    public CodeGeneratorVisitor() {
        code = new StringBuilder();
    }

    /**
     * <p>
     * NodeProgram visitor.
     * </p>
     * 
     * <p>
     * Visits the whole AST and creates code.
     * </p>
     * 
     * @param node The node containing the whole AST.
     */
    @Override
    public void visit(NodeProgram node) {
        SymbolTable.init();
        for (NodeAST nodeAST : node)
            nodeAST.accept(this);
    }

    /**
     * <p>
     * Gets a char to identify the register associated to a variable.
     * </p>
     * 
     * <p>
     * Increments automatically the register index.
     * </p>
     * 
     * @return a char that identifies the register.
     */
    private static char newRegister() {
        return register[registerIndex++];
    }

    /**
     * Inserts a space in the code stringbuilder.
     */
    private void insertSpace() {
        code.append(" ");
    }

    /**
     * Returns the trimmed code.
     * 
     * @return the trimmed code.
     */
    public String getCode() {
        return code.toString().trim();
    }

    /**
     * Visits a {@link NodeId} node.
     * 
     * @param node The node representing the variable name.
     */
    @Override
    public void visit(NodeId node) {
        // Empty method
    }

    /**
     * <p>
     * Visits a {@link NodeDecl} node.
     * </p>
     * 
     * <p>
     * Sets the register for the corresponding {@link NodeId} node.
     * </p>
     * 
     * @param node The node representing the declaration.
     */
    @Override
    public void visit(NodeDecl node) {
        node.getNodeId().getDefinition().setRegister(newRegister());
    }

    /**
     * <p>
     * Visits a {@link NodeBinOp} node.
     * </p>
     * 
     * <p>
     * Triggers the visit to both expressions parts and then appends the operation
     * sign. Adds the operation to code stringbuilder (e.g.: {@code 4 4 + 5 +}).
     * </p>
     * 
     * @param node The node representing a binary operation.
     */
    @Override
    public void visit(NodeBinOp node) {
        node.getLeftOp().accept(this);
        node.getRightOp().accept(this);
        switch (node.getOp()) {
            case DIV:
                code.append("/ ");
                break;
            case MINUS:
                code.append("- ");
                break;
            case PLUS:
                code.append("+ ");
                break;
            case TIMES:
                code.append("* ");
                break;
            default:
                break;

        }
    }

    /**
     * <p>
     * Visits a {@link NodeDeref} node.
     * </p>
     * 
     * <p>
     * Gets the register associated with the variable. Appends it in code
     * stringbuilder.
     * </p>
     * 
     * @param node The node representing a dereferencing (e.g.: {@code b = b + 1}.
     *             In this case {@code b} is {@link NodeAssign} and then a
     *             {@link NodeDeref}).
     */
    @Override
    public void visit(NodeDeref node) {
        char reg = node.getId().getDefinition().getRegister();
        code.append("l" + reg);
        insertSpace();
    }

    /**
     * <p>
     * Visits a {@link NodeConst} node.
     * </p>
     * 
     * <p>
     * Gets the node value and appends it to code stringbuilder.
     * Appends a trailing space.
     * </p>
     * 
     * @param node The node representing a constant (e.g.: {@code 4} in source
     *             code).
     */
    @Override
    public void visit(NodeConst node) {
        code.append(node.getValue());
        insertSpace();
    }

    /**
     * <p>
     * Visits a {@code NodeAssign} node.
     * </p>
     * 
     * <p>
     * Gets the variable assigned register, appends the code to stringbuilder, a
     * trailing space and
     * then sets precision to 0 decimals ({@code int} type).
     * </p>
     * 
     * @param node The node representing the assignment (e.g.: {@code a = 4}).
     */
    @Override
    public void visit(NodeAssign node) {
        char assignedRegister = node.getId().getDefinition().getRegister();
        node.getExpr().accept(this);
        code.append("s").append(assignedRegister);
        insertSpace();
        code.append("0 k ");
    }

    /**
     * <p>
     * Visits a {@link NodePrint} node.
     * </p>
     * 
     * <p>
     * Gets the variable assigned register and appends the dc code to code
     * stringbuilder.
     * </p>
     * 
     * @param node The node representing the print statement.
     */
    @Override
    public void visit(NodePrint node) {
        char reg = node.getId().getDefinition().getRegister();
        code.append("l" + reg + " p P ");
    }

    /**
     * <p>
     * Visits a {@link NodeConvert} node.
     * </p>
     * 
     * <p>
     * Visits the expression and appends to code stringbuilder the 5 decimal
     * precision ({@code float} type)
     * </p>
     * 
     * @param node The node representing the conversion statement (e.g.: assigning a
     *             {@code int} value to a {@code float} variable).
     */
    @Override
    public void visit(NodeConvert node) {
        node.getExpr().accept(this);
        code.append("5 k ");
    }

}
