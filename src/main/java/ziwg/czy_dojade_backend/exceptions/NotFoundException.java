package ziwg.czy_dojade_backend.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {}
    public NotFoundException(String message) {
        super(message);
    }
}
