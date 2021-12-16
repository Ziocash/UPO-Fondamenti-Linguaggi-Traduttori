package exception;

public class SyntacticException extends InternalException {
    public SyntacticException(String message, Throwable innerException) {
        super(message, innerException);
    }

    public SyntacticException(String message) {
        super(message);
    }
}