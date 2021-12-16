package exception;

public class IllegalTokenException extends Exception {

    public IllegalTokenException(String message, Throwable innerException) {
        super(message, innerException);
    }

    public IllegalTokenException(String message) {
        super(message);
    }
}
