package exception;

public class InternalException extends Exception{
    protected Throwable innerException;

    public InternalException(String message, Throwable innerException) {
        super(message);
        this.innerException = innerException;
    }

    public InternalException(String message) {
        super(message);
    }
}