package ziwg.czy_dojade_backend.exceptions;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException() {}
    public BadCredentialsException(String message) {
        super(message);
    }
}
