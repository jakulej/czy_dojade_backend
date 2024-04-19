package ziwg.czy_dojade_backend.exceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {}
    public InvalidPasswordException(String message) {
        super(message);
    }
}
