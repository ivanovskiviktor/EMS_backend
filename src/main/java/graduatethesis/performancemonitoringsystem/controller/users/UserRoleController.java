package graduatethesis.performancemonitoringsystem.controller.users;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/userrole")
@PreAuthorize("isAuthenticated()")
public class UserRoleController {
}
