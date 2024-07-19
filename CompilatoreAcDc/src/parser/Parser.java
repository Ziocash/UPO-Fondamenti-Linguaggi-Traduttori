package parser;

import java.io.IOException;
import java.util.ArrayList;

import ast.LangOper;
import ast.LangType;
import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeConst;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeExpr;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import ast.NodeStm;
import exception.LexicalException;
import exception.SyntacticException;
import scanner.Scanner;
import token.Token;
import token.TokenType;

public class Parser {

    private Scanner scanner;

    private String scanErrorMessage = "Something went wrong during scan";

    /**
     * Parser constructor
     * 
     * @param scanner Scanner instance
     */
    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Parse the scanned document
     * 
     * @return NodeProgram that represents the whole program parsed
     * @throws SyntacticException
     */
    public NodeProgram parse() throws SyntacticException {
        return parsePrg();
    }

    /**
     * Parse all scanned tokens through building the AST
     * 
     * @return NodeProgram that represents the whole program parsed
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            is not a start token
     */
    private NodeProgram parsePrg() throws SyntacticException {
        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (tk.getType()) {
            case TYINT:
            case TYFLOAT:
            case ID:
            case PRINT:
            case EOF:
                ArrayList<NodeDecSt> retNodeDecSt = parseDSs();
                match(TokenType.EOF);
                return new NodeProgram(retNodeDecSt);
            default:
                throw new SyntacticException(
                        "Token \'" + tk.getType() + "\' at line " + tk.getRow() + " is not a program start");
        }
    }

    /**
     * Parse declarations and statements
     * 
     * @return an {@code ArrayList<NodeDecSt>} containing all tokens
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            is not a start token
     */
    private ArrayList<NodeDecSt> parseDSs() throws SyntacticException {

        Token tk;
        try {
            tk = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (tk.getType()) {

            case TYINT:
            case TYFLOAT: // DSs -> Dcl DSs
                NodeDecl dec = parseDcl();
                ArrayList<NodeDecSt> retList = parseDSs();
                retList.add(0, dec);
                return retList;
            case ID:
            case PRINT: // DSs -> Stm DSs
                NodeStm stm = parseStm();
                ArrayList<NodeDecSt> retList1 = parseDSs();
                retList1.add(0, stm);
                return retList1;
            case EOF:
                return new ArrayList<>();
            default:
                throw new SyntacticException(
                        "Token \'" + tk.getType() + "\' at line " + tk.getRow() + " is not a program start");
        }
    }

    /**
     * Parse declaration
     * 
     * @return AST node representing declaration
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     */
    private NodeDecl parseDcl() throws SyntacticException {
        Token token;
        try {
            token = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case TYFLOAT:
                String name = match(TokenType.TYFLOAT).getValue();
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodeDecl(LangType.FLOAT, name);
            case TYINT:
                name = match(TokenType.TYINT).getValue();
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodeDecl(LangType.INT, name);
            default:
                throw new SyntacticException(
                        String.format("Unexpected token \'%s\' at line %d", token.getValue(), token.getRow()));

        }

    }

    /**
     * Parse statement
     * 
     * @return NodeStm that represents a statement
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     */
    private NodeStm parseStm() throws SyntacticException {
        Token token;
        try {
            token = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case ID:
                match(TokenType.ID);
                match(TokenType.ASSIGN);
                NodeExpr expr = parseExp();
                match(TokenType.SEMI);
                return new NodeAssign(new NodeId(token.getValue()), expr);
            case PRINT:
                Token tk = match(TokenType.PRINT);
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodePrint(new NodeId(tk.getValue()));
            default:
                String string = String.format("Unexpected token \'%s\' at line %d", token.getType(), token.getRow());
                throw new SyntacticException(string);
        }
    }

    /**
     * Parse expression for Exp non-terminal
     * 
     * @return NodeExpr representing the visited expression
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     */
    private NodeExpr parseExp() throws SyntacticException {
        Token token = null;
        try {
            token = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case INT:
            case FLOAT:
            case ID:
                NodeExpr ter = parseTr();
                NodeExpr exp = parseExpP(ter);
                return exp;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * Parse expression (PLUS and MINUS)
     * 
     * @param leftOp Left operation to keep track of
     * @return NodeExpr representing the current expression
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     */
    private NodeExpr parseExpP(NodeExpr leftOp) throws SyntacticException {
        Token token = null;
        try {
            token = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case PLUS:
                match(TokenType.PLUS);
                NodeExpr terP = parseTr();
                NodeExpr opP = new NodeBinOp(leftOp, terP, LangOper.PLUS);
                NodeExpr expP = parseExpP(opP);
                return expP;
            case MINUS:
                match(TokenType.MINUS);
                NodeExpr terM = parseTr();
                NodeExpr opM = new NodeBinOp(leftOp, terM, LangOper.MINUS);
                NodeExpr expM = parseExpP(opM);
                return expM;
            case SEMI:
                return leftOp;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * Parsing Tr non-terminal representing DIVIDE or TIMES operations (higher precedence)
     * 
     * @return an object representing complete expression tree
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     * 
     * @see TokenType
     */
    private NodeExpr parseTr() throws SyntacticException {
        Token token = null;
        try {
            token = scanner.peekToken();
        } catch (LexicalException | IOException e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case INT:
            case FLOAT:
            case ID:
                NodeExpr left = parseVal();
                NodeExpr expr = parseTrP(left);
                return expr;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * Parsing TrP non-terminal 
     * 
     * @param leftOp Left operation to keep track of
     * @return NodeExpr representing a complete AST for the given expression
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     */
    private NodeExpr parseTrP(NodeExpr leftOp) throws SyntacticException {
        Token token = null;
        try {
            token = scanner.peekToken();
        } catch (LexicalException | IOException e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case TIMES:
                match(TokenType.TIMES);
                NodeExpr valT = parseVal();
                NodeExpr opT = new NodeBinOp(leftOp, valT, LangOper.TIMES);
                NodeExpr expT = parseTrP(opT);
                return expT;
            case DIV:
                match(TokenType.DIV);
                NodeExpr valD = parseVal();
                NodeExpr opD = new NodeBinOp(leftOp, valD, LangOper.DIV);
                NodeExpr expD = parseTrP(opD);
                return expD;
            case PLUS:
            case MINUS:
            case SEMI:
                return leftOp;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * Parse Val 
     * 
     * @return an object representing a constant value or a dereferenced variable value
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            does not respect syntax
     */
    private NodeExpr parseVal() throws SyntacticException {
        Token token = null;
        try {
            token = scanner.peekToken();
        } catch (LexicalException | IOException e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        switch (token.getType()) {
            case INT:
                match(TokenType.INT);
                return new NodeConst(token.getValue(), LangType.INT);
            case FLOAT:
                match(TokenType.FLOAT);
                return new NodeConst(token.getValue(), LangType.FLOAT);
            case ID:
                match(TokenType.ID);
                return new NodeDeref(new NodeId(token.getValue()));
            default:
                throw new SyntacticException("");
        }

    }

    /**
     * Matching method
     * 
     * @param type Type you need to match with
     * @return Next scanned token
     * @throws SyntacticException Exception thrown when expected {@code TokenType}
     *                            is not equal to actual token type
     */
    private Token match(TokenType type) throws SyntacticException {
        Token token;
        try {
            token = scanner.peekToken();
        } catch (Exception e) {
            throw new SyntacticException(scanErrorMessage, e);
        }
        if (type.equals(token.getType()))
            try {
                return scanner.nextToken();
            } catch (Exception e) {
                throw new SyntacticException(scanErrorMessage, e);
            }
        String string = String.format("Expected token \'%s\' but was \'%s\' at line %d", type, token.getType(),
                token.getRow());
        throw new SyntacticException(string);
    }

}
