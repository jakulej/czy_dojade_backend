package ziwg.czy_dojade_backend.exceptions;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException() {}
    public PasswordMismatchException(String message) {
        super(message);
    }
}
