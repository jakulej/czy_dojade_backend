package ziwg.czy_dojade_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.models.AppUser;
import ziwg.czy_dojade_backend.repositories.AppUserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(appUser.get().getUsername(), appUser.get().getHashPassword(), new ArrayList<>());
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add((GrantedAuthority) () -> role);
        }
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
