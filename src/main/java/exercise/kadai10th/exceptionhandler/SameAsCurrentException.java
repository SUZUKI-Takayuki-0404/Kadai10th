package exercise.kadai10th.exceptionhandler;

public class SameAsCurrentException extends RuntimeException {

    /* istanbul ignore next */
    public SameAsCurrentException() {
        super();
    }

    /* istanbul ignore next */
    public SameAsCurrentException(String message, Throwable cause) {
        super(message + " nothing updated", cause);
    }

    /* istanbul ignore next */
    public SameAsCurrentException(String message) {
        super(message + " nothing updated");
    }

    /* istanbul ignore next */
    public SameAsCurrentException(Throwable cause) {
        super(cause);
    }
}
