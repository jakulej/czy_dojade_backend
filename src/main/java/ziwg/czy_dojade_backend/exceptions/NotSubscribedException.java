package ziwg.czy_dojade_backend.exceptions;

public class NotSubscribedException extends RuntimeException{
    public NotSubscribedException() {}
    public NotSubscribedException(String message) {
        super(message);
    }
}
