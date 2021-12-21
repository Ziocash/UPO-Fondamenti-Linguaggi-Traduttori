package parser;

import java.io.IOException;
import java.util.ArrayList;

import ast.LangOper;
import ast.LangType;
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
        }
        throw new SyntacticException(
                String.format("Unexpected token \'%s\' at line %d", token.getValue(), token.getRow()));
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
            case PRINT:
                Token tk = match(TokenType.PRINT);
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodePrint(new NodeId(tk.getValue()));
            case ID:
                match(TokenType.ID);
                token = match(TokenType.ASSIGN);
                if (token.getType() == TokenType.INT)
                    match(TokenType.INT);
                else
                    match(TokenType.FLOAT);
                return null;
            default:
                String string = String.format("Unexpected token \'%s\' at line %d", token.getType(), token.getRow());
                throw new SyntacticException(string);
        }
    }

    /**
     * 
     * @return
     * @throws SyntacticException
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
     * 
     * @param leftOp
     * @return
     * @throws SyntacticException
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
                NodeExpr expP = parseExpP(leftOp);
                return new NodeBinOp(terP, expP, LangOper.PLUS);
            case MINUS:
                match(TokenType.MINUS);
                NodeExpr terM = parseTr();
                NodeExpr expM = parseExpP(leftOp);
                return new NodeBinOp(terM, expM, LangOper.MINUS);
            case SEMI:
                return leftOp;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * 
     * @return
     * @throws SyntacticException
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
                parseTrP(left);
                return null;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * 
     * @param leftOp
     * @return
     * @throws SyntacticException
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
                NodeExpr left = parseVal();
                NodeExpr right = parseTrP(left);
                return new NodeBinOp(left, right, LangOper.TIMES);
            case FLOAT:
                match(TokenType.DIV);
                left = parseVal();
                right = parseTrP(left);
                return new NodeBinOp(left, right, LangOper.DIV);
            case PLUS:
            case MINUS:
            case SEMI:
                return leftOp;
            default:
                throw new SyntacticException("");
        }
    }

    /**
     * 
     * @return
     * @throws SyntacticException
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
                token = match(TokenType.INT);
                return new NodeConst(token.getValue(), LangType.INT);
            case FLOAT:
                match(TokenType.FLOAT);
                return new NodeConst(token.getValue(), LangType.FLOAT);
            case ID:
                Token temp = match(TokenType.ID);
                return new NodeDeref(new NodeId(temp.getValue()));
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
    Token match(TokenType type) throws SyntacticException {
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
