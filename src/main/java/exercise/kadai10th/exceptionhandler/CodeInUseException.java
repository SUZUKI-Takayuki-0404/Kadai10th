package exercise.kadai10th.exceptionhandler;

public class CodeInUseException extends RuntimeException {

    public CodeInUseException() {
        super();
    }

    public CodeInUseException(String message, Throwable cause) {
        super(message + " code in use", cause);
    }

    public CodeInUseException(String message) {
        super(message + " code in use");
    }

    public CodeInUseException(Throwable cause) {
        super(cause);
    }
}
