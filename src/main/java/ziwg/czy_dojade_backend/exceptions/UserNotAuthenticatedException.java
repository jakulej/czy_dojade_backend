package ziwg.czy_dojade_backend.exceptions;

public class UserNotAuthenticatedException extends RuntimeException{
    public UserNotAuthenticatedException() {}
    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
