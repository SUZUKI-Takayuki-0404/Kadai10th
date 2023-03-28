package exercise.kadai10th.exceptionhandler;

public class CodeInUseException extends RuntimeException {

    /* istanbul ignore next */
    public CodeInUseException() {
        super();
    }

    /* istanbul ignore next */
    public CodeInUseException(String message, Throwable cause) {
        super(message + " code in use", cause);
    }

    /* istanbul ignore next */
    public CodeInUseException(String message) {
        super(message + " code in use");
    }

    /* istanbul ignore next */
    public CodeInUseException(Throwable cause) {
        super(cause);
    }
}
