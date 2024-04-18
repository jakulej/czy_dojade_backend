package ziwg.czy_dojade_backend.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ziwg.czy_dojade_backend.config.CustomUserDetailsService;
import ziwg.czy_dojade_backend.dtos.user.LoginDto;
import ziwg.czy_dojade_backend.models.AppUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static ziwg.czy_dojade_backend.config.SecurityConstraints.EXPIRATION_TIME;
import static ziwg.czy_dojade_backend.config.SecurityConstraints.SECRET;

//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//        setFilterProcessesUrl("/user/login");
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            LoginDto loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginDto.getEmail(),
//                            loginDto.getHashPassword(),
//                            new ArrayList<>()
//                    )
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain,
//            Authentication auth) throws IOException {
//        String token = JWT.create().
//                withSubject(((AppUser)auth.getPrincipal()).getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .sign(Algorithm.HMAC512(SECRET.getBytes()));
//        String body = ((AppUser)auth.getPrincipal()).getUsername() + " " + token;
//
//        response.getWriter().write(body);
//        response.getWriter().flush();
//    }
//
//}

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTGenerator tokenGenerator;
    private final CustomUserDetailsService userDetailsService;

    public JWTAuthenticationFilter(JWTGenerator tokenGenerator, CustomUserDetailsService userDetailsService) {
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getUsernameFromJwt(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}