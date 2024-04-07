package ziwg.czy_dojade_backend.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException() {}
    public AlreadyExistsException(String message) {
        super(message);
    }
}
