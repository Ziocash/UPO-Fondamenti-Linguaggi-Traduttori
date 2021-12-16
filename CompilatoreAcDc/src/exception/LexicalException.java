package exception;

public class LexicalException extends Exception {

    public LexicalException(String message, Throwable innerException) {
        super(message, innerException);
    }

    public LexicalException(String message) {
        super(message);
    }
}
