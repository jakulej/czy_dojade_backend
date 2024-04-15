package ziwg.czy_dojade_backend.config;

public class SecurityConstraints {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_00; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/user/signup";
    public static final String FRONTEND_URL = "http://localhost:3000";

}
