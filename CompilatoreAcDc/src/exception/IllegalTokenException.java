package exception;

public class IllegalTokenException extends InternalException {
    public IllegalTokenException(String message, Throwable innerException) {
        super(message, innerException);
    }

    public IllegalTokenException(String message) {
        super(message);
    }
}
