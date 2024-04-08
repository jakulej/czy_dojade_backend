package ziwg.czy_dojade_backend.exceptions;

public class AlreadySubscribedException extends RuntimeException{
    public AlreadySubscribedException() {}
    public AlreadySubscribedException(String message) {
        super(message);
    }
}
