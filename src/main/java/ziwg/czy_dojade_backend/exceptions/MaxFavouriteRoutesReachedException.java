package ziwg.czy_dojade_backend.exceptions;

public class MaxFavouriteRoutesReachedException extends RuntimeException{
    public MaxFavouriteRoutesReachedException() {}
    public MaxFavouriteRoutesReachedException(String message) {
        super(message);
    }
}
