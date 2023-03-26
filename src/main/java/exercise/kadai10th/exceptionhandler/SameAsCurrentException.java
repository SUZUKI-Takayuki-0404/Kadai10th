package exercise.kadai10th.exceptionhandler;

public class SameAsCurrentException extends RuntimeException {

    public SameAsCurrentException() {
        super();
    }

    public SameAsCurrentException(String message, Throwable cause) {
        super(message + " nothing updated", cause);
    }

    public SameAsCurrentException(String message) {
        super(message + " nothing updated");
    }

    public SameAsCurrentException(Throwable cause) {
        super(cause);
    }

}
