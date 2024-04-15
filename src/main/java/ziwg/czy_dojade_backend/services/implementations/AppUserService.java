package ziwg.czy_dojade_backend.services.implementations;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.dtos.AccidentDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportCreationDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportDetailsDTO;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.SignUpDto;
import ziwg.czy_dojade_backend.exceptions.*;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.repositories.*;
import ziwg.czy_dojade_backend.services.interfaces.IAppUserService;
import ziwg.czy_dojade_backend.utils.AppUserServiceUtils;

import javax.naming.LimitExceededException;
import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements IAppUserService
{

    private final AppUserRepository appUserRepository;
    private final RouteRepository routeRepository;
    private final ReportRepository reportRepository;
    private final AccidentRepository accidentRepository;
    private final TripRepository tripRepository;

    private final PasswordEncoder passwordEncoder;

    public void hashPasswords(ChangePasswordDto user){
        user.setOldPassword(passwordEncoder.encode(CharBuffer.wrap(user.getOldPassword())));
        user.setNewPassword(passwordEncoder.encode(CharBuffer.wrap(user.getNewPassword())));
        user.setNewPasswordConfirm(passwordEncoder.encode(CharBuffer.wrap(user.getNewPasswordConfirm())));
    }
    public void validateSignUp(SignUpDto user) throws AlreadyExistsException {
        if (appUserRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        else if (appUserRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }
    }

    public AppUser validateChangePassword(ChangePasswordDto user) throws NotFoundException, BadCredentialsException {
        Optional<AppUser> updatedUser = appUserRepository.findByEmail(user.getEmail());
        if (updatedUser.isEmpty()){
            throw new NotFoundException("User with email " + user.getEmail() + " was not found");
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
        hashPasswords(user);
        return updatedUser.get();
    }

    @Override
    public boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
    }
    @Override
    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    @Override
    public List<AppUser> getAllUsers() { return appUserRepository.findAll(); }
    @Override
    public AppUser getUserById(Long id) throws NotFoundException{
        return appUserRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }
    @Override
    public AppUser getUserByEmail(String email) throws NotFoundException {
        return appUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }
    @Override
    public AppUser getUserByUsername(String username) throws NotFoundException {
        return appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }

    @Override
    public AppUser signUpUser(SignUpDto user) throws AlreadyExistsException {
        String pswd = passwordEncoder.encode(CharBuffer.wrap(user.getHashPassword()));
        user.setHashPassword(pswd);
        validateSignUp(user);

        AppUser newUser = new AppUser();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setHashPassword(user.getHashPassword());

        user.clearPassword();
        appUserRepository.saveAndFlush(newUser);
        return newUser;
    }
    @Override
    public AppUser updateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException {
        Optional<AppUser> updatedUser = appUserRepository.findById(id);
        if (updatedUser.isEmpty()){
            throw new NotFoundException("User with id " + id + " not found");
        }

        updatedUser.get().setUsername(user.getUsername());
        updatedUser.get().setEmail(user.getEmail());
        updatedUser.get().setFirstName(user.getFirstName());
        updatedUser.get().setLastName(user.getLastName());
        updatedUser.get().setSubscriber(user.isSubscriber());

        appUserRepository.saveAndFlush(updatedUser.get());
        return updatedUser.get();
    }
    @Override
    public AppUser changePassword(ChangePasswordDto user) throws NotFoundException, BadCredentialsException {
        AppUser updatedUser = validateChangePassword(user);

        updatedUser.setHashPassword(user.getNewPassword());
        user.clearPasswords();
        appUserRepository.saveAndFlush(updatedUser);

        return updatedUser;
    }
    @Override
    public AppUser deleteUser(Long id) throws NotFoundException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        AppUser deletedUser = user.get();
        appUserRepository.deleteById(id);
        return deletedUser;
    }

    @Override
    public Route addRouteToFavourites(Long id, String routeName) throws NotFoundException, LimitExceededException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        Optional<Route> route = routeRepository.findByShortName(routeName);
        if (route.isEmpty()) {
            throw new NotFoundException("Route with name " + routeName + " not found");
        }

        int favouritesCount = user.get().getFavouriteRoutes().size();
        if (user.get().isSubscriber() && favouritesCount >= 10){
            throw new LimitExceededException("User with id " + id + " has reached the limit of favourite routes for a subscriber (10)");
        }
        else if (!user.get().isSubscriber() && favouritesCount >= 3){
            throw new LimitExceededException("User with id " + id + " has reached the limit of favourite routes for a non-subscriber (3)");
        }
        else if(user.get().getFavouriteRoutes().contains(route.get())){
            throw new AlreadyExistsException("Route with name " + routeName + " already exists in favourites");
        }
        else{
            user.get().getFavouriteRoutes().add(route.get());
            appUserRepository.saveAndFlush(user.get());
            return route.get();
        }
    }

    @Override
    public Route removeRouteFromFavourites(Long id, String routeName) throws NotFoundException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        Optional<Route> route = routeRepository.findByShortName(routeName);
        if (route.isEmpty()) {
            throw new NotFoundException("Route with name " + routeName + " not found");
        }

        if (!user.get().getFavouriteRoutes().contains(route.get())){
            throw new NotFoundException("Route with name " + routeName + " not found in favourites");
        }
        else{
            user.get().getFavouriteRoutes().remove(route.get());
            appUserRepository.saveAndFlush(user.get());
            return route.get();
        }
    }

    @Override
    public ReportDetailsDTO reportAccident(Long id, ReportCreationDto reportDto) throws NotFoundException {
        if (reportDto == null){
            throw new NotFoundException("Report data not found");
        }
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id: " + id + " not found");
        }
        List<Trip> trips = tripRepository.findAllByTripHeadsign(
                reportDto
                        .getAccidentDto()
                        .getTripHeadsign()
        );
        if (trips.isEmpty()){
            throw new NotFoundException("Trip with headsign: " + reportDto.getAccidentDto().getTripHeadsign() + " not found");
        }

        LocalDateTime currentTime = LocalDateTime.now();
//        LocalDateTime startTime = currentTime.minusHours(3);
//        LocalDateTime endTime = currentTime.plusHours(3);

        Trip trip = AppUserServiceUtils.findNearestTrip(
                trips,
                reportDto.getAccidentDto().getAccLatitude(),
                reportDto.getAccidentDto().getAccLongitude()
        );
        Accident accident = accidentRepository.findByTripId(trip.getId());
//                .orElseGet(() -> {
//                    Accident newAccident = new Accident();
//                    newAccident.setAccLatitude(reportDto.getAccidentDto().getAccLatitude());
//                    newAccident.setAccLongitude(reportDto.getAccidentDto().getAccLongitude());
//                    newAccident.setVerified(false);
//                    newAccident.setTimeOfAccident(currentTime);
//                    newAccident.setTrip(trip);
//                    return accidentRepository.saveAndFlush(newAccident);
//                });
        if (accident == null) {
            accident = new Accident();
            accident.setAccLatitude(reportDto.getAccidentDto().getAccLatitude());
            accident.setAccLongitude(reportDto.getAccidentDto().getAccLongitude());
            accident.setVerified(false);
            accident.setTimeOfAccident(currentTime);
            accident.setTrip(trip);
            accidentRepository.saveAndFlush(accident);
        }
//            accident = AppUserServiceUtils.findNearestAccident(
//                    accidents,
//                    reportDto.getAccidentDto().getAccLatitude(),
//                    reportDto.getAccidentDto().getAccLongitude()
//            );

        if (!accident.isVerified()){
            List<Report> reports = reportRepository.findAllByAccident_Id(accident.getId());
            accident.setVerified(reports.size() >= 2);
        }


        Report report = new Report();
        report.setDescription(reportDto.getDescription());
        report.setTimeOfReport(currentTime);
        report.setUser(user.get());
        report.setAccident(accident);

        reportRepository.saveAndFlush(report);

        AccidentDto accidentDto = new AccidentDto();
        accidentDto.setAccLatitude(accident.getAccLatitude());
        accidentDto.setAccLongitude(accident.getAccLongitude());
        accidentDto.setTripHeadsign(accident.getTrip().getTripHeadsign());

        ReportDetailsDTO output = new ReportDetailsDTO();
        output.setDescription(report.getDescription());
        output.setTimeOfReport(report.getTimeOfReport());
        output.setAccidentDto(accidentDto);
        output.setUserId(report.getUser().getId());

        return output;
    }

    @Override
    public Optional<AppUser> subscribe(Long id) throws NotFoundException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (user.get().isSubscriber()){
            throw new AlreadySubscribedException("User with id " + id + " already is a subscriber");
        }
        user.get().setSubscriber(true);
        appUserRepository.saveAndFlush(user.get());
        return user;
    }

    @Override
    public Optional<AppUser> unsubscribe(Long id) throws NotFoundException, LimitExceededException {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (!user.get().isSubscriber()){
            throw new NotSubscribedException("User with id " + id + " is not a subscriber");
        }
        if (user.get().getFavouriteRoutes().size() > 3){
            throw new LimitExceededException("User with id " + id + " has more than 3 favourite routes - cannot unsubscribe until the number of favourite routes is 3 or less");
        }
        user.get().setSubscriber(false);
        appUserRepository.saveAndFlush(user.get());
        return user;
    }
}
