package app.exception;

public class MaximumCategoriesReachedException extends RuntimeException {
    public MaximumCategoriesReachedException(String message) {
        super(message);
    }
}
