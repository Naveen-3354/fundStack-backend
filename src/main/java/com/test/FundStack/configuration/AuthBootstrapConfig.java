package com.test.FundStack.configuration;

import com.test.FundStack.entity.AppRole;
import com.test.FundStack.entity.AppUser;
import com.test.FundStack.enums.RoleName;
import com.test.FundStack.repository.AppRoleRepository;
import com.test.FundStack.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthBootstrapConfig implements CommandLineRunner {

    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.bootstrap.admin-user-id:admin}")
    private String adminUserId;

    @Value("${security.bootstrap.admin-password:Admin@123}")
    private String adminPassword;

    @Value("${security.bootstrap.admin-email:admin@fundstack.local}")
    private String adminEmail;

    @Override
    @Transactional
    public void run(String... args) {
        for (RoleName roleName : RoleName.values()) {
            appRoleRepository.findByName(roleName).orElseGet(() ->
                    appRoleRepository.save(AppRole.builder().name(roleName).build())
            );
        }

        if (!appUserRepository.existsByUserId(adminUserId)) {
            AppRole adminRole = appRoleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow();

            appUserRepository.save(AppUser.builder()
                    .userId(adminUserId)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .active(true)
                    .loginRevoked(false)
                    .failedLoginAttempts(0)
                    .roles(Set.of(adminRole))
                    .build());
        }
    }
}
