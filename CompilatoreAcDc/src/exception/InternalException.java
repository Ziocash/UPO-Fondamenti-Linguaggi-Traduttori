package exception;

public class InternalException extends Exception {

    public InternalException(String message, Throwable innerException) {
        super(message, innerException);
    }

    public InternalException(String message) {
        super(message);
    }
}