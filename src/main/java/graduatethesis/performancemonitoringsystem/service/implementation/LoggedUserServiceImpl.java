package graduatethesis.performancemonitoringsystem.service.implementation;

import graduatethesis.performancemonitoringsystem.model.users.User;
import graduatethesis.performancemonitoringsystem.repository.users.UserRepository;
import graduatethesis.performancemonitoringsystem.service.interfaces.LoggedUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {

    private final UserRepository userRepository;

    public LoggedUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getLoggedUserOptional() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String mail = authentication.getPrincipal().toString();

        return userRepository.findByEmailCustom(mail);
    }
    @Override
    public User getLoggedUser() {
        return getLoggedUserOptional();
    }
}
