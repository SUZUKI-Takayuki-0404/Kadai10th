package exercise.kadai10th.exceptionhandler;

public class CodeInUseException extends RuntimeException {

    public CodeInUseException() {
        /* istanbul ignore next */
        super();
    }

    public CodeInUseException(String message, Throwable cause) {
        /* istanbul ignore next */
        super(message + " code in use", cause);
    }

    public CodeInUseException(String message) {
        /* istanbul ignore next */
        super(message + " code in use");
    }

    public CodeInUseException(Throwable cause) {
        /* istanbul ignore next */
        super(cause);
    }
}
