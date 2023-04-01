package exercise.kadai10th.exceptionhandler;

public class SameAsCurrentException extends RuntimeException {
    public SameAsCurrentException(String message) {
        super(message);
    }
}
