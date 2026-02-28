package com.test.FundStack.service;

import com.test.FundStack.entity.Permission;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.PermissionRepo;
import org.springframework.stereotype.Service;

/**
 * @author Gemini CLI
 */

@Service
public class PermissionService extends CrudService<Permission, Long> {

    private final PermissionRepo permissionRepo;

    public PermissionService(CrudRepositoryBase<Permission, Long> repository, PermissionRepo permissionRepo) {
        super(repository);
        this.permissionRepo = permissionRepo;
    }
}
