package graduatethesis.performancemonitoringsystem.security;


public class SecurityConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REQUEST_PARAM_TOKEN = "access_token";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 dena
    public static final String SECRET = "PMS@AutTHT0ken";
    public static final String LOGIN_URL = "/rest/login";
    public static final String LOGOUT = "/rest/logout";
    public static final String PUBLIC_URLS="/rest/public/**";
    public final static String CLAIM_AUTHORITY = "authority";
    public final static String CONFIRM_REGISTRATION="/rest/user/confirm/registration/**";
    public final static String RESET_PASSWORD="/rest/user/reset-password-token**";
    public final static String CONFIRM_RESET="/rest/user/new/password/**";
    public final static String NEW_PASSWORD="/rest/user/reset-password**";
    public final static String CREATE_USER="/rest/user/create";
    public final static String SIGNUP="/rest/user/signup";
}
