package exercise.kadai10th.exceptionhandler;

public class NoResourceException extends RuntimeException {

    public NoResourceException() {
        /* istanbul ignore next */
        super();
    }

    public NoResourceException(String message, Throwable cause) {
        /* istanbul ignore next */
        super(message + " : not found", cause);
    }

    public NoResourceException(String message) {
        /* istanbul ignore next */
        super(message + " : not found");
    }

    public NoResourceException(Throwable cause) {
        /* istanbul ignore next */
        super(cause);
    }
}
