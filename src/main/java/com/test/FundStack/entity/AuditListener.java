package com.test.FundStack.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

/**
 * @author Naveen
 * 
 * History
 * -03-03-2025 <NaveenDhanasekaran> AuditListener
 *      - InitialVersion
 * -14-10-2025 <NaveenDhanasekaran>
 *      - Added created by and updated by
 */

public class AuditListener {

    @PrePersist
    public void onPrePersist(BaseEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        String username = getCurrentUsername();
        
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity entity) {
        entity.setUpdatedBy(getCurrentUsername());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return "system";
    }
}
