package ziwg.czy_dojade_backend.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.exceptions.*;
import ziwg.czy_dojade_backend.models.Accident;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class AppUserServiceUtils {
    public static void hashPasswords(PasswordEncoder passwordEncoder, ChangePasswordDto user){
        user.setOldPassword(passwordEncoder.encode(CharBuffer.wrap(user.getOldPassword())));
        user.setNewPassword(passwordEncoder.encode(CharBuffer.wrap(user.getNewPassword())));
        user.setNewPasswordConfirm(passwordEncoder.encode(CharBuffer.wrap(user.getNewPasswordConfirm())));
    }

    public AppUser getAuthenticatedUser() throws UserNotAuthenticatedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null){
            throw new UserNotAuthenticatedException("User not authenticated");
        }
        return (AppUser) auth.getPrincipal();
    }

    public static AppUser validateChangePassword(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository, ChangePasswordDto user) throws NotFoundException, BadCredentialsException, UserMismatchException {
        Optional<AppUser> updatedUser = appUserRepository.findByEmail(user.getEmail());
        AppUser authenticatedUser = getAuthenticatedUser();

        if (updatedUser.isEmpty()){
            throw new NotFoundException("User with email " + user.getEmail() + " was not found");
        }
        if (!updatedUser.get().getEmail().equals(authenticatedUser.getEmail())){
            throw new UserMismatchException("Authenticated user does not match the user with email " + user.getEmail());
        }
        if (!user.getNewPassword().equals(user.getNewPasswordConfirm())){
            throw new BadCredentialsException("New password and new password confirmation are not the same");
        }
        if (user.getOldPassword().equals(user.getNewPassword())){
            throw new BadCredentialsException("New password and old password are the same");
        }
        if (!passwordEncoder.matches(user.getOldPassword(), updatedUser.get().getHashPassword())){
            throw new BadCredentialsException("Old password is incorrect");
        }
        if (!RegexUtil.isValidPassword(user.getNewPassword())) {
            throw new InvalidPasswordException("Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character and be between 8 and 30 characters long");
        }
        hashPasswords(passwordEncoder, user);
        return updatedUser.get();
    }

    public static AppUser validateUser(Optional<AppUser> user, Long id, AppUser authenticatedUser) throws NotFoundException, UserMismatchException {
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (!user.get().getEmail().equals(authenticatedUser.getEmail())){
            throw new UserMismatchException("Authenticated user does not match the user with id " + id);
        }
        return user.get();
    }
    public static Trip findNearestTrip(List<Trip> trips, double accidentLatitude, double accidentLongitude){
        Trip nearestTrip = null;
        double minDistance = Double.MAX_VALUE;

        for(Trip trip : trips){
            double distance = calculateDistance(
                    trip.getVehicle().getCurrLatitude(),
                    trip.getVehicle().getCurrLongitude(),
                    accidentLatitude,
                    accidentLongitude
            );
            if(distance < minDistance){
                minDistance = distance;
                nearestTrip = trip;
            }
        }
        return nearestTrip;
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    public static Accident findNearestAccident(List<Accident> accidents, double accidentLatitude, double accidentLongitude){
        Accident nearestAccident = null;
        double minDistance = Double.MAX_VALUE;

        for(Accident accident : accidents){
            double distance = calculateDistance(
                    accident.getAccLatitude(),
                    accident.getAccLongitude(),
                    accidentLatitude,
                    accidentLongitude
            );
            if(distance < minDistance){
                minDistance = distance;
                nearestAccident = accident;
            }
        }
        return nearestAccident;
    }

}
