import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import parser.Parser;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

public class Main {
    public static void main(String[] args) {
        try {
            Logger logger = Logger.getLogger(Main.class.getName());
            JFileChooser chooser = new JFileChooser(new File(".").getCanonicalPath());
            int value = chooser.showOpenDialog(null);
            if (value == JFileChooser.APPROVE_OPTION) {
                var scanner = new Scanner(chooser.getSelectedFile().getAbsolutePath());
                var parser = new Parser(scanner);
                var nP = parser.parse();
                var typeVisitor = new TypeCheckingVisitor();
                nP.accept(typeVisitor);
                if (!typeVisitor.hasErrors()) {
                    var codeGenVisitor = new CodeGeneratorVisitor();
                    nP.accept(codeGenVisitor);
                    value = chooser.showSaveDialog(null);
                    try (var writer = new FileWriter(chooser.getSelectedFile().getAbsolutePath())) {
                        writer.write(codeGenVisitor.getCode());
                    }
                    logger.log(Level.INFO, codeGenVisitor.getCode());
                    logger.log(Level.INFO, "Has errors: {0}", typeVisitor.hasErrors());
                } else
                    logger.log(Level.SEVERE, typeVisitor.getLoggerString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
