package com.test.FundStack.service;

import com.test.FundStack.entity.Role;
import com.test.FundStack.repository.CrudRepositoryBase;
import com.test.FundStack.repository.RoleRepo;
import org.springframework.stereotype.Service;

/**
 * @author Gemini CLI
 */

@Service
public class RoleService extends CrudService<Role, Long> {

    private final RoleRepo roleRepo;

    public RoleService(CrudRepositoryBase<Role, Long> repository, RoleRepo roleRepo) {
        super(repository);
        this.roleRepo = roleRepo;
    }
}
