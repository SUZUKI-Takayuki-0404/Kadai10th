package exercise.kadai10th.exceptionhandler;

public class NoResourceException extends RuntimeException {

    public NoResourceException() {
        super();
    }

    public NoResourceException(String message, Throwable cause) {
        super(message + " : not found", cause);
    }

    public NoResourceException(String message) {
        super(message + " : not found");
    }

    public NoResourceException(Throwable cause) {
        super(cause);
    }

}
