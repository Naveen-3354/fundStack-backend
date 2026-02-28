package com.test.FundStack.controller;

import com.test.FundStack.entity.Role;
import com.test.FundStack.service.CrudService;
import com.test.FundStack.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gemini CLI
 */

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController extends CrudController<Role, Long> {

    private final RoleService roleService;

    public RoleController(CrudService<Role, Long> service, RoleService roleService) {
        super(service);
        this.roleService = roleService;
    }
}
