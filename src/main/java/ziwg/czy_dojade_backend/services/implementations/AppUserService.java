package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.config.JWTService;
import ziwg.czy_dojade_backend.dtos.AccidentDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportCreationDto;
import ziwg.czy_dojade_backend.dtos.reports.ReportDetailsDTO;
import ziwg.czy_dojade_backend.dtos.user.AppUserDto;
import ziwg.czy_dojade_backend.dtos.user.ChangePasswordDto;
import ziwg.czy_dojade_backend.dtos.user.UpdateUserResponseDto;
import ziwg.czy_dojade_backend.exceptions.*;
import ziwg.czy_dojade_backend.models.*;
import ziwg.czy_dojade_backend.repositories.*;
import ziwg.czy_dojade_backend.services.interfaces.IAppUserService;
import ziwg.czy_dojade_backend.utils.AppUserServiceUtils;

import javax.naming.LimitExceededException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ziwg.czy_dojade_backend.utils.AppUserServiceUtils.validateChangePassword;
import static ziwg.czy_dojade_backend.utils.AppUserServiceUtils.validateUser;

@Service
@RequiredArgsConstructor
public class AppUserService implements IAppUserService
{

    private final AppUserRepository appUserRepository;
    private final RouteRepository routeRepository;
    private final ReportRepository reportRepository;
    private final AccidentRepository accidentRepository;
    private final TripRepository tripRepository;

    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public boolean existsByEmail(String email) {
        return appUserRepository.existsByEmail(email);
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
    public AppUser getAuthenticatedUser() throws UserNotAuthenticatedException{
        return AppUserServiceUtils.getAuthenticatedUser();
    }

    @Override
    public UpdateUserResponseDto updateUser(Long id, AppUserDto user) throws NotFoundException, AlreadyExistsException {
        AppUser updatedUser = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());

        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());

        appUserRepository.saveAndFlush(updatedUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getEmail());
        String newToken = jwtService.generateToken(userDetails);

        return UpdateUserResponseDto
                .builder()
                .user(updatedUser)
                .token(newToken)
                .build();
    }
    @Override
    public UpdateUserResponseDto changePassword(ChangePasswordDto user) throws NotFoundException, BadCredentialsException {
        AppUser updatedUser = validateChangePassword(passwordEncoder,appUserRepository, user);

        updatedUser.setHashPassword(user.getNewPassword());
        user.clearPasswords();
        appUserRepository.saveAndFlush(updatedUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getEmail());
        String newToken = jwtService.generateToken(userDetails);

        return UpdateUserResponseDto
                .builder()
                .user(updatedUser)
                .token(newToken)
                .build();
    }
    @Override
    public AppUser deleteUser(Long id) throws NotFoundException {
        AppUser user = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());
        appUserRepository.deleteById(id);
        return user;
    }

    @Override
    public Route addRouteToFavourites(Long id, String routeName) throws NotFoundException, LimitExceededException {
        AppUser user = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());
        Optional<Route> route = routeRepository.findByShortName(routeName);
        if (route.isEmpty()) {
            throw new NotFoundException("Route with name " + routeName + " not found");
        }

        int favouritesCount = user.getFavouriteRoutes().size();
        if (user.isSubscriber() && favouritesCount >= 10){
            throw new LimitExceededException("User with id " + id + " has reached the limit of favourite routes for a subscriber (10)");
        }
        else if (!user.isSubscriber() && favouritesCount >= 3){
            throw new LimitExceededException("User with id " + id + " has reached the limit of favourite routes for a non-subscriber (3)");
        }
        else if(user.getFavouriteRoutes().contains(route.get())){
            throw new AlreadyExistsException("Route with name " + routeName + " already exists in favourites");
        }
        user.getFavouriteRoutes().add(route.get());
        appUserRepository.saveAndFlush(user);
        return route.get();
    }

    @Override
    public Route removeRouteFromFavourites(Long id, String routeName) throws NotFoundException {
        AppUser user = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());
        Optional<Route> route = routeRepository.findByShortName(routeName);
        if (route.isEmpty()) {
            throw new NotFoundException("Route with name " + routeName + " not found");
        }

        if (!user.getFavouriteRoutes().contains(route.get())){
            throw new NotFoundException("Route with name " + routeName + " not found in favourites");
        }
        else{
            user.getFavouriteRoutes().remove(route.get());
            appUserRepository.saveAndFlush(user);
            return route.get();
        }
    }

    @Override
    public ReportDetailsDTO reportAccident(Long id, ReportCreationDto reportDto) throws NotFoundException {
        AppUser user = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());
        if (reportDto == null){
            throw new NotFoundException("Report data not found");
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

        Trip trip = AppUserServiceUtils.findNearestTrip(
                trips,
                reportDto.getAccidentDto().getAccLatitude(),
                reportDto.getAccidentDto().getAccLongitude()
        );
        Accident accident = accidentRepository.findByTripId(trip.getId());

        if (accident == null) {
            accident = new Accident();
            accident.setAccLatitude(reportDto.getAccidentDto().getAccLatitude());
            accident.setAccLongitude(reportDto.getAccidentDto().getAccLongitude());
            accident.setVerified(false);
            accident.setTimeOfAccident(currentTime);
            accident.setTrip(trip);
            accidentRepository.saveAndFlush(accident);
        }

        if (!accident.isVerified()){
            List<Report> reports = reportRepository.findAllByAccident_Id(accident.getId());
            boolean currentUserReported = reports.stream()
                    .anyMatch(report -> report.getUser().getId() == (id));
            if (!currentUserReported){
                accident.setVerified(reports.size() >= 2);
            }
        }

        Report report = new Report();
        report.setDescription(reportDto.getDescription());
        report.setTimeOfReport(currentTime);
        report.setUser(user);
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
    public AppUser subscribe(Long id) throws AlreadySubscribedException {
        AppUser user = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());
        if (user.isSubscriber()){
            throw new AlreadySubscribedException("User with id " + id + " already is a subscriber");
        }
        user.setSubscriber(true);
        appUserRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public AppUser unsubscribe(Long id) throws LimitExceededException, NotSubscribedException {
        AppUser user = validateUser(appUserRepository.findById(id), id, getAuthenticatedUser());
        if (!user.isSubscriber()){
            throw new NotSubscribedException("User with id " + id + " is not a subscriber");
        }
        if (user.getFavouriteRoutes().size() > 3){
            throw new LimitExceededException("User with id " + id + " has more than 3 favourite routes - cannot unsubscribe until the number of favourite routes is 3 or less");
        }
        user.setSubscriber(false);
        appUserRepository.saveAndFlush(user);
        return user;
    }
}
