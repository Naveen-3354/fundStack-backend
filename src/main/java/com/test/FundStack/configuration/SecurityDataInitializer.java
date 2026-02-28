package com.test.FundStack.configuration;

import com.test.FundStack.entity.Permission;
import com.test.FundStack.entity.Role;
import com.test.FundStack.repository.PermissionRepo;
import com.test.FundStack.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author Gemini CLI
 */

@Component
@RequiredArgsConstructor
public class SecurityDataInitializer implements CommandLineRunner {

    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;

    @Override
    @Transactional
    public void run(String... args) {
        // Create Permissions
        Permission readPerm = createPermissionIfNotFound("READ");
        Permission writePerm = createPermissionIfNotFound("WRITE");
        Permission deletePerm = createPermissionIfNotFound("DELETE");
        Permission managePerm = createPermissionIfNotFound("MANAGE");

        // Create Roles
        createRoleIfNotFound("ADMIN", Set.of(readPerm, writePerm, deletePerm, managePerm));
        createRoleIfNotFound("DEV", Set.of(readPerm, writePerm, managePerm));
        createRoleIfNotFound("MANAGE", Set.of(readPerm, writePerm));
        createRoleIfNotFound("USER", Set.of(readPerm));
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepo.findByName(name)
                .orElseGet(() -> permissionRepo.save(Permission.builder().name(name).build()));
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        return roleRepo.findByName(name)
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name(name)
                            .permissions(permissions)
                            .build();
                    return roleRepo.save(role);
                });
    }
}
