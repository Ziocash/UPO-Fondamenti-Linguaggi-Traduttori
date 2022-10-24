package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import ast.NodeProgram;
import ast.TypeDescriptor;
import exception.SyntacticException;
import parser.Parser;
import scanner.Scanner;
import symboltable.SymbolTable;
import visitor.TypeCheckingVisitor;

public class TestTypeCheck {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    /**
     * Tests repeated declarations.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testRepeatedDeclarations() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testRepeatedDeclarations.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertEquals(TypeDescriptor.ERROR, nP.getResType());
    }

    /**
     * Tests correct type.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testTypeCheckCorrect() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testTypeCheckCorrect.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertEquals(TypeDescriptor.VOID, nP.getResType());
    }

    /**
     * Tests correct type.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testTypeCheckCorrect2() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testTypeCheckCorrect2.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertEquals(TypeDescriptor.VOID, nP.getResType());
    }

    /**
     * Tests id not declared.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testIdNotDeclared() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testIdNotDeclared.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }

    /**
     * General test 1 with errors in code.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testGeneral() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testTypeGeneral.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }

    /**
     * General test 2 with no errors in code.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testGeneral2() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testTypeGeneral2.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertFalse(visitor.hasErrors());
    }

    /**
     * Tests if assignment and conversion error are caught.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testErrorAssignConvert() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\errorAssignConvert.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }

    /**
     * Tests if operation errors are caught.
     * @throws FileNotFoundException Scanner source file not found.
     * @throws SyntacticException Parser found a Syntactic exception.
     */
    @Test
    public void testErrorOp() throws FileNotFoundException, SyntacticException {
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\errorOp.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO,visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }
}
