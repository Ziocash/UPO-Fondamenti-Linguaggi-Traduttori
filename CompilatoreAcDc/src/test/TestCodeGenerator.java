package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ast.NodeProgram;
import ast.TypeDescriptor;
import parser.Parser;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

public class TestCodeGenerator {

    /**
     * General code generation test.
     * 
     * @throws IOException If the named file exists but is a directory rather than a
     *                     regular file, does not exist but cannot be created, or
     *                     cannot be opened for any other reason.
     */
    @Test
    public void testGeneral() throws IOException {
        var logger = Logger.getLogger(this.getClass().getName());
        Scanner scanner = new Scanner(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\testTypeGeneral2.txt");
        Parser parser = new Parser(scanner);
        NodeProgram nP = assertDoesNotThrow(parser::parse);
        var typeVisitor = new TypeCheckingVisitor();
        nP.accept(typeVisitor);
        logger.log(Level.INFO, "nP value: {0}", nP);
        assertEquals(TypeDescriptor.VOID, nP.getResType());
        var codeGenVisitor = new CodeGeneratorVisitor();
        nP.accept(codeGenVisitor);
        try (var writer = new FileWriter(
                "C:\\Users\\Simone Gattini\\source\\repos\\UPO-Fondamenti-Linguaggi-Traduttori\\CompilatoreAcDc\\src\\test\\data\\output.txt")) {
            writer.write(codeGenVisitor.getCode());
        }
        String expected = "1.0 6 5 k / sb 0 k lb p P 1 6 / sa 0 k la p P la sb 0 k";
        assertEquals(expected, codeGenVisitor.getCode());
    }
}
