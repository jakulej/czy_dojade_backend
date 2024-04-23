package ziwg.czy_dojade_backend.exceptions;

public class UserMismatchException extends RuntimeException{
    public UserMismatchException(){}
    public UserMismatchException(String message) {
        super(message);
    }
}
