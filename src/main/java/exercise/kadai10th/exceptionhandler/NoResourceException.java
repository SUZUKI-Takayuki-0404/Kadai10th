package exercise.kadai10th.exceptionhandler;

public class NoResourceException extends RuntimeException {
    public NoResourceException(String message) {
        super(message);
    }
}
