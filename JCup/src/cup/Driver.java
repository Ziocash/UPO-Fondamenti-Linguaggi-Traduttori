package cup;

import java.util.logging.Level;
import java.util.logging.Logger;

import exception.SyntacticException;

public class Driver {
    public static void main(String[] args) {
        var logger = Logger.getLogger(Driver.class.getName());
        String expected = "1.0 6 5 k / sb 0 k lb p P 1 6 / sa 0 k la p P la sb 0 k";
        Parser parser = new Parser();
        try {
            parser.parse();
            logger.log(Level.INFO, parser.getCode());
            if(!parser.getCode().equals(expected))
                throw new SyntacticException("Code does not match.");
            logger.log(Level.INFO, parser.getLoggerString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
