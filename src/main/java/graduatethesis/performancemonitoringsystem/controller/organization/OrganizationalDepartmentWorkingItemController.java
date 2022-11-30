package graduatethesis.performancemonitoringsystem.controller.organization;

import graduatethesis.performancemonitoringsystem.model.helpers.OrganizationalDepartmentWorkingItemHelper;
import graduatethesis.performancemonitoringsystem.model.organization.OrganizationalDepartmentWorkingItem;
import graduatethesis.performancemonitoringsystem.service.interfaces.organization.OrganizationalDepartmentWorkingItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/orgDepartmentWorkingItem")
@PreAuthorize("isAuthenticated()")
public class OrganizationalDepartmentWorkingItemController {

    private final OrganizationalDepartmentWorkingItemService organizationalDepartmentWorkingItemService;

    public OrganizationalDepartmentWorkingItemController(OrganizationalDepartmentWorkingItemService organizationalDepartmentWorkingItemService) {
        this.organizationalDepartmentWorkingItemService = organizationalDepartmentWorkingItemService;
    }

    @PreAuthorize("@privilegeServiceImpl.loggedUserHasAnyPrivilege('ACCESS_ALL','HEAD_READ_DATA','READ_USER_DATA')")
    @PostMapping(value="/create")
    public List<OrganizationalDepartmentWorkingItem> save(@RequestBody OrganizationalDepartmentWorkingItemHelper organizationalDepartmentWorkingItemHelper){
        return this.organizationalDepartmentWorkingItemService.create(organizationalDepartmentWorkingItemHelper);
    }   

}