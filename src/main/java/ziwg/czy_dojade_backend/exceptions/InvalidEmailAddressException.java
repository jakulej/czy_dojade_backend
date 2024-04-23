package ziwg.czy_dojade_backend.exceptions;

public class InvalidEmailAddressException extends RuntimeException {
    public InvalidEmailAddressException() {}
    public InvalidEmailAddressException(String invalidEmail) {
        super(invalidEmail);
    }
}
