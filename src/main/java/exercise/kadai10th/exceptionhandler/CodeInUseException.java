package exercise.kadai10th.exceptionhandler;

public class CodeInUseException extends RuntimeException {
    public CodeInUseException(String message) {
        super(message);
    }
}
