package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

import ast.LangType;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeProgram;
import exception.SyntacticException;
import parser.Parser;
import scanner.Scanner;
import token.Token;
import token.TokenType;

public class TestParser {

    @Test
    public void testDecParse() throws FileNotFoundException {
        Scanner scanner = new Scanner("C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testDec.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        NodeDecSt node = new NodeDecl(LangType.INT, "a");
        assertEquals(node, program.iterator().next());
        assertEquals(new NodeDecl(LangType.FLOAT, "b"), program.iterator().next());
    }

    /**
     * <p>Throws an exception on {@code parser.parse()} due to {@code int float;} statement in file.</p>
     * 
     * <p>Statement {@code int float;} is not allowed due to an ID cannot be called as a type.</p>
     */
    @Test
    public void testDSsDclStm() throws FileNotFoundException{
        Scanner scanner = new Scanner("C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testDSsDclStm.txt");
        Parser parser = new Parser(scanner);
        assertThrows(SyntacticException.class, parser::parse);
    }

    /**
     * Just a quick test to see if {@code ArrayList} inserts elements on head
     */
    @Test
    public void testAddOnHead()
    {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(0, new Token(TokenType.EOF, 2));
        assertTrue(TokenType.EOF == tokens.get(0).getType());

        tokens.add(0, new Token(TokenType.INT, 1, "temp"));
        assertTrue(TokenType.INT == tokens.get(0).getType());
        assertEquals("temp", tokens.get(0).getValue());
    }

    /**
     * Quick test on {@code toString()} methods
     * 
     * @throws FileNotFoundException
     */
    @Test
    public void testToString() throws FileNotFoundException
    {
        Scanner scanner = new Scanner("C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testDec.txt");
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        assertEquals( "[Program:[Decl:INT,[Id:a]][Decl:FLOAT,[Id:b]][Print:[Id:a]]]", program.toString());
    }
}
