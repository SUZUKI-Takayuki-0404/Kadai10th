package exercise.kadai10th.exceptionhandler;

public class NoResourceException extends RuntimeException {

    /* istanbul ignore next */
    public NoResourceException() {
        super();
    }

    /* istanbul ignore next */
    public NoResourceException(String message, Throwable cause) {
        super(message + " : not found", cause);
    }

    /* istanbul ignore next */
    public NoResourceException(String message) {
        super(message + " : not found");
    }

    /* istanbul ignore next */
    public NoResourceException(Throwable cause) {
        super(cause);
    }
}
