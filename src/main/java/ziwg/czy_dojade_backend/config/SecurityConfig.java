package ziwg.czy_dojade_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ziwg.czy_dojade_backend.config.jwt.JWTAuthEntryPoint;
import ziwg.czy_dojade_backend.config.jwt.JWTAuthenticationFilter;
import ziwg.czy_dojade_backend.config.jwt.JWTGenerator;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    private JWTGenerator tokenGenerator;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    private JWTAuthEntryPoint authEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(tokenGenerator, userDetailsService);
    }

//    @Bean
//    public AuthTokenFilter authenticationJwtTokenFilter() {
//        return new AuthTokenFilter();
//    }
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        return provider;
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
//        return builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()).and().build();
//    }

//    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userDetailsService = userDetailsService;
//    }

//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors().and()
//                .csrf().disable();
//        http.authorizeHttpRequests()
////                .anyRequest().authenticated()
//                .anyRequest().permitAll();
////        http
////                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
////                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
////                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////        http.headers().frameOptions().disable();
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().
                csrf().disable()
                .exceptionHandling().
                authenticationEntryPoint(authEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ).and()
                .authorizeHttpRequests()
//                .anyRequest().authenticated()
                .anyRequest().permitAll().and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService users(){
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//
//    @Bean
//    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager());
//        filter.setFilterProcessesUrl("/user/login");
//        return filter;
//    }

//     4
}
