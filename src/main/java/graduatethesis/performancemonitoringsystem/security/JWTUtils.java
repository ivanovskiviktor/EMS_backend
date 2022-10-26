package graduatethesis.performancemonitoringsystem.security;

import com.auth0.jwt.JWT;
import graduatethesis.performancemonitoringsystem.model.exceptions.PasswordsNotTheSameException;
import graduatethesis.performancemonitoringsystem.model.exceptions.UserNotEnabledException;
import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static graduatethesis.performancemonitoringsystem.security.SecurityConstants.*;


@Component
public class JWTUtils {

    private final UserService userService;

    public JWTUtils(UserService userService) {
        this.userService = userService;
    }

    public List<GrantedAuthority> addAuthoritiesFromRoles(User user, String password) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.isEnabled()) {
            if (!userService.passwordMatches(user, password)) {
                throw new PasswordsNotTheSameException();
            }
            for (var role : user.getUserRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
            }
        } else {
            throw new UserNotEnabledException();
        }

        return authorities;
    }

    public String createToken(UserDetails userDetails) {
        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withArrayClaim(SecurityConstants.CLAIM_AUTHORITY, (userDetails).getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .sign(HMAC512(SECRET.getBytes()));

        return token;
    }

    public String createAndAppendToken(UserDetails userDetails, HttpServletResponse res) throws IOException {
        String token = createToken(userDetails);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.getWriter().append(token);

        return token;
    }

    public String createAndAppendToken(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
        UserDetails userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return createAndAppendToken(userDetails, res);
    }
}
