package ziwg.czy_dojade_backend.repositories;

import ziwg.czy_dojade_backend.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByHashPassword(String hashPassword);
    boolean existsByEmailAndUsername(String email, String username);
    boolean existsByEmailAndHashPassword(String email, String hashPassword);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findAllBySubscriber(boolean subscriber);
    @Query("SELECT u.hashPassword FROM AppUser u")
    List<String> getAllHashedPasswords();

}
