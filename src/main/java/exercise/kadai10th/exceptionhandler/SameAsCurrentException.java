package exercise.kadai10th.exceptionhandler;

public class SameAsCurrentException extends RuntimeException {

    public SameAsCurrentException() {
        /* istanbul ignore next */
        super();
    }

    public SameAsCurrentException(String message, Throwable cause) {
        /* istanbul ignore next */
        super(message + " nothing updated", cause);
    }

    public SameAsCurrentException(String message) {
        /* istanbul ignore next */
        super(message + " nothing updated");
    }

    public SameAsCurrentException(Throwable cause) {
        /* istanbul ignore next */
        super(cause);
    }
}
