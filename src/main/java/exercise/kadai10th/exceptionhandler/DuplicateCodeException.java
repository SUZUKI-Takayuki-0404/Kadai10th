package exercise.kadai10th.exceptionhandler;

public class DuplicateCodeException extends RuntimeException {
    public DuplicateCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
