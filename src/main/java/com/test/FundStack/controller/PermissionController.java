package com.test.FundStack.controller;

import com.test.FundStack.entity.Permission;
import com.test.FundStack.service.CrudService;
import com.test.FundStack.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gemini CLI
 */

@RestController
@RequestMapping("/api/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController extends CrudController<Permission, Long> {

    private final PermissionService permissionService;

    public PermissionController(CrudService<Permission, Long> service, PermissionService permissionService) {
        super(service);
        this.permissionService = permissionService;
    }
}
