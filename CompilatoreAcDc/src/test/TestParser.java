package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import ast.LangType;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import exception.SyntacticException;
import parser.Parser;
import scanner.Scanner;
import token.Token;
import token.TokenType;

public class TestParser {

    /**
     * <p>Parse declaration test.</p>
     * 
     * <p>Throws an exception when {@code parser.parse()} throws an exception.</p>
     * <p>Tests if iterator and declarations in file are correctly displayed.</p>
     * 
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testDecParse() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testDec.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        NodeDecSt node = new NodeDecl(LangType.INT, "a");
        Iterator<NodeDecSt> iterator = program.iterator();
        assertEquals(node.toString(), iterator.next().toString());
        assertEquals(new NodeDecl(LangType.FLOAT, "b").toString(), iterator.next().toString());
        assertEquals(new NodePrint(new NodeId("a")).toString(), iterator.next().toString());
    }

    /**
     * <p>
     * Throws an exception on {@code parser.parse()} due to {@code int float;}
     * statement in file.
     * </p>
     * 
     * <p>
     * Statement {@code int float;} is not allowed due to an ID cannot be called as
     * a type.
     * </p>
     * 
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testDSsDclStm() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testDSsDclStm.txt");
        Parser parser = new Parser(scanner);
        assertThrows(SyntacticException.class, parser::parse);
    }

    /**
     * Just a quick test to see if {@code ArrayList} inserts elements on head.
     */
    @Test
    public void testAddOnHead() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(0, new Token(TokenType.EOF, 2));
        assertTrue(TokenType.EOF == tokens.get(0).getType());

        tokens.add(0, new Token(TokenType.INT, 1, "temp"));
        assertTrue(TokenType.INT == tokens.get(0).getType());
        assertEquals("temp", tokens.get(0).getValue());
    }

    /**
     * Quick test on {@code toString()} methods.
     * 
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testToString() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testDec.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        assertEquals("[Program:[Decl:INT,[Id:a]][Decl:FLOAT,[Id:b]][Print:[Id:a]]]", program.toString());
    }

    /**
     * <p>A complete parser test.</p>
     * 
     * <p>Tests if a correct program is correctly parsed in an AST.</p>
     * 
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testComplete() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\fileParserCorrect2.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        program.toString();
    }

    /**
     * Associativity test.
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testAssociativity() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testAssociativity.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        program.toString();
    }

    /**
     * Associativity test.
     * 
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testAssociativityPlus() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testAssociativityPlus.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        assertEquals("[Program:[Decl:INT,[Id:b]][Assign:[Id:b],[BinOp:[BinOp:[Const:INT,3],PLUS,[Const:INT,2]],PLUS,[Const:INT,7]]][Print:[Id:b]]]", program.toString());
    }

    /**
     * Associativity test.
     * 
     * @throws FileNotFoundException If the named file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     */
    @Test
    public void testAssociativityAllSigns() throws FileNotFoundException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testAssociativityAllSigns.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        //Checks if associativity prints (3 - (2 * 4)) - 7
        assertEquals("[Program:[Decl:INT,[Id:b]][Assign:[Id:b],[BinOp:[BinOp:[Const:INT,3],MINUS,[BinOp:[Const:INT,2],TIMES,[Const:INT,4]]],MINUS,[Const:INT,7]]][Print:[Id:b]]]", program.toString());
    }
}
